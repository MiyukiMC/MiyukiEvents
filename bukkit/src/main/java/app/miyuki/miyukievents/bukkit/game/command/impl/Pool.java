package app.miyuki.miyukievents.bukkit.game.command.impl;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.Command;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import app.miyuki.miyukievents.bukkit.util.title.TitleAnimation;
import com.google.common.collect.Lists;
import javafx.util.Pair;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Pool extends Command<User> {

    private final List<Player> players = Lists.newArrayList();

    public Pool(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (gameState != GameState.STARTED)
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

        plugin.getVaultProvider().provide().ifPresent(economyAPI -> economyAPI.withdraw(player.getUniqueId(), getCost()));
        players.add(player);
        messageDispatcher.dispatch(player, "YouEntered");
    }

    @Override
    public void start() {
        players.clear();
        setGameState(GameState.STARTED);

        val config = configProvider.provide(ConfigType.CONFIG);

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.runAsync(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                val seconds = calls.get() * interval;

                messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{size}", String.valueOf(players.size()))
                        .replace("{totalValue}", String.valueOf(getCost().multiply(BigDecimal.valueOf(players.size()))))
                        .replace("{cost}", String.valueOf(getCost()))
                        .replace("{seconds}", String.valueOf(seconds)));
            } else {

                if (players.size() < 2) {
                    messageDispatcher.globalDispatch("NoWinner");
                    players.forEach(player -> plugin.getVaultProvider().provide().ifPresent(economyAPI -> economyAPI.deposit(player.getUniqueId(), getCost())));
                    stop();
                } else {
                    val section = config.getConfigurationSection("RandomTitles");
                    if (section.getBoolean("Enabled")) {

                        Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "Raffling"));

                        List<Pair<String, String>> titles = Lists.newArrayList();

                        Player lastPlayer = null;

                        for (int i = 0; i < config.getInt("Calls"); i++) {

                            lastPlayer = RandomUtils.getRandomElement(players);

                            val title = ChatUtils.colorize(section.getString("Title").replace("{player}", lastPlayer.getName()));
                            val subtitle = ChatUtils.colorize(section.getString("Subtitle").replace("{player}", lastPlayer.getName()));

                            titles.add(new Pair<>(title, subtitle));

                        }

                        Player finalLastPlayer = lastPlayer;
                        TitleAnimation.Builder()
                                .animation(titles)
                                .period(config.getInt("Interval"))
                                .callback(() -> onWin(plugin.getUserRepository().findById(finalLastPlayer.getUniqueId())))
                                .build()
                                .start();

                    } else {
                        onWin(plugin.getUserRepository().findById(RandomUtils.getRandomElement(players).getUniqueId()));
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

        players.forEach(player -> plugin.getVaultProvider().provide().ifPresent(economyAPI -> economyAPI.deposit(player.getUniqueId(), getCost())));
    }

    @Override
    public void onWin(User user) {
        players.clear();
        stop();
        val total = getCost().multiply(BigDecimal.valueOf(players.size()));

        messageDispatcher.globalDispatch("Win", message -> message
                .replace("{winner}", user.getPlayerName())
                .replace("{money}", String.valueOf(total)));

        messageDispatcher.dispatch(Bukkit.getPlayer(user.getUuid()), "YouWin", message -> message
                .replace("{money}", String.valueOf(total)));

        giveReward(user);
    }

    @Override
    protected void giveReward(User user) {
        val total = getCost().multiply(BigDecimal.valueOf(players.size()));

        plugin.getVaultProvider().provide().ifPresent(economyAPI -> economyAPI.deposit(user.getUuid(), total));
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return true;
    }

}