package app.miyuki.miyukievents.bukkit.commands;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.evaluator.CommandEvaluator;
import com.google.common.collect.ImmutableList;
import lombok.val;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GameCommand extends Command {

    protected final @NotNull MiyukiEvents plugin;

    protected final List<SubCommand> subcommands = new ArrayList<>();
    private final boolean console;

    public GameCommand(@NotNull MiyukiEvents plugin, @NotNull String name, @NotNull List<String> aliases, boolean console) {
        super(name);
        this.console = console;
        setAliases(aliases);
        this.plugin = plugin;
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if (!CommandEvaluator.of(plugin).evaluate(sender, getPermission(), console))
            return false;

        if (args.length > 0) {

            for (SubCommand subcommand : subcommands) {

                for (String alias : subcommand.getAliases()) {
                    if (args[0].equalsIgnoreCase(alias))
                        return subcommand.evaluate(sender, Arrays.copyOfRange(args, 1, args.length));
                }

            }

        }

        return execute(sender, args);
    }

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String[] args);

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {

        if (args.length == 0) {
            return ImmutableList.of();
        }

        val subcommandName = args[0];

        ArrayList<String> match = new ArrayList<>();
        for (SubCommand subCommand : subcommands) {

            val aliases = subCommand.getAliases();

            if (aliases.isEmpty())
                return ImmutableList.of();

            val name = aliases.get(0);

            if (StringUtil.startsWithIgnoreCase(name, subcommandName)) {

                val permission = subCommand.getPermission();

                if (permission == null || !sender.hasPermission(permission))
                    continue;

                if (args.length == 1) {
                    match.add(name);
                } else {
                    match.addAll(subCommand.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length)));
                }

            }
        }

        match.sort(String.CASE_INSENSITIVE_ORDER);
        return match;
    }

    public void registerSubCommand(SubCommand... subCommand) {
        subcommands.addAll(Arrays.asList(subCommand));
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return onTabComplete(sender, args);
    }
}
