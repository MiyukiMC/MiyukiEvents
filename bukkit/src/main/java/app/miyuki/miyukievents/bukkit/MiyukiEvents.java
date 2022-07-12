package app.miyuki.miyukievents.bukkit;

import app.miyuki.miyukievents.bukkit.adapter.impl.*;
import app.miyuki.miyukievents.bukkit.commands.CommandRegistry;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.dependency.DependencyManager;
import app.miyuki.miyukievents.bukkit.game.manager.GameManager;
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
import app.miyuki.miyukievents.bukkit.user.UserGameHistory;
import app.miyuki.miyukievents.bukkit.user.UserRepository;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import lombok.Getter;
import lombok.val;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class MiyukiEvents extends JavaPlugin {

    private String language;

    private BukkitAudiences adventure;

    private GameManager gameManager;

    private Config globalConfig;

    private MessageDispatcher globalMessageDispatcher;

    private RewardAdapter rewardAdapter;

    private LocationAdapter locationAdapter;

    private ItemSerialAdapter itemSerialAdapter;

    private TextColorAdapter textColorAdapter;

    private UserGameHistoryAdapter userGameHistoryAdapter;

    private CommandRegistry commandRegistry;

    private ClanProvider clanProvider;

    private CashProvider cashProvider;

    private EconomyProvider vaultProvider;

    private WorldEditProvider worldEditProvider;

    private Storage storage;

    private UserRepository userRepository;

    private DependencyManager dependencyManager;

    @Override
    public void onEnable() {
        this.language = new LanguageEvaluator().evaluate(new LanguageProvider().provide());

        loadGlobalConfig();

        dependencyManager = new DependencyManager(this);
        dependencyManager.loadGlobalDependencies();

        adventure = BukkitAudiences.create(this);

        this.loadMessages();

        this.loadAdapters();
        this.loadProviders();

        this.loadCommands();
        this.loadListeners();

        ChatHook.of(this).hook();

        this.loadGameManager();

        this.loadDatabase();
        this.loadMetrics();

        this.userRepository = new UserRepository(this);
    }

    @Override
    public void onDisable() {
        val currentGame = this.gameManager.getCurrentGame();
        if (currentGame != null)
            currentGame.stop();

        this.adventure.close();

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
        this.globalMessageDispatcher = new MessageDispatcher(this, new Config("messages.yml", language + "/messages.yml"));
        LoggerHelper.log("Messages loaded successfully");
    }

    private void loadDatabase() {
        val storageFactory = new StorageFactory(this);
        this.storage = storageFactory.create();

        if (storage == null)
            return;

        this.storage.createTables();

        LoggerHelper.log("Database loaded successfully");
    }

    private void loadGameManager() {
        this.gameManager = new GameManager(this, language);
        this.gameManager.load();
    }

    private void loadAdapters() {
        this.rewardAdapter = new RewardAdapter();
        this.locationAdapter = new LocationAdapter();
        this.itemSerialAdapter = new ItemSerialAdapter();
        this.textColorAdapter = new TextColorAdapter();
        this.userGameHistoryAdapter = new UserGameHistoryAdapter();
    }

    private void loadMetrics() {
        if (globalConfig.getBoolean("Metrics")) {
            new Metrics(this, 14218);
            LoggerHelper.log("Metrics loaded successfully");
        }
    }

}
