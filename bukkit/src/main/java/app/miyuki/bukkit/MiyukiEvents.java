package app.miyuki.bukkit;

import app.miyuki.bukkit.commands.CommandRegistry;
import co.aikar.commands.PaperCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MiyukiEvents extends JavaPlugin {

    private CommandRegistry commandRegistry;

    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {


        commandManager = new PaperCommandManager(this);

        commandRegistry = CommandRegistry.of(commandManager);
        commandRegistry.register();
    }

    @Override
    public void onDisable() {

    }

}
