package app.miyuki.bukkit.commands;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.commands.evaluator.CommandEvaluator;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SubCommand {

    protected final @NotNull MiyukiEvents plugin;
    private final boolean console;

    public SubCommand(@NotNull MiyukiEvents plugin, boolean console) {
        this.plugin = plugin;
        this.console = console;
    }

    public abstract List<String> getAliases();

    public abstract @Nullable String getPermission();

    public boolean evaluate(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!CommandEvaluator.of(plugin).evaluate(sender, getPermission(), console))
            return false;

        return execute(sender, args);
    }


    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String[] args);

}
