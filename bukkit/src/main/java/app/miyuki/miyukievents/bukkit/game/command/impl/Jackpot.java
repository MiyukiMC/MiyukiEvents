package app.miyuki.miyukievents.bukkit.game.command.impl;

import app.miyuki.miyukievents.bukkit.commands.impl.command.GenericCommandCommand;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.GameInfo;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.Command;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.number.NumberEvaluator;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@GameInfo(typeName = "Jackpot", commandClass = GenericCommandCommand.class)
public class Jackpot extends Command<User> {

    @Getter
    private final Map<User, BigDecimal> players = Maps.newHashMap();

    private BigDecimal maxBet;

    public Jackpot(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
        super(config, messages, data);
    }


    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length != 1) {
            this.messageDispatcher.dispatch(player, "CommandUsedIncorrectly");
            return;
        }

        if (permission != null && !player.hasPermission(permission)) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoPermission");
            return;
        }

        if (!NumberEvaluator.isDouble(args[0])) {
            messageDispatcher.dispatch(player, "EnterAValidValue");
            return;
        }

        // refactor this
        val economyAPI = plugin.getVaultProvider().provide().get();

        val uuid = player.getUniqueId();
        val money = new BigDecimal(args[0]);

        val uniqueId = player.getUniqueId();

        val user = plugin.getUserRepository().findById(uniqueId).get(); // null check

        val playerBet = players.get(user);

        if (playerBet != null && playerBet.compareTo(maxBet) == 0) {
            messageDispatcher.dispatch(player, "YouAlreadyBetTheMost");
            return;
        }

        if (money.compareTo(playerBet) > 0) {
            messageDispatcher.dispatch(player, "ValueGreaterMax");
            return;
        }

        if (!economyAPI.has(uuid, money)) {
            messageDispatcher.dispatch(player, "YouDontHaveMoney");
            return;
        }

        // use .replace?
        if (playerBet != null) {
            val oldValue = players.get(user);
            players.replace(user, money.add(oldValue));
        } else
            players.put(user, money);

        // change to class variable?
        economyAPI.withdraw(uuid, money);

        this.messageDispatcher.dispatch(player, "YouEntered", message -> message
                .replace("{chance}", String.valueOf(RandomUtils.getChance(players, user)))
                .replace("{money}", String.format("%.2f", players.get(user))));
    }

    @Override
    public void start() {
        this.players.clear();

        val configRoot = config.getRoot();

        this.maxBet = new BigDecimal(configRoot.node("MaxBet").getString("1000"));

        this.setGameState(GameState.STARTED);

        val calls = new AtomicInteger(configRoot.node("Calls").getInt());
        val interval = configRoot.node("CallInterval").getInt();

        this.schedulerManager.runAsync(0L, interval * 20L, () -> {
            val seconds = calls.get() * interval;

            if (calls.get() > 0) {
                this.messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{size}", String.valueOf(players.size()))
                        .replace("{total}", String.valueOf(players.values().stream().reduce(BigDecimal.valueOf(0), BigDecimal::add)))
                        .replace("{maxBet}", String.valueOf(maxBet))
                        .replace("{seconds}", String.valueOf(seconds)));

                calls.getAndDecrement();
                return;
            }

            if (players.size() >= 2) {
                val winner = RandomUtils.getWeightedRandom(players);
                this.onWin(winner);
            } else {
                messageDispatcher.globalDispatch("NoWinner");
                this.stop();
            }


        });
    }

    @Override
    public void stop() {
        this.setGameState(GameState.STOPPED);
        this.schedulerManager.cancel();

        // change to class variable?
        val economyAPI = plugin.getVaultProvider().provide().get();

        this.players.forEach((user, value) ->
                economyAPI.deposit(user.getUuid(), value)
        );
    }

    @Override
    public void onWin(User user) {
        this.giveReward(user);

        val total = players.values().stream().reduce(BigDecimal.valueOf(0), BigDecimal::add);

        this.players.clear();
        this.stop();

        this.messageDispatcher.globalDispatch("Win", message -> message
                .replace("{chance}", String.valueOf(RandomUtils.getChance(players, user)))
                .replace("{total}", String.valueOf(total))
                .replace("{winner}", user.getPlayerName()));
    }

    @Override
    protected void giveReward(User user) {
        val total = players.values().stream().reduce(BigDecimal.valueOf(0), BigDecimal::add);

        // change to class variable?
        plugin.getVaultProvider().provide().get().deposit(user.getUuid(), total);

        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return true;
    }

}
