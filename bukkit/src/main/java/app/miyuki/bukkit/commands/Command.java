package app.miyuki.bukkit.commands;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.commands.evaluator.CommandEvaluator;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends org.bukkit.command.Command {

    protected final @NotNull MiyukiEvents plugin;

    protected final List<SubCommand> subcommands = new ArrayList<>();
    private final @NotNull boolean console;

    public Command(@NotNull MiyukiEvents plugin, @NotNull String name, @NotNull List<String> aliases, boolean console) {
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
                        subcommand.evaluate(sender, Arrays.copyOfRange(args, 1, args.length));
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

        String lastWord = args[args.length - 1];

        ArrayList<String> match = new ArrayList<>();
        for (SubCommand subCommand : subcommands) {
            String name = subCommand.getAliases().get(0);
            if (StringUtil.startsWithIgnoreCase(name, lastWord)) {
                match.add(name);
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
