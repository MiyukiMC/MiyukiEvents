package app.miyuki.miyukievents.bukkit.game.impl.chat;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
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

public class FastQuiz extends Chat<Player> {

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

        String message = String.join(" ", args);

        if (question.compareAnswer(message))
            onWin(player);
    }

    @Override
    public void start() {
        setupQuestions();
        setGameState(GameState.STARTED);

        val config = configProvider.provide(ConfigType.CONFIG);

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.runAsync(0L, interval * 20L, () -> {
            val seconds = calls.get() * interval;

            if (calls.get() > 0) {
                Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "Start", message -> message
                        .replace("{seconds}", String.valueOf(seconds))
                        .replace("{question}", question.getQuestion())));

                calls.getAndDecrement();
            } else {

                Bukkit.getOnlinePlayers().forEach(player -> messageDispatcher.dispatch(player, "NoWinner", message -> message
                        .replace("{answer}", question.getAnswer())));

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

        Bukkit.getOnlinePlayers().forEach(it -> messageDispatcher.dispatch(it, "Win", message -> message
                .replace("{answer}", question.getAnswer())
                .replace("{winner}", player.getName())));

        giveReward(player);
    }

    @Override
    protected void giveReward(Player player) {
        this.reward.execute(player);
    }

    private void setupQuestions() {
        questions.clear();

        val config = configProvider.provide(ConfigType.CONFIG);
        val section = config.getConfigurationSection("Questions");

        for (String key : section.getKeys(false)) {
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
    private static class Question {

        private String question;
        private String answer;
        private boolean ignoreCase;

        private boolean compareAnswer(String message) {
            return ignoreCase ? message.equalsIgnoreCase(answer) : message.equals(answer);
        }

    }

}
