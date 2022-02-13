package app.miyuki.miyukievents.bukkit.commands.impl.generic;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GenericStopSubCommand extends SubCommand {

    private final GameConfigProvider configProvider;
    private final MessageDispatcher messageDispatcher;
    private final Game<?> game;

    public GenericStopSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game<?> game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, true);
        this.game = game;
        this.configProvider = configProvider;
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public List<String> getAliases() {
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Stop.Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.Stop.Permission");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        val globalMessageDispatcher = plugin.getGlobalMessageDispatcher();

        if (game.getGameState() == GameState.STOPPED) {
            globalMessageDispatcher.dispatch(sender, "NoGamesGoingOn");
            return false;
        }

        plugin.getQueue().unregister(game);
        game.stop();

        Bukkit.getOnlinePlayers().forEach(it -> messageDispatcher.dispatch(it, "Cancelled"));

        globalMessageDispatcher.dispatch(sender, "GameCanceled");

        return true;
    }

}