package app.miyuki.miyukievents.bukkit.commands.impl.chat;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.HelpSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.StartSubCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.generic.StopSubCommand;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.impl.chat.Word;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WordCommand extends Command {

    private final Game game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    public WordCommand(@NotNull MiyukiEvents plugin, @NotNull Game game, @NotNull String name, @NotNull List<String> aliases) {
        super(plugin, name, aliases, true);

        this.game = game;
        messageDispatcher = game.getMessageDispatcher();
        configProvider = game.getConfigProvider();

        registerSubCommand(
                new StartSubCommand(plugin, game, configProvider),
                new StopSubCommand(plugin, game, configProvider, messageDispatcher),
                new HelpSubCommand(plugin, configProvider, messageDispatcher)
        );
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if (game.getGameState() == GameState.HAPPENING) {
            ((Chat) game).onChat((Player) sender, args[0]);
            return true;
        }

        Bukkit.dispatchCommand(sender, getName() + " help");

        return false;
    }

}