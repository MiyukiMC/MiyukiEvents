package app.miyuki.miyukievents.bukkit.commands.impl.command.auction;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.GameCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericHelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericReloadSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStopSubCommand;
import app.miyuki.miyukievents.bukkit.game.Game;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AuctionCommand extends GameCommand {

    private final Game<?> game;

    public AuctionCommand(@NotNull MiyukiEvents plugin, @NotNull Game<?> game, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        this.game = game;

        val messageDispatcher = game.getMessageDispatcher();
        val configProvider = game.getConfigProvider();

        this.registerSubCommand(
                new AuctionStartSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericStopSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericHelpSubCommand(plugin, configProvider, messageDispatcher),
                new GenericReloadSubCommand(plugin, game, configProvider)
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {


        return false;
    }

}
