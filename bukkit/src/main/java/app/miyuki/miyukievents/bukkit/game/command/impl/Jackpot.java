package app.miyuki.miyukievents.bukkit.game.command.impl;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.Command;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.util.number.NumberEvaluator;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Jackpot extends Command<User> {

    @Getter
    private final Map<User, BigDecimal> players = Maps.newHashMap();

    private BigDecimal maxBet;

    public Jackpot(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onCommand(Player player, String[] args) {

        if (gameState != GameState.STARTED) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "GameNotFound");
            return;
        }

        if (!player.hasPermission(getPermission())) {
            plugin.getGlobalMessageDispatcher().dispatch(player, "NoPermission");
            return;
        }

        if (!StringUtils.isNumeric(args[0]) || !NumberEvaluator.isValid(Double.parseDouble(args[0]))) {
            messageDispatcher.dispatch(player, "EnterAValidValue");
            return;
        }

        val economyAPI = plugin.getVaultProvider().provide().get();
        val uuid = player.getUniqueId();
        val money = new BigDecimal(args[0]);

        val playerBet = players.get(plugin.getUserRepository().findById(uuid));

        if (playerBet != null && playerBet.compareTo(maxBet) == 0) {
            messageDispatcher.dispatch(player, "YouAlreadyBetTheMost");
            return;
        }

        if (money.compareTo(playerBet) == 1) {
            messageDispatcher.dispatch(player, "ValueGreaterMax");
            return;
        }

        if (!economyAPI.has(uuid, money)) {
            messageDispatcher.dispatch(player, "YouDontHaveMoney");
            return;
        }

        val user = plugin.getUserRepository().findById(player.getUniqueId());

        if (playerBet != null) {
            val oldValue = players.get(user);
            players.replace(user, money.add(oldValue));
        } else {
            players.put(user, money);
        }

        economyAPI.withdraw(uuid, money);

        messageDispatcher.dispatch(player, "YouEntered", message -> message
                .replace("{chance}", String.valueOf(RandomUtils.getChance(players, user)))
                .replace("{money}", String.format("%.2f", players.get(user))));
    }

    @Override
    public void start() {
        players.clear();
        val config = configProvider.provide(ConfigType.CONFIG);
        this.maxBet = new BigDecimal(config.getString("MaxBet"));
        setGameState(GameState.STARTED);

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.runAsync(0L, interval * 20L, () -> {
            val seconds = calls.get() * interval;

            if (calls.get() > 0) {
                messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{size}", String.valueOf(players.size()))
                        .replace("{total}", String.valueOf(players.values().stream().reduce(BigDecimal.valueOf(0), BigDecimal::add)))
                        .replace("{maxBet}", String.valueOf(maxBet))
                        .replace("{seconds}", String.valueOf(seconds)));

                calls.getAndDecrement();
            } else {
                if (players.size() >= 2) {
                    val winner = RandomUtils.getWeightedRandom(players);
                    onWin(winner);
                } else {
                    messageDispatcher.globalDispatch("NoWinner");
                    stop();
                }

            }
        });
    }

    @Override
    public void stop() {
        setGameState(GameState.STOPPED);
        schedulerManager.cancel();

        val economyAPI = plugin.getVaultProvider().provide().get();
        players.forEach((user, value) -> economyAPI.deposit(user.getUuid(), value));
    }

    @Override
    public void onWin(User user) {
        giveReward(user);
        val total = players.values().stream().reduce(BigDecimal.valueOf(0), BigDecimal::add);
        players.clear();
        stop();

        messageDispatcher.globalDispatch("Win", message -> message
                .replace("{chance}", String.valueOf(RandomUtils.getChance(players, user)))
                .replace("{total}", String.valueOf(total))
                .replace("{winner}", user.getPlayerName()));
    }

    @Override
    protected void giveReward(User user) {
        val total = players.values().stream().reduce(BigDecimal.valueOf(0), BigDecimal::add);

        plugin.getVaultProvider().provide().get().deposit(user.getUuid(), total);
        this.reward.execute(user);
    }

    @Override
    public boolean isEconomyRequired() {
        return true;
    }

}
