package app.miyuki.bukkit.commands;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.config.ConfigProvider;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class SubCommand {

    private final @NotNull MiyukiEvents plugin;
    private final @NotNull ConfigProvider configProvider;

    public SubCommand(@NotNull MiyukiEvents plugin, @NotNull ConfigProvider configProvider) {
        this.plugin = plugin;
        this.configProvider = configProvider;
    }

    public abstract List<String> getAliases();

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String[] args);

}
