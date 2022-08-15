package app.miyuki.miyukievents.bukkit;

import app.miyuki.miyukievents.bukkit.adapter.impl.ItemSerialAdapter;
import app.miyuki.miyukievents.bukkit.adapter.impl.LocationAdapter;
import app.miyuki.miyukievents.bukkit.adapter.impl.RewardAdapter;
import app.miyuki.miyukievents.bukkit.adapter.impl.UserGameHistoryAdapter;
import app.miyuki.miyukievents.bukkit.commands.CommandRegistry;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.manager.GameManager;
import app.miyuki.miyukievents.bukkit.hook.cash.CashProvider;
import app.miyuki.miyukievents.bukkit.hook.chat.ChatHook;
import app.miyuki.miyukievents.bukkit.hook.clan.ClanProvider;
import app.miyuki.miyukievents.bukkit.hook.economy.EconomyProvider;
import app.miyuki.miyukievents.bukkit.hook.worldedit.WorldEditProvider;
import app.miyuki.miyukievents.bukkit.messages.language.LanguageEvaluator;
import app.miyuki.miyukievents.bukkit.messages.language.LanguageProvider;
import app.miyuki.miyukievents.bukkit.listener.ListenerRegistry;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.storage.Storage;
import app.miyuki.miyukievents.bukkit.storage.StorageFactory;
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

    private UserGameHistoryAdapter userGameHistoryAdapter;

    private CommandRegistry commandRegistry;

    private ClanProvider clanProvider;

    private CashProvider cashProvider;

    private EconomyProvider vaultProvider;

    private WorldEditProvider worldEditProvider;

    private Storage storage;

    private UserRepository userRepository;


    @Override
    public void onEnable() {
        this.language = new LanguageEvaluator().evaluate(new LanguageProvider(this).provide());

        loadGlobalConfig();

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
        if (this.gameManager != null && this.gameManager.getCurrentGame() != null)
            this.gameManager.getCurrentGame().stop();

        this.adventure.close();

        if (this.storage != null)
            this.storage.shutdown();
    }

    private void loadCommands() {
        this.commandRegistry = new CommandRegistry(this);
        LoggerHelper.console("Commands loaded successfully");
    }

    private void loadListeners() {
        ListenerRegistry.of(this).register();
        LoggerHelper.console("Listeners loaded successfully");
    }

    private void loadProviders() {
        this.vaultProvider = new EconomyProvider(this);
        if (vaultProvider.hook())
            LoggerHelper.console("Vault Provider loaded successfully");

        this.clanProvider = new ClanProvider(this);
        if (clanProvider.hook())
            LoggerHelper.console("Clan Provider loaded successfully");

        this.cashProvider = new CashProvider(this);
        if (cashProvider.hook())
            LoggerHelper.console("Cash Provider loaded successfully");

        this.worldEditProvider = new WorldEditProvider();
        if (worldEditProvider.hook())
            LoggerHelper.console("WorldEdit Provider loaded successfully");
    }

    private void loadGlobalConfig() {
        this.globalConfig = new Config(getDataFolder() + "/config.yml", language + "/config.yml");
    }

    private void loadMessages() {
        this.globalMessageDispatcher = new MessageDispatcher(this, new Config(getDataFolder() + "/messages.yml", language + "/messages.yml"));
        LoggerHelper.console("Messages loaded successfully");
    }

    private void loadDatabase() {
        val storageFactory = new StorageFactory(this);
        this.storage = storageFactory.create();

        if (storage == null)
            return;

        this.storage.createTables();

        LoggerHelper.console("Database loaded successfully");
    }

    private void loadGameManager() {
        this.gameManager = new GameManager(this, language);
        this.gameManager.load();
        LoggerHelper.console("Games loaded successfully");
    }

    private void loadAdapters() {
        this.rewardAdapter = new RewardAdapter();
        this.locationAdapter = new LocationAdapter();
        this.itemSerialAdapter = new ItemSerialAdapter();
        this.userGameHistoryAdapter = new UserGameHistoryAdapter();
    }

    private void loadMetrics() {
        if (globalConfig.getRoot().node("Metrics").getBoolean()) {
            new Metrics(this, 14218);
            LoggerHelper.console("Metrics loaded successfully");
        }
    }

}
