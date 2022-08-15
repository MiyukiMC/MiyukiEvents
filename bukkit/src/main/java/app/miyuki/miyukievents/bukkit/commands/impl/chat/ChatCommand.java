package app.miyuki.miyukievents.bukkit.commands.impl.chat;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.GameCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericHelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericReloadSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStartSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStopSubCommand;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.chat.Chat;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatCommand extends GameCommand {

    private final Game<?> game;
    private final MessageDispatcher messageDispatcher;

    public ChatCommand(@NotNull MiyukiEvents plugin, @NotNull Game<?> game, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        this.game = game;
        messageDispatcher = game.getMessageDispatcher();

        registerSubCommand(
                new GenericStartSubCommand(plugin, game),
                new GenericStopSubCommand(plugin, game, messageDispatcher),
                new GenericHelpSubCommand(plugin, game, messageDispatcher),
                new GenericReloadSubCommand(plugin, game)
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        Bukkit.dispatchCommand(sender, getName() + " help");
        return false;
    }

}
