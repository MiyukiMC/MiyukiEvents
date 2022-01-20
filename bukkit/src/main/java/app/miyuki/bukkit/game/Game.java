package app.miyuki.bukkit.game;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.config.ConfigType;
import app.miyuki.bukkit.messages.MessageDispatcher;
import app.miyuki.bukkit.reward.Reward;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class Game<W> {

    @Getter(value = AccessLevel.PACKAGE)
    private final @NotNull MiyukiEvents plugin;

    @Getter(value = AccessLevel.PROTECTED)
    protected final @NotNull ConfigProvider configProvider;

    @Getter(value = AccessLevel.PROTECTED)
    protected final @NotNull MessageDispatcher messageDispatcher;

    protected GameState gameState;

    @Getter
    protected Reward reward;

    public Game(@NotNull ConfigProvider configProvider) {
        this.plugin = JavaPlugin.getPlugin(MiyukiEvents.class);
        this.configProvider = configProvider;
        this.reward = plugin.getRewardAdapter().adapt(
                configProvider.provide(ConfigType.CONFIG).getConfigurationSection("Reward")
        );
        messageDispatcher = new MessageDispatcher(configProvider);
    }

    public abstract String getTypeName();

    public abstract String getName();

    public abstract String getPermission();

    protected abstract void setGameState(GameState gameState);

    public abstract GameState getGameState();

    protected abstract void start();

    protected abstract void stop();

    protected abstract void onWin(W w);

    protected abstract void giveReward(W w);

}
