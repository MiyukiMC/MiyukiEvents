package app.miyuki.bukkit;

import app.miyuki.bukkit.config.Config;
import app.miyuki.bukkit.game.manager.GameManager;
import app.miyuki.bukkit.language.LanguageEvaluator;
import app.miyuki.bukkit.language.LanguageProvider;
import app.miyuki.bukkit.listener.ListenerRegistry;
import lombok.Getter;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiyukiEvents extends JavaPlugin {

    @Getter
    private GameManager gameManager;

    @Getter
    private Config config;

    @Override
    public void onEnable() {

        val language = new LanguageEvaluator().evaluate(new LanguageProvider().provide());

        config = new Config("config.yml", language + "/config.yml");

        gameManager = new GameManager(this, config, language);

        ListenerRegistry.of(this).register();

    }

    @Override
    public void onDisable() {

    }

}
