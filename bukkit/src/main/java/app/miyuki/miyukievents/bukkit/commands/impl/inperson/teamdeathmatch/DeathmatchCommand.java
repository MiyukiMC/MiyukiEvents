package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teamdeathmatch;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericHelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericReloadSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStopSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.inperson.GenericSetKitSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.inperson.GenericSetLocationSubCommand;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeathmatchCommand extends Command {

    private final Game game;
    private final MessageDispatcher messageDispatcher;

    public DeathmatchCommand(@NotNull MiyukiEvents plugin, @NotNull Game game, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        this.game = game;
        messageDispatcher = game.getMessageDispatcher();
        GameConfigProvider configProvider = game.getConfigProvider();

        registerSubCommand(
                new GenericSetKitSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericStopSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericHelpSubCommand(plugin, configProvider, messageDispatcher),
                new GenericSetLocationSubCommand(plugin,game, configProvider, messageDispatcher, GenericSetLocationSubCommand.LocationType.CABIN),
                new GenericSetLocationSubCommand(plugin,game, configProvider, messageDispatcher, GenericSetLocationSubCommand.LocationType.ENTRY),
                new GenericSetLocationSubCommand(plugin,game, configProvider, messageDispatcher, GenericSetLocationSubCommand.LocationType.EXIT),
                new GenericSetLocationSubCommand(plugin,game, configProvider, messageDispatcher, GenericSetLocationSubCommand.LocationType.LOBBY),
                new GenericReloadSubCommand(plugin, game, configProvider)
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {


        return false;
    }

}
