package app.miyuki.miyukievents.bukkit.game.command.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.command.GenericCommandCommand;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.Command;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.number.NumberEvaluator;
import app.miyuki.miyukievents.bukkit.util.number.NumberFormatter;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import app.miyuki.miyukievents.bukkit.util.singlemap.Pair;
import com.google.common.collect.Lists;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@GameInfo(typeName = "Auction", commandClass = GenericCommandCommand.class)
public class Auction extends Command<User> {

    @Getter
    private final List<AuctionItem> auctionItems = Lists.newArrayList();

    @Getter
    @Setter
    private AuctionItem auctionItem;

    private Pair<User, BigDecimal> lastBid = null;

    public Auction(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
        super(config, messages, data);
    }


    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length != 1) {
            this.messageDispatcher.dispatch(player, "CommandUsedIncorrectly");
            return;
        }

        if (!NumberEvaluator.isDouble(args[0])) {
            messageDispatcher.dispatch(player, "EnterAValidValue");
            return;
        }

        // change to class field?
        val economyAPI = this.plugin.getVaultProvider().provide().get(); // null check
        val uuid = player.getUniqueId();

        if (this.lastBid != null && this.lastBid.getFirst().getUuid().equals(uuid)) {
            messageDispatcher.dispatch(player, "TheLastBidIsYours");
            return;
        }

        val amount = new BigDecimal(args[0]);

        val money = lastBid == null
                ? amount.add(auctionItem.getStartBid())
                : amount.add(lastBid.getSecond());

        if (!economyAPI.has(uuid, money)) {
            this.messageDispatcher.dispatch(player, "YouDontHaveMoney");
            return;
        }

        val subtractedValue = money.subtract(lastBid == null ? auctionItem.getStartBid() : lastBid.getSecond());

        final Function<String, String> format = (message -> message
                .replace("{min}", NumberFormatter.format(auctionItem.getMinDifferenceBetweenEntries()))
                .replace("{max}", NumberFormatter.format(auctionItem.getMaxDifferenceBetweenEntries())));

        if (subtractedValue.compareTo(auctionItem.getMinDifferenceBetweenEntries()) < 0) {
            messageDispatcher.dispatch(player, "MinMaxBetweenError", format);
            return;
        }

        if (subtractedValue.compareTo(auctionItem.getMaxDifferenceBetweenEntries()) > 0) {
            messageDispatcher.dispatch(player, "MinMaxBetweenError", format);
            return;
        }

        economyAPI.withdraw(player.getUniqueId(), money);

        if (this.lastBid != null)
            economyAPI.deposit(lastBid.getFirst().getUuid(), lastBid.getSecond());

        this.lastBid = new Pair<>(plugin.getUserRepository().findById(uuid).get(), money);

        this.messageDispatcher.globalDispatch("PlayerEnteredInTheAuction", message -> message
                .replace("{player}", player.getName())
                .replace("{money}", money.toString()));
    }

    @Override
    public void start() {
        this.setGameState(GameState.STARTED);
        val configRoot = config.getRoot();

        val calls = new AtomicInteger(configRoot.node("Calls").getInt());
        val interval = configRoot.node("CallInterval").getInt();

        final Function<String, String> format = message -> message
                .replace("{itemname}", auctionItem.getName())
                .replace("{itemnamedisplayname}", auctionItem.getDisplayName())
                .replace("{username}", lastBid == null ? "Ninguém" : lastBid.getFirst().getPlayerName())
                .replace("{usernamebid}", lastBid == null ? "0" : lastBid.getSecond().toString())
                .replace("{actualbid}", lastBid == null ? auctionItem.getStartBid().toString() : lastBid.getSecond().toString())
                .replace("{mindifference}", auctionItem.getMinDifferenceBetweenEntries().toString())
                .replace("{maxdifference}", auctionItem.getMaxDifferenceBetweenEntries().toString())
                .replace("{seconds}", String.valueOf(calls.get() * interval));

        this.schedulerManager.run(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                this.messageDispatcher.globalDispatch("Start", format);

                calls.getAndDecrement();
                return;
            }

            this.schedulerManager.run(() -> {
                if (lastBid == null) {
                    this.messageDispatcher.globalDispatch("NoWinner");
                    this.stop();
                    return;
                }

                val user = lastBid.getFirst();

                this.onWin(user);

                // maybe change this to onWin function?
                this.messageDispatcher.globalDispatch("Win", message -> message
                        .replace("{name}", user.getPlayerName()));
            });

        });

    }

    @Override
    public void stop() {
        this.setGameState(GameState.STOPPED);
        this.schedulerManager.cancel();


        if (this.lastBid != null) {
            // change to class variable
            val economyAPI = plugin.getVaultProvider().provide().get();
            economyAPI.deposit(lastBid.getFirst().getUuid(), lastBid.getSecond());
        }
    }

    @Override
    public void onWin(User user) {
        this.lastBid = null;
        this.stop();
        this.giveReward(user);

        this.messageDispatcher.globalDispatch("Win", message -> message
                .replace("{name}", user.getPlayerName()));
    }

    @Override
    protected void giveReward(User user) {
        // check if player is offline
        this.auctionItem.execute(Bukkit.getPlayer(user.getUuid()));
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return true;
    }

    public void setupAuctionItems() {
        this.auctionItems.clear();
        this.lastBid = null;

        val configRoot = config.getRoot();
        val auctionsNode = configRoot.node("Auctions");

        for (Map.Entry<Object, CommentedConfigurationNode> entry : auctionsNode.childrenMap().entrySet()) {
            auctionItems.add(AuctionItem.fromConfig(entry.getValue()));
        }

        this.auctionItem = RandomUtils.getRandomElement(auctionItems);
    }

    @Nullable
    public AuctionItem getAuctionItemByName(@NotNull String name) {
        return this.auctionItems.stream()
                .filter(auctionItem -> auctionItem.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public AuctionItem getRandomAuctionItem() {
        return RandomUtils.getRandomElement(auctionItems);
    }

    @AllArgsConstructor
    @Getter
    public static class AuctionItem {

        private String name;
        private String displayName;
        private List<String> commands;
        private BigDecimal startBid;
        private BigDecimal minDifferenceBetweenEntries;
        private BigDecimal maxDifferenceBetweenEntries;

        @SneakyThrows
        private static AuctionItem fromConfig(@NotNull CommentedConfigurationNode node) {
            return new AuctionItem(
                    (String) node.key(),
                    node.node("Name").getString(),
                    node.node("Commands").getList(String.class, ArrayList::new),
                    new BigDecimal(Objects.requireNonNull(node.node("StartBid").getString())),
                    new BigDecimal(Objects.requireNonNull(node.node("MinDifferenceBetweenEntries").getString())),
                    new BigDecimal(Objects.requireNonNull(node.node("MaxDifferenceBetweenEntries").getString()))
            );
        }

        private void execute(Player player) {
            commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName())));
        }
    }

}
