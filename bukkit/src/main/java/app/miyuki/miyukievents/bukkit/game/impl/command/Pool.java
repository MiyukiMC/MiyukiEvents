package app.miyuki.miyukievents.bukkit.game.impl.command;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Command;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import com.cryptomorin.xseries.messages.Titles;
import com.google.common.collect.Lists;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Pool extends Game<Player> implements Command {

    private final List<Player> players = Lists.newArrayList();

    public Pool(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (gameState != GameState.HAPPENING)
            return;

        if (!player.hasPermission(getPermission())) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoPermission");
            return;
        }

        if (players.contains(player)) {
            messageDispatcher.dispatch(player, "YouAlreadyEntered");
            return;
        }

        if (!checkCost(player)) {
            messageDispatcher.dispatch(player, "YouDontHaveBalance");
            return;
        }

        plugin.getVaultProvider().provide().withdrawPlayer(player, getCost());
        players.add(player);
        messageDispatcher.dispatch(player, "YouEntered");
    }

    @Override
    public void start() {
        players.clear();
        setGameState(GameState.HAPPENING);

        val config = configProvider.provide(ConfigType.CONFIG);

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.runAsync(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                val seconds = calls.get() * interval;

                Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "Start", message -> message
                        .replace("{size}", String.valueOf(players.size()))
                        .replace("{totalValue}", String.valueOf(players.size() * getCost()))
                        .replace("{cost}", String.valueOf(getCost()))
                        .replace("{seconds}", String.valueOf(seconds))));
            } else {

                if (players.size() < 2) {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        messageDispatcher.dispatch(player, "NoWinner");
                    });
                    players.forEach(player -> plugin.getVaultProvider().provide().depositPlayer(player, getCost()));
                    stop();
                } else {
                    val section = config.getConfigurationSection("RandomTitles");
                    if (section.getBoolean("Enabled")) {
                        Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "Raffling"));

                        AtomicInteger titleCalls = new AtomicInteger(config.getInt("Calls"));

                        val title = ChatUtils.colorize(section.getString("Title"));
                        val subtitle = ChatUtils.colorize(section.getString("Subtitle"));

                        AtomicReference<Player> winner = new AtomicReference<>();

                        schedulerManager.runAsync(0L, 20L, () -> {

                            if (titleCalls.get() > 0) {
                                val randomPlayer = RandomUtils.getRandomElement(players);
                                winner.set(randomPlayer);
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                    Titles.sendTitle(
                                            player,
                                            title.replace("{player}", randomPlayer.getName()),
                                            subtitle.replace("{player}", randomPlayer.getName()
                                            ));
                                });

                                titleCalls.getAndDecrement();
                            } else {
                                onWin(winner.get());
                            }

                        });

                    } else {
                        onWin(RandomUtils.getRandomElement(players));
                    }
                }
            }

            calls.getAndDecrement();
        });
    }

    @Override
    public void stop() {
        setGameState(GameState.STOPPED);
        schedulerManager.cancel();
    }

    @Override
    public void onWin(Player player) {
        stop();
        val total = players.size() * getCost();

        Bukkit.getOnlinePlayers().forEach(otherPlayer -> {
            messageDispatcher.dispatch(otherPlayer, "Win", message -> message
                    .replace("{winner}", player.getName())
                    .replace("{money}", String.valueOf(total)));
        });

        messageDispatcher.dispatch(player, "YouWin", message -> message
                .replace("{money}", String.valueOf(getCost())));

        plugin.getVaultProvider().provide().depositPlayer(player, total);
        giveReward(player);
    }

    @Override
    protected void giveReward(Player player) {
        this.reward.execute(player);
    }

}
