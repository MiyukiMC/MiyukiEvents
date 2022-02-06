package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teamdeathmatch;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericHelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericReloadSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.GenericStopSubCommand;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultCommand extends Command {

    private final Game game;
    private final MessageDispatcher messageDispatcher;

    public DefaultCommand(@NotNull MiyukiEvents plugin, @NotNull Game game, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        this.game = game;
        messageDispatcher = game.getMessageDispatcher();
        GameConfigProvider configProvider = game.getConfigProvider();

        registerSubCommand(
                new SetKitSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericStopSubCommand(plugin, game, configProvider, messageDispatcher),
                new GenericHelpSubCommand(plugin, configProvider, messageDispatcher),
                new GenericReloadSubCommand(plugin, game, configProvider)
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if (sender instanceof Player && game.getGameState() == GameState.HAPPENING) {
            if (args.length == 0) {
                messageDispatcher.dispatch(sender, "CommandUsedIncorrectly");
                return false;
            }
            if ( game.getGameState() == GameState.HAPPENING)
                ((Chat) game).onChat((Player) sender, args[0]);
            else
                messageDispatcher.dispatch(sender, "");
            return true;
        }

        return false;
    }

}
