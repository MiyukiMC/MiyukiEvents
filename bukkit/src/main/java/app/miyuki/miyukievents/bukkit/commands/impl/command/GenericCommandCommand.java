package app.miyuki.miyukievents.bukkit.commands.impl.command;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.command.jackpot.JackpotChanceSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericHelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericReloadSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStartSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStopSubCommand;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.command.Jackpot;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenericCommandCommand extends Command {

    private final Game<?> game;

    public GenericCommandCommand(@NotNull MiyukiEvents plugin, @NotNull Game<?> game, @NotNull String name, @NotNull List<String> aliases) {
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

        if (game instanceof Jackpot)
            registerSubCommand(new JackpotChanceSubCommand(plugin, game, configProvider, messageDispatcher));

    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if (sender instanceof Player && game.getGameState() == GameState.STARTED) {
            ((app.miyuki.miyukievents.bukkit.game.command.Command<?>) game).onCommand((Player) sender, args);
            return true;
        }

        Bukkit.dispatchCommand(sender, getName() + " help");
        return false;
    }

}