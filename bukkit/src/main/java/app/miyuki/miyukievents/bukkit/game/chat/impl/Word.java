package app.miyuki.miyukievents.bukkit.game.chat.impl;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.chat.Chat;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class Word extends Chat<User> {

    private String word;

    public Word(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onChat(Player player, String[] args) {
        if (gameState != GameState.STARTED)
            return;

        if (args.length < 1)
            return;

        if (!player.hasPermission(getPermission()))
            return;

        if (!checkCost(player))
            return;

        String message = args[0];

        if (configProvider.provide(ConfigType.CONFIG).getBoolean("Words.IgnoreCase")) {
            message = message.toLowerCase(Locale.ROOT);
            word = word.toLowerCase(Locale.ROOT);
        }

        if (message.equals(word))
            onWin(plugin.getUserRepository().findById(player.getUniqueId()));
    }

    @Override
    public void start() {
        setupResult();
        setGameState(GameState.STARTED);

        messageDispatcher.globalDispatch("Start", message -> message
                .replace("{word}", word));

        val expireTime = configProvider.provide(ConfigType.CONFIG).getInt("ExpireTime");

        schedulerManager.runAsync(expireTime * 20L, () -> {
            messageDispatcher.globalDispatch("NoWinner");
            stop();
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

        messageDispatcher.globalDispatch("Win", message -> message.replace("{winner}", user.getPlayerName()));
    }

    @Override
    public void giveReward(User user) {
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return false;
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