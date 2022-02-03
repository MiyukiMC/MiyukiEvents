package app.miyuki.miyukievents.bukkit.game.impl.chat;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Lottery extends Game<Player> implements Chat {

    private Integer minNumber;
    private Integer maxNumber;

    private Integer result;

    public Lottery(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    // TODO: 31/01/2022  check cost to enter (needs VAULTAPI)
    @Override
    public void onChat(Player player, String message) {
        if (!player.hasPermission(getPermission()))
            return;

        if (!checkCost(player))
            return;

        if (StringUtils.isNumeric(message))
            return;

        if (Integer.parseInt(message) == result)
            onWin(player);
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void start() {
        setupResult();
        setGameState(GameState.HAPPENING);

        Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "Start", message -> message
                .replace("{minNumber}", String.valueOf(minNumber))
                .replace("{maxNumber}", String.valueOf(maxNumber))));
    }

    @Override
    public void stop() {
        setGameState(GameState.STOPPED);
    }

    @Override
    public void onWin(Player player) {
        stop();
        giveReward(player);

        Bukkit.getOnlinePlayers().forEach(it -> messageDispatcher.dispatch(it, "Win", message -> message
                .replace("{result}", String.valueOf(result))));
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
