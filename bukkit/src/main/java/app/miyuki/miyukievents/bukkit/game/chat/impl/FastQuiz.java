package app.miyuki.miyukievents.bukkit.game.chat.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.chat.GenericChatCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.chat.Chat;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@GameInfo(typeName = "FastQuiz", commandClass = GenericChatCommand.class)
public class FastQuiz extends Chat<User> {

    private final List<Question> questions = Lists.newArrayList();

    private Question question;

    public FastQuiz(@NotNull GameConfigProvider configProvider) {
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

        val message = String.join(" ", args);

        if (!question.compareAnswer(message))
            return;

        val uniqueId = player.getUniqueId();
        val user = plugin.getUserRepository().findById(uniqueId).get(); // null check

        this.onWin(user);
    }

    @Override
    public void start() {
        this.setupQuestions();
        this.setGameState(GameState.STARTED);

        val config = configProvider.provide(ConfigType.CONFIG);

        val calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        this.schedulerManager.runAsync(0L, interval * 20L, () -> {
            val seconds = calls.get() * interval;

            if (calls.get() > 0) {
                this.messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{seconds}", String.valueOf(seconds))
                        .replace("{question}", question.getQuestion()));

                calls.getAndDecrement();
                return;
            }

            this.messageDispatcher.globalDispatch("NoWinner", message ->
                    message.replace("{answer}", question.getAnswer()));

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

        this.messageDispatcher.globalDispatch("Win", message -> message
                .replace("{answer}", question.getAnswer())
                .replace("{winner}", user.getPlayerName()));

        this.giveReward(user);
    }

    @Override
    protected void giveReward(User user) {
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return false;
    }

    private void setupQuestions() {
        this.questions.clear();

        val config = configProvider.provide(ConfigType.CONFIG);
        val section = config.getConfigurationSection("Questions");

        for (val key : section.getKeys(false)) {
            val questionSection = section.getConfigurationSection(key);

            questions.add(new Question(
                    questionSection.getString("Question"),
                    questionSection.getString("Answer"),
                    questionSection.getBoolean("IgnoreCase")
            ));
        }

        this.question = RandomUtils.getRandomElement(questions);
    }

    @AllArgsConstructor
    @Getter
    private class Question {

        private String question;
        private String answer;
        private boolean ignoreCase;

        private boolean compareAnswer(String message) {
            return ignoreCase ? message.equalsIgnoreCase(answer) : message.equals(answer);
        }

    }

}
