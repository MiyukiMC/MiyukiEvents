package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teamdeathmatch;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericHelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericReloadSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStopSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.inperson.InPersonSetKitSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.inperson.InPersonSetLocationSubCommand;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import org.bukkit.command.CommandSender;
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
                new InPersonSetKitSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericStopSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericHelpSubCommand(plugin, configProvider, messageDispatcher),
                new InPersonSetLocationSubCommand(plugin,game, configProvider, messageDispatcher, InPersonSetLocationSubCommand.LocationType.CABIN),
                new InPersonSetLocationSubCommand(plugin,game, configProvider, messageDispatcher, InPersonSetLocationSubCommand.LocationType.ENTRY),
                new InPersonSetLocationSubCommand(plugin,game, configProvider, messageDispatcher, InPersonSetLocationSubCommand.LocationType.EXIT),
                new InPersonSetLocationSubCommand(plugin,game, configProvider, messageDispatcher, InPersonSetLocationSubCommand.LocationType.LOBBY),
                new GenericReloadSubCommand(plugin, game, configProvider)
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {


        return false;
    }

}
