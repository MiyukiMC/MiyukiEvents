package app.miyuki.miyukievents.bukkit.game.command;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class FastClick extends Command<User> {

    private String id;

    public FastClick(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (gameState != GameState.STARTED)
            return;

        if (args.length < 1)
            return;

        if (!player.hasPermission(getPermission())) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoPermission");
            return;
        }

        if (!checkCost(player)) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoMoney");
            return;
        }

        if (!args[0].equals(id)) {
            messageDispatcher.dispatch(player, "WrongPlace");
            return;
        }

        onWin(plugin.getUserRepository().findById(player.getUniqueId()));
    }

    @Override
    public void start() {
        setGameState(GameState.STARTED);
        this.id = RandomUtils.generateRandomString(10);
        System.out.println(id);

        val config = configProvider.provide(ConfigType.CONFIG).getConfig();

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.runAsync(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                val seconds = calls.get() * interval;

                messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{seconds}", String.valueOf(seconds)));

                calls.getAndDecrement();

            } else {

                messageDispatcher.globalDispatch("NoWinner");
                stop();

            }
        });
    }

    @Override
    public void stop() {
        setGameState(GameState.STOPPED);
        schedulerManager.cancel();
    }

    @Override
    public void onWin(User user) {
        stop();
        giveReward(user);

        messageDispatcher.globalDispatch("Win", message -> message
                .replace("{winner}", user.getPlayerName()));
    }

    @Override
    protected void giveReward(User user) {
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return false;
    }

}