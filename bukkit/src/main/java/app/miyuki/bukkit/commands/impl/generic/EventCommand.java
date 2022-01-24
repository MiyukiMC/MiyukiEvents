package app.miyuki.bukkit.commands.impl.generic;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.commands.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventCommand extends Command {

    public EventCommand(@NotNull MiyukiEvents plugin, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        
        return false;
    }


}
