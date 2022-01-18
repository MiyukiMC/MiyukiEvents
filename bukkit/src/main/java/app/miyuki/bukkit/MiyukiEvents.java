package app.miyuki.bukkit;

import app.miyuki.bukkit.adapter.impl.LocationAdapter;
import app.miyuki.bukkit.adapter.impl.RewardAdapter;
import app.miyuki.bukkit.config.Config;
import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.game.manager.GameManager;
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
    private MessageDispatcher globalMessageDispatcher;

    @Getter
    private ConfigProvider globalConfigProvider;

    @Getter
    private RewardAdapter rewardAdapter;

    @Getter
    private LocationAdapter locationAdapter;

    @Override
    public void onEnable() {

        val language = new LanguageEvaluator().evaluate(new LanguageProvider().provide());

        globalConfigProvider = new ConfigProvider("", language + "/config.yml");

        gameManager = new GameManager(this, globalConfigProvider, language);

        globalMessageDispatcher = new MessageDispatcher(globalConfigProvider);

        ListenerRegistry.of(this).register();

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
