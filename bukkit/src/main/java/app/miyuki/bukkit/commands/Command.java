package app.miyuki.bukkit.commands;

import app.miyuki.bukkit.MiyukiEvents;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Command extends org.bukkit.command.Command {

    private final @NotNull MiyukiEvents plugin;

    public Command(@NotNull MiyukiEvents plugin, @NotNull String name, @NotNull List<String> aliases) {
        super(name);
        setAliases(aliases);
        this.plugin = plugin;
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return execute(sender, args);
    }

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String[] args);

    public abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args);

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return onTabComplete(sender, args);
    }
}
