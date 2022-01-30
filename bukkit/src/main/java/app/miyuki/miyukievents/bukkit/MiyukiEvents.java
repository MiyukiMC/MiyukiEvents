package app.miyuki.miyukievents.bukkit;

import app.miyuki.miyukievents.bukkit.adapter.impl.LocationAdapter;
import app.miyuki.miyukievents.bukkit.adapter.impl.RewardAdapter;
import app.miyuki.miyukievents.bukkit.commands.CommandRegistry;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.manager.GameManager;
import app.miyuki.miyukievents.bukkit.game.queue.GameQueue;
import app.miyuki.miyukievents.bukkit.language.LanguageEvaluator;
import app.miyuki.miyukievents.bukkit.language.LanguageProvider;
import app.miyuki.miyukievents.bukkit.listener.ListenerRegistry;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.Getter;
import lombok.val;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiyukiEvents extends JavaPlugin {

    @Getter
    private GameManager gameManager;

    @Getter
    private Config globalConfig;

    @Getter
    private MessageDispatcher globalMessageDispatcher;

    @Getter
    private RewardAdapter rewardAdapter;

    @Getter
    private LocationAdapter locationAdapter;

    @Getter
    private GameQueue queue;

    @Getter
    private CommandRegistry commandRegistry;

    @Override
    public void onEnable() {

        val language = new LanguageEvaluator().evaluate(new LanguageProvider().provide());

        loadAdapters();

        globalConfig = new Config("config.yml", language + "/config.yml");

        commandRegistry = new CommandRegistry(this);
        ListenerRegistry.of(this).register();

        gameManager = new GameManager(this, globalConfig, language);
        gameManager.load();

        globalMessageDispatcher = new MessageDispatcher(new Config("messages.yml", language + "/messages.yml"));


        queue = new GameQueue(this, gameManager);

        loadMetrics();
    }

    @Override
    public void onDisable() {

    }

    private void loadAdapters() {
        this.rewardAdapter = new RewardAdapter(this);
        this.locationAdapter = new LocationAdapter(this);
    }

    private void loadMetrics() {
        if (globalConfig.getBoolean("Metrics")) {
            new Metrics(this, 14120);
        }
    }

}
