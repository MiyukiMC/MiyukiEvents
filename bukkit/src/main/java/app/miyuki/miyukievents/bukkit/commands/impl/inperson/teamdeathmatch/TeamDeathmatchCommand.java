package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teamdeathmatch;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericHelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.inperson.InPersonCabinSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.inperson.InPersonSetLocationSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.inperson.teams.*;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.inperson.Teams;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeamDeathmatchCommand extends Command {

    private final Teams<?> game;
    private final MessageDispatcher messageDispatcher;

    public TeamDeathmatchCommand(@NotNull MiyukiEvents plugin, @NotNull Game<?> game, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        this.game = (Teams<?>) game;
        messageDispatcher = game.getMessageDispatcher();
        GameConfigProvider configProvider = game.getConfigProvider();

        registerSubCommand(
                new GenericHelpSubCommand(plugin, configProvider, messageDispatcher),
                new InPersonCabinSubCommand(plugin, this.game, configProvider, messageDispatcher),
                new TeamsInPersonSetEntrySubCommand(plugin, this.game, configProvider, messageDispatcher),
                new TeamsInPersonRemoveEntrySubCommand(plugin, this.game, configProvider, messageDispatcher),
                new TeamsInPersonStartSubCommand(plugin, this.game, configProvider, messageDispatcher)
        );

        for (InPersonSetLocationSubCommand.LocationType locationType : InPersonSetLocationSubCommand.LocationType.values()) {
            if (locationType == InPersonSetLocationSubCommand.LocationType.ENTRY)
                continue;
            registerSubCommand(new InPersonSetLocationSubCommand(plugin, this.game, configProvider, messageDispatcher, locationType));
        }

        if (this.game.isKitRequired()) {
            registerSubCommand(
                    new TeamsInPersonSetKitSubCommand(plugin, this.game, configProvider, messageDispatcher),
                    new TeamsInPersonGetKitSubCommand(plugin, this.game, configProvider, messageDispatcher)
            );
        }

        if (this.game.isWorldEditRequired()) {
            registerSubCommand();
        }

    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {



        Bukkit.dispatchCommand(sender, getName() + " help");

        return false;
    }

}
