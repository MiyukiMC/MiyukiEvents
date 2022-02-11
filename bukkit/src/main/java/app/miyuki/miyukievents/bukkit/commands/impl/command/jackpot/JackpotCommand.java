package app.miyuki.miyukievents.bukkit.commands.impl.command.jackpot;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericHelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericReloadSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStartSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStopSubCommand;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JackpotCommand extends Command {

    private Game game;

    public JackpotCommand(@NotNull MiyukiEvents plugin, @NotNull Game game, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        this.game = game;
        MessageDispatcher messageDispatcher = game.getMessageDispatcher();
        GameConfigProvider configProvider = game.getConfigProvider();

        registerSubCommand(
                new GenericStartSubCommand(plugin, game, configProvider),
                new GenericStopSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericHelpSubCommand(plugin, configProvider, messageDispatcher),
                new GenericReloadSubCommand(plugin, game, configProvider),
                new JackpotChanceSubCommand(plugin, game, configProvider, messageDispatcher)
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender instanceof Player && game.getGameState() == GameState.STARTED) {
            val player = (Player) sender;

            if (args.length > 0) {
                ((app.miyuki.miyukievents.bukkit.game.Command) game).onCommand(player, args);
            } else {
                Bukkit.dispatchCommand(player, getName() + " help");
            }

            return false;
        }

        Bukkit.dispatchCommand(sender, getName() + " help");

        return false;
    }
}
