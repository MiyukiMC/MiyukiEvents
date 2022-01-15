package app.miyuki.bukkit;

import app.miyuki.bukkit.commands.CommandRegistry;
import app.miyuki.bukkit.config.Config;
import co.aikar.commands.PaperCommandManager;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiyukiEvents extends JavaPlugin {

    private CommandRegistry commandRegistry;

    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {

//        commandManager = new PaperCommandManager(this);
//
//        commandRegistry = CommandRegistry.of(commandManager);
//        commandRegistry.register();

        val config = new Config("lang.yml");
        val config2 = new Config("config.yml", "pt-br/config.yml");

        config.saveDefaultConfig();
        config.reloadConfig();

        config2.saveDefaultConfig();
        config2.reloadConfig();

        System.out.println("Lang: " + config.getConfig().getString("Language"));
        System.out.println("Teste: " + config2.getConfig().getString("teste2"));

    }

    @Override
    public void onDisable() {

    }

}
