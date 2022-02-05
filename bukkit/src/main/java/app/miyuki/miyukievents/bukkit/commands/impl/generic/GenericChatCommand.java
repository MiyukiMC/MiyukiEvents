package app.miyuki.miyukievents.bukkit.commands.impl.generic;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenericChatCommand extends Command {

    private final Game game;
    private final MessageDispatcher messageDispatcher;

    public GenericChatCommand(@NotNull MiyukiEvents plugin, @NotNull Game game, @NotNull String name, @NotNull List<String> aliases) {
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
            if (args.length == 0) {
                messageDispatcher.dispatch(sender, "CommandUsedIncorrectly");
                return false;
            }
            ((Chat) game).onChat((Player) sender, args);
            return true;
        }

        Bukkit.dispatchCommand(sender, getName() + " help");

        return false;
    }

}