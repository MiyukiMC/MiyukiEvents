package app.miyuki.bukkit.commands;

import app.miyuki.bukkit.MiyukiEvents;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Command extends org.bukkit.command.Command {

    private final MiyukiEvents plugin;


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return execute(sender, args);
    }

    abstract boolean execute(@NotNull CommandSender sender, @NotNull String[] args);

    abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args);

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return onTabComplete(sender, args);
    }
}
