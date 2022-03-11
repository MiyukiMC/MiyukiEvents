package app.miyuki.miyukievents.bukkit.commands.impl.generic;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.commands.evaluator.GenericStartConditionEvaluator;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GenericStartSubCommand extends SubCommand {

    private final GameConfigProvider configProvider;
    private final Game<?> game;

    public GenericStartSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game<?> game,
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

        if (!GenericStartConditionEvaluator.of(globalMessageDispatcher).evaluate(sender, game.getGameState()))
            return false;

        game.setGameState(GameState.QUEUE);
        plugin.getGameManager().getQueue().register(game);

        globalMessageDispatcher.dispatch(sender, "GameAddedToQueue");

        return true;
    }

}
