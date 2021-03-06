package app.miyuki.miyukievents.bukkit.game.command.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.command.GenericCommandCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
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

@GameInfo(typeName = "Pool", commandClass = GenericCommandCommand.class)
public class Pool extends Command<User> {

    // maybe change to UUID or String
    private final List<Player> players = Lists.newArrayList();

    public Pool(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (this.gameState != GameState.STARTED)
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

        this.plugin.getVaultProvider().provide().ifPresent(economyAPI -> economyAPI.withdraw(player.getUniqueId(), getCost()));
        this.players.add(player);

        this.messageDispatcher.dispatch(player, "YouEntered");
    }

    @Override
    public void start() {
        this.players.clear();
        this.setGameState(GameState.STARTED);

        val config = configProvider.provide(ConfigType.CONFIG);

        val calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

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
                this.players.forEach(player ->
                        plugin.getVaultProvider().provide().ifPresent(economyAPI ->
                                economyAPI.deposit(player.getUniqueId(), getCost()))
                );

                this.stop();
                return;
            }

            val section = config.getConfigurationSection("RandomTitles");

            if (section.getBoolean("Enabled")) {

                this.messageDispatcher.globalDispatch("Raffling");

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
                        .callback(() -> onWin(plugin.getUserRepository().findById(finalLastPlayer.getUniqueId()).get()))
                        .build()
                        .start();
                return;
            }

            this.onWin(plugin.getUserRepository().findById(RandomUtils.getRandomElement(players).getUniqueId()).get());
        });
    }

    @Override
    public void stop() {
        this.setGameState(GameState.STOPPED);
        this.schedulerManager.cancel();

        this.players.forEach(player ->
                // change this to local/class variable
                plugin.getVaultProvider().provide().ifPresent(economyAPI -> economyAPI.deposit(player.getUniqueId(), getCost()))
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

        this.messageDispatcher.dispatch(Bukkit.getPlayer(user.getUuid()), "YouWin", message -> message
                .replace("{money}", String.valueOf(total)));

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
