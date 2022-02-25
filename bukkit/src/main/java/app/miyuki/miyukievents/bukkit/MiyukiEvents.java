package app.miyuki.miyukievents.bukkit;

import app.miyuki.miyukievents.bukkit.adapter.impl.ItemSerialAdapter;
import app.miyuki.miyukievents.bukkit.adapter.impl.LocationAdapter;
import app.miyuki.miyukievents.bukkit.adapter.impl.RewardAdapter;
import app.miyuki.miyukievents.bukkit.commands.CommandRegistry;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.dependency.DependencyManager;
import app.miyuki.miyukievents.bukkit.game.manager.GameManager;
import app.miyuki.miyukievents.bukkit.game.queue.GameQueue;
import app.miyuki.miyukievents.bukkit.hook.cash.CashProvider;
import app.miyuki.miyukievents.bukkit.hook.chat.ChatHook;
import app.miyuki.miyukievents.bukkit.hook.clan.ClanProvider;
import app.miyuki.miyukievents.bukkit.hook.economy.EconomyProvider;
import app.miyuki.miyukievents.bukkit.hook.worldedit.WorldEditProvider;
import app.miyuki.miyukievents.bukkit.language.LanguageEvaluator;
import app.miyuki.miyukievents.bukkit.language.LanguageProvider;
import app.miyuki.miyukievents.bukkit.listener.ListenerRegistry;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.storage.Storage;
import app.miyuki.miyukievents.bukkit.storage.StorageFactory;
import app.miyuki.miyukievents.bukkit.user.UserRepository;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import lombok.Getter;
import lombok.val;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiyukiEvents extends JavaPlugin {

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
    private ItemSerialAdapter itemSerialAdapter;

    @Getter
    private GameQueue queue;

    @Getter
    private CommandRegistry commandRegistry;

    @Getter
    private ClanProvider clanProvider;

    @Getter
    private CashProvider cashProvider;

    @Getter
    private EconomyProvider vaultProvider;

    @Getter
    private WorldEditProvider worldEditProvider;

    @Getter
    private Storage storage;

    @Getter
    private UserRepository userRepository;

    @Getter
    private DependencyManager dependencyManager;

    @Override
    public void onEnable() {
        this.language = new LanguageEvaluator().evaluate(new LanguageProvider().provide());

        loadGlobalConfig();
        loadMessages();

        dependencyManager = new DependencyManager(this);

        dependencyManager.loadGlobalDependencies();

        loadAdapters();
        loadProviders();

        loadCommands();
        loadListeners();

        ChatHook.of(this).hook();

        loadGameManager();
        loadGameQueue();

        loadDatabase();
        loadMetrics();

        this.userRepository = new UserRepository(this);
    }

    @Override
    public void onDisable() {

        val currentGame = gameManager.getCurrentGame();
        if (currentGame != null)
            currentGame.stop();

        if (storage != null)
            storage.shutdown();

    }

    private void loadCommands() {
        this.commandRegistry = new CommandRegistry(this);
        LoggerHelper.log("Commands loaded successfully");
    }

    private void loadListeners() {
        ListenerRegistry.of(this).register();
        LoggerHelper.log("Listeners loaded successfully");
    }

    private void loadProviders() {
        this.vaultProvider = new EconomyProvider(this);
        if (vaultProvider.hook())
            LoggerHelper.log("Vault Provider loaded successfully");

        this.clanProvider = new ClanProvider(this);
        if (clanProvider.hook())
            LoggerHelper.log("Clan Provider loaded successfully");

        this.cashProvider = new CashProvider(this);
        if (cashProvider.hook())
            LoggerHelper.log("Cash Provider loaded successfully");

        this.worldEditProvider = new WorldEditProvider();
        if (worldEditProvider.hook())
            LoggerHelper.log("WorldEdit Provider loaded successfully");
    }

    private void loadGlobalConfig() {
        this.globalConfig = new Config("config.yml", language + "/config.yml");
    }

    private void loadMessages() {
        this.globalMessageDispatcher = new MessageDispatcher(new Config("messages.yml", language + "/messages.yml"));
        LoggerHelper.log("Messages loaded successfully");
    }

    private void loadDatabase() {
        val storageFactory = new StorageFactory(this);

        storage = storageFactory.create();

        if (storage == null)
            return;

        storage.createTables();
        LoggerHelper.log("Database loaded successfully");
    }

    private void loadGameManager() {
        this.gameManager = new GameManager(this, language);
        this.gameManager.load();
    }

    private void loadGameQueue() {
        this.queue = new GameQueue(this, gameManager);
        LoggerHelper.log("Queue loaded successfully");
    }

    private void loadAdapters() {
        this.rewardAdapter = new RewardAdapter();
        this.locationAdapter = new LocationAdapter();
        this.itemSerialAdapter = new ItemSerialAdapter();
    }

    private void loadMetrics() {
        if (globalConfig.getBoolean("Metrics")) {
            new Metrics(this, 14218);
            LoggerHelper.log("Metrics loaded successfully");
        }
    }

}
