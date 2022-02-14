package app.miyuki.miyukievents.bukkit;

import app.miyuki.miyukievents.bukkit.adapter.impl.ItemSerialAdapter;
import app.miyuki.miyukievents.bukkit.adapter.impl.LocationAdapter;
import app.miyuki.miyukievents.bukkit.adapter.impl.RewardAdapter;
import app.miyuki.miyukievents.bukkit.commands.CommandRegistry;
import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.manager.GameManager;
import app.miyuki.miyukievents.bukkit.game.queue.GameQueue;
import app.miyuki.miyukievents.bukkit.hook.cash.CashProvider;
import app.miyuki.miyukievents.bukkit.hook.chat.ChatHook;
import app.miyuki.miyukievents.bukkit.hook.clan.ClanProvider;
import app.miyuki.miyukievents.bukkit.hook.vault.VaultProvider;
import app.miyuki.miyukievents.bukkit.hook.worldedit.WorldEditProvider;
import app.miyuki.miyukievents.bukkit.language.LanguageEvaluator;
import app.miyuki.miyukievents.bukkit.language.LanguageProvider;
import app.miyuki.miyukievents.bukkit.listener.ListenerRegistry;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.util.logger.Logger;
import lombok.Getter;
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
    private VaultProvider vaultProvider;

    @Getter
    private WorldEditProvider worldEditProvider;

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

        ChatHook.of(this).hook();

        loadGameManager();
        loadGameQueue();

        loadMetrics();
    }

    @Override
    public void onDisable() {

    }

    private void loadCommands() {
        this.commandRegistry = new CommandRegistry(this);
        Logger.log("Commands loaded successfully");
    }

    private void loadListeners() {
        ListenerRegistry.of(this).register();
        Logger.log("Listeners loaded successfully");
    }

    private void loadProviders() {
        this.vaultProvider = new VaultProvider();
        if (vaultProvider.hook())
            Logger.log("Vault Provider loaded successfully");

        this.clanProvider = new ClanProvider(this);
        if (clanProvider.hook())
            Logger.log("Clan Provider loaded successfully");

        this.cashProvider = new CashProvider(this);
        if (cashProvider.hook())
            Logger.log("Cash Provider loaded successfully");

        this.worldEditProvider = new WorldEditProvider();
        if (worldEditProvider.hook())
            Logger.log("WorldEdit Provider loaded successfully");
    }

    private void loadGlobalConfig() {
        this.globalConfig = new Config("config.yml", language + "/config.yml");
    }

    private void loadMessages() {
        this.globalMessageDispatcher = new MessageDispatcher(new Config("messages.yml", language + "/messages.yml"));
        Logger.log("Messages loaded successfully");
    }

    private void loadDatabase() {
        //
        Logger.log("Database loaded successfully");
    }

    private void loadGameManager() {
        this.gameManager = new GameManager(this, language);
        this.gameManager.load();
    }

    private void loadGameQueue() {
        this.queue = new GameQueue(this, gameManager);
        Logger.log("Queue loaded successfully");
    }

    private void loadAdapters() {
        this.rewardAdapter = new RewardAdapter();
        this.locationAdapter = new LocationAdapter();
        this.itemSerialAdapter = new ItemSerialAdapter();
    }

    private void loadMetrics() {
        if (globalConfig.getBoolean("Metrics")) {
            new Metrics(this, 14218);
            Logger.log("Metrics loaded successfully");
        }
    }

}
