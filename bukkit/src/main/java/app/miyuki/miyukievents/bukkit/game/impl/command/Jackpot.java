package app.miyuki.miyukievents.bukkit.game.impl.command;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Command;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.util.number.NumberEvaluator;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Jackpot extends Command<Player> {

    private final Economy economy = plugin.getVaultProvider().provide().get();

    @Getter
    private final Map<String, Double> players = Maps.newHashMap();

    private double maxBet;

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

        val money = Double.parseDouble(args[0]);

        val playerBet = players.get(player.getName());

        if (playerBet != null && playerBet == maxBet) {
            messageDispatcher.dispatch(player, "YouAlreadyBetTheMost");
            return;
        }

        if (money > maxBet) {
            messageDispatcher.dispatch(player, "ValueGreaterMax");
            return;
        }

        if (economy.getBalance(player.getName()) < money) {
            messageDispatcher.dispatch(player, "YouDontHaveMoney");
            return;
        }


        if (playerBet != null) {
            val oldValue = players.get(player.getName());
            players.replace(player.getName(), money + oldValue);
        } else {
            players.put(player.getName(), money);
        }

        economy.withdrawPlayer(player, money);

        messageDispatcher.dispatch(player, "YouEntered", message -> message
                .replace("{chance}", String.valueOf(RandomUtils.getChance(players, player.getName())))
                .replace("{money}", String.format("%.2f", players.get(player.getName()))));
    }

    @Override
    public void start() {
        setGameState(GameState.STARTED);
        val config = configProvider.provide(ConfigType.CONFIG);

        this.maxBet = config.getInt("MaxBet");

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.runAsync(0L, interval * 20L, () -> {
            val seconds = calls.get() * interval;

            if (calls.get() > 0) {
                messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{size}", String.valueOf(players.size()))
                        .replace("{total}", String.valueOf(players.values().stream().mapToDouble(v -> v).sum()))
                        .replace("{maxBet}", String.valueOf(maxBet))
                        .replace("{seconds}", String.valueOf(seconds)));

                calls.getAndDecrement();
            } else {
                if (players.size() >= 2) {
                    val winner = Bukkit.getPlayer(RandomUtils.getWeightedRandom(players));
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
    }

    @Override
    public void onWin(Player player) {
        stop();

        giveReward(player);

        val total = players.values().stream().mapToDouble(value -> value).sum();

        messageDispatcher.globalDispatch("Win", message -> message
                .replace("{chance}", String.valueOf(RandomUtils.getChance(players, player.getName())))
                .replace("{total}", String.valueOf(total))
                .replace("{winner}", player.getName()));
    }

    @Override
    protected void giveReward(Player player) {
        val total = players.values().stream().mapToDouble(value -> value).sum();

        economy.depositPlayer(player, total);
        this.reward.execute(player);
    }

    @Override
    public boolean isEconomyRequired() {
        return true;
    }

}
