package app.miyuki.bukkit.commands.impl.chat;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.commands.Command;
import app.miyuki.bukkit.commands.impl.generic.StartSubCommand;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.messages.MessageDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WordCommand extends Command {

    private final Game game;
    private final MessageDispatcher messageDispatcher;

    public WordCommand(@NotNull MiyukiEvents plugin, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        game = plugin.getGameManager().getGames().get(name);
        messageDispatcher = game.getMessageDispatcher();

        registerSubCommand(
                new StartSubCommand(
                    plugin, game, game.getConfigProvider()
                )
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        Bukkit.dispatchCommand(sender, getName() + " help");

        return false;
    }

}
