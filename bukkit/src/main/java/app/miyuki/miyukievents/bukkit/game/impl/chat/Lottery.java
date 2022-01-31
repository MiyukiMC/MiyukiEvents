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

public class Lottery extends Game<Player> implements Chat<Player> {

    private Integer minNumber;
    private Integer maxNumber;

    private Integer result;

    public Lottery(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    // TODO: 31/01/2022  check cost to enter (needs VAULTAPI)
    @Override
    public void onChat(Player player, String message) {
        // check cost

        if (StringUtils.isNumeric(message))
            return;

        if (Integer.parseInt(message) == result)
            onWin(player);
        else
            messageDispatcher.dispatch(player, "Lose");
    }

    @Override
    public String getTypeName() {
        return getConfigProvider().provide(ConfigType.CONFIG).getString("Type");
    }

    @Override
    public String getName() {
        return getConfigProvider().provide(ConfigType.CONFIG).getString("Name");
    }

    @Override
    public void setGameState(GameState gameState) {

    }

    // TODO: 31/01/2022 Runnable calls
    @Override
    public void start() {
        setupResult();

        Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "Start", message -> message
                .replace("{minNumber}", String.valueOf(minNumber))
                .replace("{maxNumber}", String.valueOf(maxNumber))));
    }

    @Override
    public void stop() {
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
    }

}
