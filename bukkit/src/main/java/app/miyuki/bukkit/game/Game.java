package app.miyuki.bukkit.game;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.messages.MessageDispatcher;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class Game<W> {

    private final @NotNull MiyukiEvents plugin;
    protected final @NotNull ConfigProvider configProvider;
    protected final @NotNull MessageDispatcher messageDispatcher;

    public Game(@NotNull ConfigProvider configProvider) {
        this.plugin = JavaPlugin.getPlugin(MiyukiEvents.class);
        this.configProvider = configProvider;
        messageDispatcher = new MessageDispatcher(configProvider);
    }

    public abstract String getTypeName();

    public abstract String getName();

    public abstract GameState getState();

    public abstract void start();

    public abstract void stop();

    public abstract void onWin(W w);

    public abstract void giveReward(W w);

}
