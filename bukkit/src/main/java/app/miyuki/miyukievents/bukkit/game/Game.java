package app.miyuki.miyukievents.bukkit.game;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.reward.Reward;
import app.miyuki.miyukievents.bukkit.util.number.NumberEvaluator;
import lombok.Getter;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class Game<W> {

    @Getter
    protected final @NotNull MiyukiEvents plugin;

    @Getter
    protected final @NotNull GameConfigProvider configProvider;

    @Getter
    protected final @NotNull MessageDispatcher messageDispatcher;

    @Getter
    protected GameState gameState;

    @Getter
    protected Reward reward;

    @Getter
    protected String permission;

    @Getter
    protected double cost = 0.0;

    public Game(@NotNull GameConfigProvider configProvider) {
        this.plugin = JavaPlugin.getPlugin(MiyukiEvents.class);
        this.configProvider = configProvider;
        this.reward = plugin.getRewardAdapter().adapt(
                configProvider.provide(ConfigType.CONFIG).getConfigurationSection("Reward")
        );
        this.permission = configProvider.provide(ConfigType.CONFIG).getString("Permission");

        val cost = configProvider.provide(ConfigType.CONFIG).getDouble("Cost");

        if (NumberEvaluator.isValid(cost))
            this.cost = cost;

        messageDispatcher = new MessageDispatcher(configProvider);
        gameState = GameState.STOPPED;
    }

    protected boolean checkCost(Player player) {
        val economy = plugin.getVaultProvider().provide();

        if (economy == null)
            return true;

        return economy.getBalance(player) >= getCost();
    }

    public String getTypeName() {
        return configProvider.provide(ConfigType.CONFIG).getString("Type");
    }

    public String getName() {
        return configProvider.provide(ConfigType.CONFIG).getString("Name");
    }

    public abstract void setGameState(GameState gameState);

    public abstract void start();

    public abstract void stop();

    public abstract void onWin(W w);

    protected abstract void giveReward(W w);

}
