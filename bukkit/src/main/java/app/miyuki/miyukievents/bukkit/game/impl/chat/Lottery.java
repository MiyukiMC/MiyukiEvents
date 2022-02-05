package app.miyuki.miyukievents.bukkit.game.impl.chat;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class Lottery extends Game<Player> implements Chat {

    private Integer minNumber;
    private Integer maxNumber;

    private Integer result;

    public Lottery(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onChat(Player player, String[] args) {
        if (gameState != GameState.HAPPENING)
            return;

        if (args.length < 1)
            return;

        if (!player.hasPermission(getPermission()))
            return;

        if (!checkCost(player))
            return;

        if (!StringUtils.isNumeric(args[0]))
            return;

        if (Integer.parseInt(args[0]) == result)
            onWin(player);
    }

    @Override
    public void start() {
        setupResult();
        setGameState(GameState.HAPPENING);

        val config = configProvider.provide(ConfigType.CONFIG);

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.runAsync(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                val seconds = calls.get() * interval;

                Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "Start", message -> message
                        .replace("{minNumber}", String.valueOf(minNumber))
                        .replace("{maxNumber}", String.valueOf(maxNumber))
                        .replace("{seconds}", String.valueOf(seconds))));

                calls.getAndDecrement();

            } else {

                Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "NoWinner"));
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

        Bukkit.getOnlinePlayers().forEach(it -> messageDispatcher.dispatch(it, "Win", message -> message
                .replace("{result}", String.valueOf(result))
                .replace("{winner}", player.getName())));
    }

    @Override
    protected void giveReward(Player player) {
        this.reward.execute(player);
    }

    private void setupResult() {
        this.minNumber = getConfigProvider().provide(ConfigType.CONFIG).getInt("MinNumber");
        this.maxNumber = getConfigProvider().provide(ConfigType.CONFIG).getInt("MaxNumber");

        this.result = RandomUtils.generateRandomNumber(minNumber, maxNumber);

        System.out.println(result);
    }

}
