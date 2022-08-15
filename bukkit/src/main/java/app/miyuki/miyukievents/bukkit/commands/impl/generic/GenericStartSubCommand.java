package app.miyuki.miyukievents.bukkit.commands.impl.generic;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.commands.evaluator.GenericStartConditionEvaluator;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GenericStartSubCommand extends SubCommand {

    private final Game<?> game;

    public GenericStartSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game<?> game
    ) {
        super(plugin, true);
        this.game = game;
    }

    @SneakyThrows
    @Override
    public List<String> getAliases() {
        return game.getConfig().getRoot().node("SubCommands", "Start", "Names").getList(String.class, ArrayList::new);
    }

    @Override
    public @Nullable String getPermission() {
        return game.getConfig().getRoot().node("SubCommands", "Start", "Permission").getString();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        val globalMessageDispatcher = plugin.getGlobalMessageDispatcher();

        if (!GenericStartConditionEvaluator.evaluate(globalMessageDispatcher, sender, game.getGameState()))
            return false;

        game.setGameState(GameState.QUEUE);
        plugin.getGameManager().getQueue().register(game);

        globalMessageDispatcher.dispatch(sender, "GameAddedToQueue");

        return true;
    }

}
