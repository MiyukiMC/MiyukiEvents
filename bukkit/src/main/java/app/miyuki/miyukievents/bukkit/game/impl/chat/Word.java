package app.miyuki.miyukievents.bukkit.game.impl.chat;

import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class Word extends Game<Player> implements Chat {

    private String word;

    public Word(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onChat(Player player, String message) {
        if (!player.hasPermission(getPermission()))
            return;

        if (!checkCost(player))
            return;

        if (configProvider.provide(ConfigType.CONFIG).getBoolean("Words.IgnoreCase"))
            message = message.toLowerCase(Locale.ROOT);

        if (message.contains(word.toLowerCase(Locale.ROOT)))
            onWin(player);
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void start() {
        setupResult();

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

    private void setupResult() {
        val randomSection = configProvider.provide(ConfigType.CONFIG).getConfigurationSection("Words.Random");

        if (randomSection.getBoolean("Enabled")) {
            val min = randomSection.getInt("MinCharacters");
            val max = randomSection.getInt("MaxCharacters");

            val length = RandomUtils.generateRandomNumber(min, max);

            this.word = RandomUtils.generateRandomString(
                    randomSection.getString("Characters").toCharArray(),
                    length
            );

        } else {
            this.word = RandomUtils.getRandomElement(configProvider.provide(ConfigType.CONFIG).getStringList("Words.Words"));
        }

    }
}
