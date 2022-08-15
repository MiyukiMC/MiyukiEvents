package app.miyuki.miyukievents.bukkit.game.chat.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.chat.ChatCommand;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.chat.Chat;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.number.NumberEvaluator;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

@GameInfo(typeName = "Lottery", commandClass = ChatCommand.class)
public class Lottery extends Chat<User> {

    // Maybe change this to local variable
    private Integer minNumber;
    private Integer maxNumber;

    private Integer result;

    public Lottery(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
        super(config, messages, data);
    }


    @Override
    public void onChat(Player player, String[] args) {
        if (gameState != GameState.STARTED)
            return;

        if (args.length < 1)
            return;

        if (!NumberEvaluator.isInteger(args[0]))
            return;

        if (!checkCost(player))
            return;

        if (Integer.parseInt(args[0]) != result)
            return;

        val uniqueId = player.getUniqueId();
        val user = plugin.getUserRepository().findById(uniqueId).get(); // null check

        this.onWin(user);
    }

    @Override
    public void start() {
        this.setupResult();
        this.setGameState(GameState.STARTED);

        val configRoot = config.getRoot();

        val calls = new AtomicInteger(configRoot.node("Calls").getInt());
        val interval = configRoot.node("CallInterval").getInt();

        this.schedulerManager.runAsync(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                val seconds = calls.get() * interval;

                this.messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{minNumber}", String.valueOf(minNumber))
                        .replace("{maxNumber}", String.valueOf(maxNumber))
                        .replace("{seconds}", String.valueOf(seconds)));

                calls.getAndDecrement();
                return;
            }

            this.messageDispatcher.globalDispatch("NoWinner", message -> message
                    .replace("{result}", String.valueOf(result)));
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

        this.messageDispatcher.globalDispatch("Win", message -> message
                .replace("{result}", String.valueOf(result))
                .replace("{winner}", user.getPlayerName()));
    }

    @Override
    protected void giveReward(User user) {
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return false;
    }

    private void setupResult() {
        val configRoot = config.getRoot();
        this.minNumber = configRoot.node("MinNumber").getInt();
        this.maxNumber = configRoot.node("MaxNumber").getInt();

        this.result = RandomUtils.generateRandomNumber(minNumber, maxNumber);

        System.out.println(result); // delete this after
    }

}
