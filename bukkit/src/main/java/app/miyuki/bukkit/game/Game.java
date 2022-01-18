package app.miyuki.bukkit.game;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.messages.MessageDispatcher;
import app.miyuki.bukkit.reward.Reward;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class Game<W> {

    @Getter
    private final @NotNull MiyukiEvents plugin;

    @Getter
    protected final @NotNull ConfigProvider configProvider;

    @Getter
    protected final @NotNull MessageDispatcher messageDispatcher;

    protected GameState gameState;

    protected Reward reward;

    public Game(@NotNull ConfigProvider configProvider) {
        this.plugin = JavaPlugin.getPlugin(MiyukiEvents.class);
        this.configProvider = configProvider;
        messageDispatcher = new MessageDispatcher(configProvider);
    }

    public abstract String getTypeName();

    public abstract String getName();

    public abstract String getPermission();

    public abstract void setGameState(GameState gameState);

    public abstract GameState getGameState();

    public abstract void start();

    public abstract void stop();

    public abstract void onWin(W w);

    public abstract void giveReward(W w);

}
