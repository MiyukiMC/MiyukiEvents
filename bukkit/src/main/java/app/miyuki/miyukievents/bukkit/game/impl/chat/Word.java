package app.miyuki.miyukievents.bukkit.game.impl.chat;

import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Word extends Game<Player> implements Chat<Player> {

    private String word;

    public Word(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onChat(Player player, String message) {
        if (message.equals(word))
            onWin(player);
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
        this.gameState = gameState;
    }

    @Override
    public void start() {
        if (configProvider.provide(ConfigType.CONFIG).getBoolean("Words.Random.Enabled")) {
            this.word = RandomUtils.generateRandomString(
                    configProvider.provide(ConfigType.CONFIG).getString("Words.Random.Characters").toCharArray(),
                    configProvider.provide(ConfigType.CONFIG).getInt("Words.Random.MaxCharacters")
            );
        } else {
            this.word = RandomUtils.getRandomElement(configProvider.provide(ConfigType.CONFIG).getStringList("Words.Words"));
        }

        Bukkit.getOnlinePlayers().forEach(it -> messageDispatcher.dispatch(it, "Start", message -> message.replace("{word}", word)));
        setGameState(GameState.HAPPENING);
    }

    @Override
    public void stop() {
        setGameState(GameState.STOPPED);
    }

    @Override
    public void onWin(Player player) {
        stop();
        giveReward(player);
        Bukkit.getOnlinePlayers().forEach(it -> messageDispatcher.dispatch(it, "Win", message ->
                message.replace("{winner}", player.getName())));
    }

    @Override
    public void giveReward(Player player) {
        this.reward.execute(player);
    }


}
