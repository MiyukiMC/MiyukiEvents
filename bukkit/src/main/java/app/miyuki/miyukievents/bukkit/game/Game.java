package app.miyuki.miyukievents.bukkit.game;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.manager.GameSchedulerManager;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.reward.Reward;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

@Getter
public abstract class Game<W> {

    protected final @NotNull MiyukiEvents plugin;

    protected final @NotNull MessageDispatcher messageDispatcher;

    protected final Config config;

    protected final Config messages;

    protected final Config data;

    @Setter
    protected GameState gameState = GameState.STOPPED;

    protected final Reward reward;

    @Nullable
    protected final String permission;

    protected BigDecimal cost;

    protected final GameSchedulerManager schedulerManager;

    public Game(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
        this.config = config;
        this.messages = messages;
        this.data = data;
        this.plugin = JavaPlugin.getPlugin(MiyukiEvents.class);

        val configRoot = config.getRoot();

        this.reward = plugin.getRewardAdapter().adapt(configRoot.node("Reward"));
        this.permission = config.getRoot().node("Command", "  Permission").getString();

        this.cost = new BigDecimal(config.getRoot().node("Cost").getString("0"));

        messageDispatcher = new MessageDispatcher(plugin, messages);
        schedulerManager = new GameSchedulerManager(plugin);
    }

    protected boolean checkCost(Player player) {
        val economyAPI = plugin.getVaultProvider().provide();
        return economyAPI.map(api -> api.has(player.getUniqueId(), cost)).orElse(true);

    }

    public String getName() {
        return config.getRoot().node("Name").getString();
    }

    public abstract void start();

    public abstract void stop();

    public abstract void onWin(W w);

    protected abstract void giveReward(W w);

    public abstract boolean isEconomyRequired();

}
