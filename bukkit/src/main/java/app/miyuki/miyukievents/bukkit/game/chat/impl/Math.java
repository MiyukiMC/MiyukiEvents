package app.miyuki.miyukievents.bukkit.game.chat.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.chat.GenericChatCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.chat.Chat;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@GameInfo(typeName = "Math", commandClass = GenericChatCommand.class)
public class Math extends Chat<User> {

    private Character operator;

    private Integer numberOne;
    private Integer numberTwo;

    private Integer result;

    public Math(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onChat(Player player, String[] args) {
        if (gameState != GameState.STARTED)
            return;

        if (args.length < 1)
            return;

        if (!checkCost(player))
            return;

        if (!(StringUtils.isNumeric(args[0])))
            return;

        if (Integer.parseInt(args[0]) != result)
            return;

        val uniqueId = player.getUniqueId();
        val user = plugin.getUserRepository().findById(uniqueId).get();

        this.onWin(user);
    }

    @Override
    public void start() {
        this.setGameState(GameState.STARTED);
        this.setupResult();

        val expireTime = configProvider.provide(ConfigType.CONFIG).getInt("ExpireTime");

        this.messageDispatcher.globalDispatch("Start", message -> message
                .replace("{operator}", String.valueOf(operator))
                .replace("{number1}", String.valueOf(numberOne))
                .replace("{number2}", String.valueOf(numberTwo)));

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

        this.messageDispatcher.globalDispatch("Win", message -> message
                .replace("{winner}", user.getPlayerName())
                .replace("{result}", String.valueOf(result)));
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
        this.operator = RandomUtils.getRandomElement(
                getConfigProvider().provide(ConfigType.CONFIG).getStringList("SumTypes")
        ).charAt(0);

        val config = configProvider.provide(ConfigType.CONFIG);

        val min = config.getInt("MinNumber");
        val max = config.getInt("MaxNumber");

        this.numberOne = RandomUtils.generateRandomNumber(min, max);
        this.numberTwo = RandomUtils.generateRandomNumber(min, max);

        switch (this.operator) {
            case '+':
                this.result = numberOne + numberTwo;
                break;
            case '-':
                this.result = numberOne - numberTwo;
                break;
            case '*':
                this.result = numberOne * numberTwo;
                break;
            default:
                this.result = 0;
                break;
        }
    }

}
