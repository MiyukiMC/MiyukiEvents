package app.miyuki.miyukievents.bukkit.commands.impl.generic;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StartSubCommand extends SubCommand {


    private final GameConfigProvider configProvider;
    private final Game game;

    public StartSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game game,
            @NotNull GameConfigProvider configProvider
    ) {
        super(plugin, true);
        this.game = game;
        this.configProvider = configProvider;
    }

    @Override
    public List<String> getAliases() {
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Start.Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.Start.Permission");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        val globalMessageDispatcher = plugin.getGlobalMessageDispatcher();

        if (game.getGameState() == GameState.QUEUE) {
            globalMessageDispatcher.dispatch(sender, "GameAlreadyInQueue");
            return false;
        }

        if (game.getGameState() != GameState.STOPPED) {
            globalMessageDispatcher.dispatch(sender, "GameAlreadyStarted");
            return false;
        }

        game.setGameState(GameState.QUEUE);
        plugin.getQueue().register(game);

        globalMessageDispatcher.dispatch(sender, "GameAddedToQueue");

        return true;
    }

}
