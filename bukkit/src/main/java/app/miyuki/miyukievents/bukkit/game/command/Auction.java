package app.miyuki.miyukievents.bukkit.game.command;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class Auction extends Command<User> {

    private final List<AuctionItem> auctionItems = Lists.newArrayList();

    private AuctionItem auctionItem;

    private BigDecimal lastBid;

    public Auction(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (gameState != GameState.STARTED)
            return;

        if (args.length < 1)
            return;
    }

    @Override
    public void start() {
        setupAuctionItems();
        this.auctionItem = RandomUtils.getRandomElement(auctionItems);

    }

    @Override
    public void stop() {
        setGameState(GameState.STOPPED);
    }

    @Override
    public void onWin(User user) {

    }

    @Override
    protected void giveReward(User user) {
        stop();
    }

    @Override
    public boolean isEconomyRequired() {
        return true;
    }

    private void setupAuctionItems() {
        auctionItems.clear();

        val config = configProvider.provide(ConfigType.CONFIG).getConfig();
        val section = config.getConfigurationSection("Auction");

        for (String key : section.getKeys(false)) {
            val auctionItemSection = section.getConfigurationSection(key);

            val name = ChatUtils.colorize(auctionItemSection.getString("Name"));
            val commands = auctionItemSection.getStringList("Commands");
            val startBid = new BigDecimal(auctionItemSection.getString("StartBid"));
            val minDifferenceBetweenEntries = new BigDecimal(auctionItemSection.getString("MinDifferenceBetweenEntries"));

            auctionItems.add(new AuctionItem(name, commands, startBid, minDifferenceBetweenEntries));
        }
    }

    @AllArgsConstructor
    @Getter
    public static class AuctionItem {

        private String name;
        private List<String> commands;
        private BigDecimal startBid;
        private BigDecimal minDifferenceBetweenEntries;

        private void execute(Player player) {
            commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
        }
    }

}
