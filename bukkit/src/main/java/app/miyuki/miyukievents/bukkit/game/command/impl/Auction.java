package app.miyuki.miyukievents.bukkit.game.command.impl;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.Command;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import app.miyuki.miyukievents.bukkit.util.number.NumberEvaluator;
import app.miyuki.miyukievents.bukkit.util.number.NumberFormatter;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import app.miyuki.miyukievents.bukkit.util.singlemap.Pair;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Auction extends Command<User> {

    @Getter
    private final List<AuctionItem> auctionItems = Lists.newArrayList();

    private AuctionItem auctionItem;

    private Pair<User, BigDecimal> lastBid = null;

    public Auction(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (gameState != GameState.STARTED)
            return;

        if (args.length < 1) {
            messageDispatcher.dispatch(player, "Help");
            return;
        }

        if (!NumberEvaluator.isValid(Double.parseDouble(args[0])) || !StringUtils.isNumeric(args[0])) {
            messageDispatcher.dispatch(player, "EnterAValidValue");
            return;
        }

        val economyAPI = plugin.getVaultProvider().provide().get();
        val uuid = player.getUniqueId();

        if (lastBid != null && lastBid.getFirst().getUuid().equals(uuid)) {
            messageDispatcher.dispatch(player, "TheLastBidIsYours");
            return;
        }

        val money = lastBid == null ? new BigDecimal(args[0]).add(auctionItem.getStartBid()) : new BigDecimal(args[0]).add(lastBid.getSecond());

        if (!economyAPI.has(uuid, money)) {
            messageDispatcher.dispatch(player, "YouDontHaveMoney");
            return;
        }

        val subtractedValue = money.subtract(lastBid == null ? auctionItem.getStartBid() : lastBid.getSecond());

        Function<String, String> format = (message -> message
                .replace("{min}", NumberFormatter.format(auctionItem.getMinDifferenceBetweenEntries()))
                .replace("{max}", NumberFormatter.format(auctionItem.getMaxDifferenceBetweenEntries())));

        if (subtractedValue.compareTo(auctionItem.getMinDifferenceBetweenEntries()) == -1) {
            messageDispatcher.dispatch(player, "MinMaxBetweenError", format);
            return;
        }

        if (subtractedValue.compareTo(auctionItem.getMaxDifferenceBetweenEntries()) == 1) {
            messageDispatcher.dispatch(player, "MinMaxBetweenError", format);
            return;
        }

        economyAPI.withdraw(player.getUniqueId(), money);

        if (lastBid != null) {
            economyAPI.deposit(lastBid.getFirst().getUuid(), lastBid.getSecond());
        }

        lastBid = new Pair<>(plugin.getUserRepository().findById(uuid), money);

        messageDispatcher.globalDispatch("PlayerEnteredInTheAuction", message -> message
                .replace("{player}", player.getName())
                .replace("{money}", money.toString()));

    }

    @Override
    public void start() {
        setupAuction();
        setGameState(GameState.STARTED);
        val config = configProvider.provide(ConfigType.CONFIG);

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.run(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{itemname}", auctionItem.getName())
                        .replace("{username}", lastBid == null ? "Ninguém" : lastBid.getFirst().getPlayerName())
                        .replace("{usernamebid}", lastBid == null ? "0" : lastBid.getSecond().toString())
                        .replace("{actualbid}", lastBid == null ? auctionItem.getStartBid().toString() : lastBid.getSecond().toString())
                        .replace("{mindifference}", auctionItem.getMinDifferenceBetweenEntries().toString())
                        .replace("{maxdifference}", auctionItem.getMaxDifferenceBetweenEntries().toString())
                        .replace("{seconds}", String.valueOf(calls.get() * interval)));

                calls.getAndDecrement();

            } else {
                schedulerManager.run(() -> {
                    if (lastBid == null) {
                        messageDispatcher.globalDispatch("NoWinner");
                        stop();
                    } else {
                        val user = lastBid.getFirst();
                        onWin(user);
                        messageDispatcher.globalDispatch("Win", message -> message
                                .replace("{name}", user.getPlayerName()));
                    }
                });
            }
        });

    }

    @Override
    public void stop() {
        setGameState(GameState.STOPPED);
        schedulerManager.cancel();

        if (lastBid != null) {
            val economyAPI = plugin.getVaultProvider().provide().get();
            economyAPI.deposit(lastBid.getFirst().getUuid(), lastBid.getSecond());
        }
    }

    @Override
    public void onWin(User user) {
        lastBid = null;
        stop();
        giveReward(user);
    }

    @Override
    protected void giveReward(User user) {
        auctionItem.execute(Bukkit.getPlayer(user.getUuid()));
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return true;
    }

    private void setupAuction() {
        auctionItems.clear();
        lastBid = null;

        val config = configProvider.provide(ConfigType.CONFIG).getConfig();
        val section = config.getConfigurationSection("Auctions");

        for (String key : section.getKeys(false)) {
            val auctionItemSection = section.getConfigurationSection(key);

            val name = ChatUtils.colorize(auctionItemSection.getString("Name"));
            val commands = auctionItemSection.getStringList("Commands");
            val startBid = new BigDecimal(auctionItemSection.getString("StartBid"));
            val minDifferenceBetweenEntries = new BigDecimal(auctionItemSection.getString("MinDifferenceBetweenEntries"));
            val maxDifferenceBetweenEntries = new BigDecimal(auctionItemSection.getString("MaxDifferenceBetweenEntries"));

            auctionItems.add(new AuctionItem(name, commands, startBid, minDifferenceBetweenEntries, maxDifferenceBetweenEntries));
        }

        this.auctionItem = RandomUtils.getRandomElement(auctionItems);
    }

    @AllArgsConstructor
    @Getter
    private static class AuctionItem {

        private String name;
        private List<String> commands;
        private BigDecimal startBid;
        private BigDecimal minDifferenceBetweenEntries;
        private BigDecimal maxDifferenceBetweenEntries;

        private void execute(Player player) {
            commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
        }
    }

}
