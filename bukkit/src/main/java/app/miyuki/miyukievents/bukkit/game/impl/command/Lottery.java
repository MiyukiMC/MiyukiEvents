package app.miyuki.miyukievents.bukkit.game.impl.command;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Command;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class Lottery extends Command<Player> {

    private Integer minNumber;
    private Integer maxNumber;

    private Integer result;

    public Lottery(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (gameState != GameState.STARTED && !StringUtils.isNumeric(args[1]))
            return;

        if (!player.hasPermission(getPermission())) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoPermissions");
            return;
        }

        if (!checkCost(player)) {
            messageDispatcher.dispatch(player, "");
            return;
        }

        if (Integer.parseInt(args[0]) != result) {
            messageDispatcher.dispatch(player, "WrongNumber");
            return;
        }

        onWin(player);
    }

    @Override
    public void start() {
        setupResult();
        setGameState(GameState.STARTED);

        val config = configProvider.provide(ConfigType.CONFIG);

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.runAsync(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                val seconds = calls.get() * interval;

                messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{minNumber}", String.valueOf(minNumber))
                        .replace("{maxNumber}", String.valueOf(maxNumber))
                        .replace("{seconds}", String.valueOf(seconds)));

                calls.getAndDecrement();

            } else {

                messageDispatcher.globalDispatch("NoWinner", message -> message
                        .replace("{result}", String.valueOf(result)));
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
    public void onWin(Player player) {
        stop();
        giveReward(player);

        messageDispatcher.globalDispatch("Win", message -> message
                .replace("{result}", String.valueOf(result))
                .replace("{winner}", player.getName()));
    }

    @Override
    protected void giveReward(Player player) {
        this.reward.execute(player);
    }

    @Override
    public boolean isEconomyRequired() {
        return false;
    }

    private void setupResult() {
        val config = configProvider.provide(ConfigType.CONFIG);
        this.minNumber = config.getInt("MinNumber");
        this.maxNumber = config.getInt("MaxNumber");

        this.result = RandomUtils.generateRandomNumber(minNumber, maxNumber);

        System.out.println(result);
    }

}
