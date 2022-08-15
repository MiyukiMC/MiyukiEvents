package app.miyuki.miyukievents.bukkit.game.chat.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.chat.ChatCommand;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.chat.Chat;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@GameInfo(typeName = "FastQuiz", commandClass = ChatCommand.class)
public class FastQuiz extends Chat<User> {

    private final List<Question> questions = Lists.newArrayList();

    private Question question;

    public FastQuiz(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
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

        if (!checkCost(player)) {
            return;
        }

        val message = String.join(" ", args);

        if (!question.compare(message))
            return;

        val uniqueId = player.getUniqueId();
        val user = plugin.getUserRepository().findById(uniqueId).get(); // null check

        this.onWin(user);
    }

    @Override
    public void start() {
        this.setupQuestions();
        this.setGameState(GameState.STARTED);

        val configRoot = config.getRoot();

        val calls = new AtomicInteger(configRoot.node("Calls").getInt());
        val interval = configRoot.node("CallInterval").getInt();

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
                    message.replace("{answer}", question.getDisplayAnswer()));

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
                .replace("{answer}", question.getDisplayAnswer())
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

    @SneakyThrows
    private void setupQuestions() {
        this.questions.clear();

        val configRoot = config.getRoot();
        val questionsNode = configRoot.node("Questions");


        for (Map.Entry<Object, CommentedConfigurationNode> entry : questionsNode.childrenMap().entrySet()) {
            val questionNode = entry.getValue();

            questions.add(new Question(
                    questionNode.node("Question").getString(),
                    questionNode.node("DisplayAnswer").getString(),
                    questionNode.node("Answers").getList(String.class, ArrayList::new),
                    questionNode.node("IgnoreCase").getBoolean()
            ));
        }

        this.question = RandomUtils.getRandomElement(questions);
    }

    @AllArgsConstructor
    @Getter
    private static class Question {

        private String question;
        private String displayAnswer;
        private List<String> answers;
        private boolean ignoreCase;

        private boolean compare(String message) {
            return ignoreCase ? compareAnswerIgnoringCase(message) : compareAnswer(message);
        }

        private boolean compareAnswerIgnoringCase(String message) {
            return answers.stream()
                    .anyMatch(answer -> answer.equalsIgnoreCase(message));
        }

        private boolean compareAnswer(String message) {
            return answers.stream()
                    .anyMatch(answer -> answer.equals(message));
        }

    }

}
