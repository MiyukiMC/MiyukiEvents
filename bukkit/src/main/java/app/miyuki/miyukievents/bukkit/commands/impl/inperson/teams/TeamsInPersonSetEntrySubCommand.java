package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teams;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.inperson.Teams;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeamsInPersonSetEntrySubCommand extends SubCommand {

    private final Teams<?> game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    public TeamsInPersonSetEntrySubCommand(
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
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.SetEntry.Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.SetEntry.Permission");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (game.getGameState() != GameState.STOPPED) {
            plugin.getGlobalMessageDispatcher().dispatch(sender, "NeedStopGame");
            return false;
        }

        if (args.length != 1 || !StringUtils.isNumeric(args[0])) {
            messageDispatcher.dispatch(sender, "IncorrectSetEntryCommand");
            return false;
        }

        val team = Integer.parseInt(args[0]);

        val entries = game.getEntries();

        val missingEntries = IntStream.range(1, team).boxed()
                .filter(it -> !entries.containsKey(it))
                .map(String::valueOf)
                .collect(Collectors.toList());

        if (team != 1 && !missingEntries.isEmpty()) {
            messageDispatcher.dispatch(
                    sender,
                    "SetMissingEntries",
                    it -> it.replace("{missing_entries}", String.join(", ", missingEntries))
            );
            return false;
        }

        val player = (Player) sender;

        val location = player.getLocation();

        game.addEntry(team, location);

        messageDispatcher.dispatch(sender, "SetEntrySuccessfully", it -> it.replace("{team}", String.valueOf(team)));
        return true;
    }


}
