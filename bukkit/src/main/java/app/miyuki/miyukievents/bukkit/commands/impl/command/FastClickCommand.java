package app.miyuki.miyukievents.bukkit.commands.impl.command;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.GameCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericHelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericReloadSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStartSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStopSubCommand;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FastClickCommand extends GameCommand {

    private final Game<?> game;

    public FastClickCommand(@NotNull MiyukiEvents plugin, @NotNull Game<?> game, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        this.game = game;

        val messageDispatcher = game.getMessageDispatcher();
        val configProvider = game.getConfigProvider();

        registerSubCommand(
                new GenericStartSubCommand(plugin, game, configProvider),
                new GenericStopSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericHelpSubCommand(plugin, configProvider, messageDispatcher),
                new GenericReloadSubCommand(plugin, game, configProvider)
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender instanceof Player && game.getGameState() == GameState.STARTED && args.length > 0) {
            val player = (Player) sender;
            ((app.miyuki.miyukievents.bukkit.game.command.Command<?>) game).onCommand(player, args);
            return false;
        }

        Bukkit.dispatchCommand(sender, getName() + " help");

        return false;
    }

}
