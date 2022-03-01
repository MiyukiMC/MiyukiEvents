package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teams;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.inperson.Teams;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeamsInPersonRemoveEntrySubCommand extends SubCommand {

    private final Teams<?> game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    public TeamsInPersonRemoveEntrySubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Teams<?> game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, false);
        this.game = game;
        this.messageDispatcher = messageDispatcher;
        this.configProvider = configProvider;
    }

    @Override
    public List<String> getAliases() {
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.RemoveEntry.Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.RemoveEntry.Permission");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if (game.getGameState() != GameState.STOPPED) {
            plugin.getGlobalMessageDispatcher().dispatch(sender, "NeedStopGame");
            return false;
        }

        val entries = game.getEntries();

        if (entries.isEmpty()) {
            messageDispatcher.dispatch(sender, "NoEntriesToRemove");
            return false;
        }

        val team = entries.size();

        game.removeEntry(team);
        game.removeKit(team);

        messageDispatcher.dispatch(sender, "RemoveEntrySuccessfully", it -> it.replace("{team}", String.valueOf(team)));
        return true;
    }

}
