package app.miyuki.bukkit.commands.impl;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.commands.Command;
import app.miyuki.bukkit.commands.SubCommand;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventCommand extends Command {

    private static ImmutableList<SubCommand> SUBCOMMANDS = ImmutableList.of(

    );

    public EventCommand(@NotNull MiyukiEvents plugin, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {

        if (args.length == 0)
            return ImmutableList.of();

        String lastWord = args[args.length - 1];
        ArrayList<String> matches = Lists.newArrayList();

        if (args.length == 1) {

            for (SubCommand subcommand : SUBCOMMANDS) {
                val commandName = subcommand.getAliases().get(0);

                if (StringUtils.startsWithIgnoreCase(commandName, lastWord)) {
                    matches.add(commandName);
                }

            }
            matches.sort(String.CASE_INSENSITIVE_ORDER);

        }

        return matches;
    }

}
