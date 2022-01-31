package app.miyuki.miyukievents.bukkit;

import app.miyuki.miyukievents.bukkit.adapter.impl.LocationAdapter;
import app.miyuki.miyukievents.bukkit.adapter.impl.RewardAdapter;
import app.miyuki.miyukievents.bukkit.commands.CommandRegistry;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.manager.GameManager;
import app.miyuki.miyukievents.bukkit.game.queue.GameQueue;
import app.miyuki.miyukievents.bukkit.hook.clan.ClanProvider;
import app.miyuki.miyukievents.bukkit.language.LanguageEvaluator;
import app.miyuki.miyukievents.bukkit.language.LanguageProvider;
import app.miyuki.miyukievents.bukkit.listener.ListenerRegistry;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class MiyukiEvents extends JavaPlugin {

    private static final String PREFIX = ChatUtils.colorize("[MiyukiEvents] ");

    private String language;

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

    @Getter
    private ClanProvider clanProvider;

    @Override
    public void onEnable() {
        this.language = new LanguageEvaluator().evaluate(new LanguageProvider().provide());

        loadGlobalConfig();
        loadMessages();

        loadAdapters();
        loadProviders();

        loadDatabase();

        loadCommands();
        loadListeners();

        loadGameManager();
        loadGameQueue();

        loadMetrics();
    }

    @Override
    public void onDisable() {

    }

    private void loadCommands() {
        this.commandRegistry = new CommandRegistry(this);
        getLogger().log(Level.FINE, PREFIX + "Commands loaded successfully");
    }

    private void loadListeners() {
        ListenerRegistry.of(this).register();
        getLogger().log(Level.FINE, PREFIX + "Listeners loaded successfully");
    }

    private void loadProviders() {
        this.clanProvider = new ClanProvider(this);
    }

    private void loadGlobalConfig() {
        this.globalConfig = new Config("config.yml", language + "/config.yml");
    }

    private void loadMessages() {
        this.globalMessageDispatcher = new MessageDispatcher(new Config("messages.yml", language + "/messages.yml"));
        getLogger().log(Level.FINE, PREFIX + "Messages loaded successfully");
    }

    private void loadDatabase() {
        //
        getLogger().log(Level.FINE, "Database loaded successfully");
    }

    private void loadGameManager() {
        this.gameManager = new GameManager(this, globalConfig, language);
        this.gameManager.load();
    }

    private void loadGameQueue() {
        this.queue = new GameQueue(this, gameManager);
        getLogger().log(Level.FINE, "Game Queue loaded successfully");
    }

    private void loadAdapters() {
        this.rewardAdapter = new RewardAdapter(this);
        this.locationAdapter = new LocationAdapter(this);
    }

    private void loadMetrics() {
        if (globalConfig.getBoolean("Metrics")) {
            new Metrics(this, 14120);
            getLogger().log(Level.FINE, "Metrics loaded successfully");
        }
    }

}
