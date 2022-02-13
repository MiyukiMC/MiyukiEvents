package app.miyuki.miyukievents.bukkit.game;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.manager.GameSchedulerManager;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.reward.Reward;
import app.miyuki.miyukievents.bukkit.util.number.NumberEvaluator;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Game<W> {

    protected final @NotNull MiyukiEvents plugin;

    @Getter
    protected final @NotNull GameConfigProvider configProvider;

    @Getter
    protected final @NotNull MessageDispatcher messageDispatcher;

    @Getter
    @Setter
    protected GameState gameState = GameState.STOPPED;

    @Getter
    protected final Reward reward;

    @Getter
    protected final String permission;

    @Getter
    protected double cost = 0.0;

    @Getter
    protected final GameSchedulerManager schedulerManager;

    public Game(@NotNull GameConfigProvider configProvider) {
        this.plugin = JavaPlugin.getPlugin(MiyukiEvents.class);
        this.configProvider = configProvider;
        this.reward = plugin.getRewardAdapter().adapt(
                Objects.requireNonNull(configProvider.provide(ConfigType.CONFIG).getConfigurationSection("Reward"))
        );
        this.permission = configProvider.provide(ConfigType.CONFIG).getString("Permission");

        val cost = configProvider.provide(ConfigType.CONFIG).getDouble("Cost");

        if (NumberEvaluator.isValid(cost))
            this.cost = cost;

        messageDispatcher = new MessageDispatcher(configProvider);
        schedulerManager = new GameSchedulerManager(plugin);
    }

    protected boolean checkCost(Player player) {
        return plugin.getVaultProvider().provide().map(value -> value.getBalance(player) >= getCost()).orElse(true);
    }

    public String getTypeName() {
        return configProvider.provide(ConfigType.CONFIG).getString("Type");
    }

    public String getName() {
        return configProvider.provide(ConfigType.CONFIG).getString("Name");
    }

    public abstract void start();

    public abstract void stop();

    public abstract void onWin(W w);

    protected abstract void giveReward(W w);

    public abstract boolean isEconomyRequired();

}
