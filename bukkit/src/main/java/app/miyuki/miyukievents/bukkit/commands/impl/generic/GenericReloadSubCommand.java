package app.miyuki.miyukievents.bukkit.commands.impl.generic;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GenericReloadSubCommand extends SubCommand {

    private final Game<?> game;

    public GenericReloadSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game<?> game
    ) {
        super(plugin, true);
        this.game = game;
    }

    @SneakyThrows
    @Override
    public List<String> getAliases() {
        return game.getConfig().getRoot().node("SubCommands", "Reload", "Names").getList(String.class, ArrayList::new);
    }

    @Override
    public @Nullable String getPermission() {
        return game.getConfig().getRoot().node("SubCommands", "Reload", "Permission").getString();
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        val globalMessageDispatcher = plugin.getGlobalMessageDispatcher();

        val gameState = game.getGameState();

        if (gameState != GameState.STOPPED) {
            globalMessageDispatcher.dispatch(sender, "NeedStopGameToReload");
            return false;
        }

        game.getConfig().reload();
        game.getMessages().reload();
        game.getData().reload();
        game.getMessageDispatcher().clear();

        globalMessageDispatcher.dispatch(sender, "GameReloadedSuccessfully");

        return true;
    }

}

