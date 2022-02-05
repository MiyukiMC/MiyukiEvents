package app.miyuki.miyukievents.bukkit.commands.impl.command;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.HelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.ReloadSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.StartSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.StopSubCommand;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PoolCommand extends Command {

    private Game game;
    private final MessageDispatcher messageDispatcher;

    public PoolCommand(@NotNull MiyukiEvents plugin, @NotNull Game game, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        this.game = game;
        messageDispatcher = game.getMessageDispatcher();
        GameConfigProvider configProvider = game.getConfigProvider();

        registerSubCommand(
                new StartSubCommand(plugin, game, configProvider),
                new StopSubCommand(plugin, game, configProvider, messageDispatcher),
                new HelpSubCommand(plugin, configProvider, messageDispatcher),
                new ReloadSubCommand(plugin, game, configProvider, messageDispatcher)
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if (sender instanceof Player && game.getGameState() == GameState.HAPPENING) {
            ((app.miyuki.miyukievents.bukkit.game.Command) game).onCommand(((Player) sender), args);
            return true;
        }

        return false;
    }
}
