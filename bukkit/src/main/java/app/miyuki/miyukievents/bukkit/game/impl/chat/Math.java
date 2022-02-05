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

public class Math extends Game<Player> implements Chat {

    private Character operator;

    private Integer numberOne;
    private Integer numberTwo;

    private Integer result;

    public Math(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onChat(Player player, String message) {
        if (gameState != GameState.HAPPENING)
            return;

        if (!checkCost(player))
            return;

        if (!(StringUtils.isNumeric(message)))
            return;

        if (Integer.parseInt(message) == result)
            onWin(player);
    }

    @Override
    public void start() {
        setGameState(GameState.HAPPENING);
        setupResult();

        val expireTime = configProvider.provide(ConfigType.CONFIG).getInt("ExpireTime");

        Bukkit.getOnlinePlayers().forEach(player -> {
            messageDispatcher.dispatch(player, "Start", message -> message
                    .replace("{operator}", String.valueOf(operator))
                    .replace("{number1}", String.valueOf(numberOne))
                    .replace("{number2}", String.valueOf(numberTwo)));
        });

        schedulerManager.runAsync(expireTime * 20L, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "NoWinner"));
            stop();
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

        Bukkit.getOnlinePlayers().forEach(it -> {
            messageDispatcher.dispatch(player, "Win", message -> message
                    .replace("{winner}", player.getName())
                    .replace("{result}", String.valueOf(result)));
        });
    }

    @Override
    protected void giveReward(Player player) {
        this.reward.execute(player);
    }

    private void setupResult() {
        this.operator = RandomUtils.getRandomElement(getConfigProvider().provide(ConfigType.CONFIG).getStringList("SumTypes")).charAt(0);

        val min = getConfigProvider().provide(ConfigType.CONFIG).getInt("MinNumber");
        val max = getConfigProvider().provide(ConfigType.CONFIG).getInt("MaxNumber");

        this.numberOne = RandomUtils.generateRandomNumber(min, max);
        this.numberTwo = RandomUtils.generateRandomNumber(min, max);

        switch (this.operator) {
            case '+':
                this.result = numberOne + numberTwo;
                break;
            case '-':
                this.result = numberOne - numberTwo;
                break;
            case '*':
                this.result = numberOne * numberTwo;
                break;
        }
    }

}
