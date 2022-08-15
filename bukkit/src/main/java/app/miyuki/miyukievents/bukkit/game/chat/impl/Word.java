package app.miyuki.miyukievents.bukkit.game.chat.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.chat.ChatCommand;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.chat.Chat;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

@GameInfo(typeName = "Word", commandClass = ChatCommand.class)
public class Word extends Chat<User> {

    private String word;

    public Word(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
        super(config, messages, data);
    }

    @Override
    public void onChat(Player player, String[] args) {
        if (gameState != GameState.STARTED)
            return;

        if (args.length < 1)
            return;

        if (permission != null && !player.hasPermission(permission))
            return;

        if (!checkCost(player))
            return;

        var message = args[0];

        if (config.getRoot().node("Words", "IgnoreCase").getBoolean()) {
            message = message.toLowerCase(Locale.ROOT);
            word = word.toLowerCase(Locale.ROOT);
        }

        if (checkResult(message))
            return;

        val uniqueId = player.getUniqueId();
        val user = plugin.getUserRepository().findById(uniqueId).get(); // null check

        this.onWin(user);
    }

    @Override
    public void start() {
        this.setupResult();
        this.setGameState(GameState.STARTED);

        this.messageDispatcher.globalDispatch("Start", message -> message
                .replace("{word}", word));

        val expireTime = config.getRoot().node("ExpireTime").getInt();

        this.schedulerManager.runAsync(expireTime * 20L, () -> {
            this.messageDispatcher.globalDispatch("NoWinner");
            this.stop();
        });
    }

    @Override
    public void stop() {
        this.setGameState(GameState.STOPPED);
        this.schedulerManager.cancel();
    }

    @Override
    public void onWin(User user) {
        this.stop();
        this.giveReward(user);

        this.messageDispatcher.globalDispatch("Win",
                message -> message.replace("{winner}", user.getPlayerName())
        );
    }

    @Override
    public void giveReward(User user) {
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return false;
    }

    private boolean checkResult(String message) {
        return message.equals(this.word);
    }

    @SneakyThrows
    private void setupResult() {
        val configRoot = config.getRoot();
        val wordsNode = configRoot.node("Words");
        val randomNode = wordsNode.node("Random");

        if (randomNode.node("Enabled").getBoolean()) {
            val min = randomNode.node("MinCharacters").getInt();
            val max = randomNode.node("MaxCharacters").getInt();

            val length = RandomUtils.generateRandomNumber(min, max);

            this.word = RandomUtils.generateRandomString(
                    Objects.requireNonNull(randomNode.node("Characters").getString()).toCharArray(),
                    length
            );
        } else {
            this.word = RandomUtils.getRandomElement(wordsNode.node("Words").getList(String.class, ArrayList::new));
        }

    }
}
