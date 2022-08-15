package app.miyuki.miyukievents.bukkit.game.command.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.command.GenericCommandCommand;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.Command;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import app.miyuki.miyukievents.bukkit.util.title.TitleAnimation;
import com.google.common.collect.Lists;
import lombok.val;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@GameInfo(typeName = "Pool", commandClass = GenericCommandCommand.class)
public class Pool extends Command<User> {

    private final List<UUID> players = Lists.newArrayList();

    public Pool(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
        super(config, messages, data);
    }


    @Override
    public void onCommand(Player player, String[] args) {
        if (permission != null && !player.hasPermission(permission)) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoPermission");
            return;
        }

        if (players.contains(player.getUniqueId())) {
            messageDispatcher.dispatch(player, "YouAlreadyEntered");
            return;
        }

        if (!checkCost(player)) {
            messageDispatcher.dispatch(player, "YouDontHaveBalance");
            return;
        }

        this.plugin.getVaultProvider().provide().ifPresent(economyAPI -> economyAPI.withdraw(player.getUniqueId(), getCost()));
        this.players.add(player.getUniqueId());

        this.messageDispatcher.dispatch(player, "YouEntered");
    }

    @Override
    public void start() {
        this.players.clear();
        this.setGameState(GameState.STARTED);

        val configRoot = config.getRoot();

        val calls = new AtomicInteger(configRoot.node("Calls").getInt());
        val interval = configRoot.node("CallInterval").getInt();

        this.schedulerManager.runAsync(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                val seconds = calls.get() * interval;

                this.messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{size}", String.valueOf(players.size()))
                        .replace("{totalValue}", String.valueOf(getCost().multiply(BigDecimal.valueOf(players.size()))))
                        .replace("{cost}", String.valueOf(getCost()))
                        .replace("{seconds}", String.valueOf(seconds)));

                calls.getAndDecrement();
                return;
            }

            if (players.size() < 2) {
                this.messageDispatcher.globalDispatch("NoWinner");

                // change economyAPI to class variable?
                this.players.forEach(uuid ->
                        plugin.getVaultProvider().provide().ifPresent(economyAPI ->
                                economyAPI.deposit(uuid, getCost()))
                );

                this.stop();
                return;
            }

            val randomTitlesNode = configRoot.node("RandomTitles");

            if (randomTitlesNode.node("Enabled").getBoolean()) {

                this.messageDispatcher.globalDispatch("Raffling");

                List<Title> titles = Lists.newArrayList();

                OfflinePlayer lastPlayer = null;

                for (int i = 0; i < configRoot.node("Calls").getInt(); i++) {

                    UUID uuid = RandomUtils.getRandomElement(players);
                    lastPlayer = Bukkit.getOfflinePlayer(uuid);

                    val title = ChatUtils.colorize(
                            randomTitlesNode.node("Title").getString("").replace("{player}", lastPlayer.getName())
                    );
                    val subtitle = ChatUtils.colorize(
                            randomTitlesNode.node("Subtitle").getString("").replace("{player}", lastPlayer.getName())
                    );

                    titles.add(Title.title(title, subtitle));
                }

                OfflinePlayer finalLastPlayer = lastPlayer;
                TitleAnimation.Builder()
                        .animation(titles)
                        .period(configRoot.node("Interval").getInt())
                        .callback(() -> onWin(plugin.getUserRepository().findById(finalLastPlayer.getUniqueId()).get()))
                        .build()
                        .start();
                return;
            }

            this.onWin(plugin.getUserRepository().findById(RandomUtils.getRandomElement(players)).get());
        });
    }

    @Override
    public void stop() {
        this.setGameState(GameState.STOPPED);
        this.schedulerManager.cancel();

        this.players.forEach(uuid ->
                // change this to local/class variable
                plugin.getVaultProvider().provide().ifPresent(economyAPI -> economyAPI.deposit(uuid, getCost()))
        );
    }

    @Override
    public void onWin(User user) {
        this.players.clear();
        this.stop();

        val total = getCost().multiply(BigDecimal.valueOf(players.size()));

        this.messageDispatcher.globalDispatch("Win", message -> message
                .replace("{winner}", user.getPlayerName())
                .replace("{money}", String.valueOf(total)));


        user.getPlayer().ifPresent(player -> this.messageDispatcher.dispatch(player, "YouWin", message -> message
                .replace("{money}", String.valueOf(total))));

        this.giveReward(user);
    }

    @Override
    protected void giveReward(User user) {
        val total = getCost().multiply(BigDecimal.valueOf(players.size()));

        this.plugin.getVaultProvider().provide().ifPresent(economyAPI -> economyAPI.deposit(user.getUuid(), total));
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return true;
    }

}
