package app.miyuki.bukkit;

import app.miyuki.bukkit.adapter.impl.LocationAdapter;
import app.miyuki.bukkit.adapter.impl.RewardAdapter;
import app.miyuki.bukkit.commands.CommandRegistry;
import app.miyuki.bukkit.config.Config;
import app.miyuki.bukkit.config.GameConfigProvider;
import app.miyuki.bukkit.game.manager.GameManager;
import app.miyuki.bukkit.game.queue.GameQueue;
import app.miyuki.bukkit.language.LanguageEvaluator;
import app.miyuki.bukkit.language.LanguageProvider;
import app.miyuki.bukkit.listener.ListenerRegistry;
import app.miyuki.bukkit.messages.MessageDispatcher;
import lombok.Getter;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiyukiEvents extends JavaPlugin {

    @Getter
    private GameManager gameManager;

    @Getter
    private Config config;

    @Getter
    private MessageDispatcher globalMessageDispatcher;

    @Getter
    private RewardAdapter rewardAdapter;

    @Getter
    private LocationAdapter locationAdapter;

    @Getter
    private GameQueue queue;

    @Override
    public void onEnable() {

        val language = new LanguageEvaluator().evaluate(new LanguageProvider().provide());

        config = new Config("config.yml", language + "/config.yml");

        gameManager = new GameManager(this, config, language);

        globalMessageDispatcher = new MessageDispatcher(new Config("messages.yml", language + "/messages.yml"));

        ListenerRegistry.of(this).register();

        CommandRegistry.of(this).register();


        queue = new GameQueue(this, gameManager);

        loadAdapters();
    }

    @Override
    public void onDisable() {

    }

    private void loadAdapters() {
        this.rewardAdapter = new RewardAdapter(this);
        this.locationAdapter = new LocationAdapter(this);
    }

}
