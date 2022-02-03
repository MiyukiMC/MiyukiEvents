package app.miyuki.miyukievents.bukkit.game;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.reward.Reward;
import lombok.Getter;
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

    public Game(@NotNull GameConfigProvider configProvider) {
        this.plugin = JavaPlugin.getPlugin(MiyukiEvents.class);
        this.configProvider = configProvider;
        this.reward = plugin.getRewardAdapter().adapt(
                configProvider.provide(ConfigType.CONFIG).getConfigurationSection("Reward")
        );
        messageDispatcher = new MessageDispatcher(configProvider);
        gameState = GameState.STOPPED;
    }

    public abstract String getTypeName();

    public abstract String getName();

    public abstract void setGameState(GameState gameState);

    public abstract void start();

    public abstract void stop();

    public abstract void onWin(W w);

    protected abstract void giveReward(W w);

}
