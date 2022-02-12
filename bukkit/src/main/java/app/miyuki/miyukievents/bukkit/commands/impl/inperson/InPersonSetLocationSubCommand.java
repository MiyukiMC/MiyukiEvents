package app.miyuki.miyukievents.bukkit.commands.impl.inperson;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.commands.evaluator.TeamArgsEvaluator;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.InPerson;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InPersonSetLocationSubCommand extends SubCommand {

    private final GameConfigProvider configProvider;
    private final MessageDispatcher messageDispatcher;
    private final LocationType locationType;
    private final Game game;

    private final int teams;

    public InPersonSetLocationSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher,
            @NotNull InPersonSetLocationSubCommand.LocationType locationType
    ) {
        super(plugin, false);
        this.game = game;
        this.configProvider = configProvider;
        this.messageDispatcher = messageDispatcher;
        this.locationType = locationType;

        val config = configProvider.provide(ConfigType.CONFIG);

        this.teams = config.contains("Teams") ? config.getInt("Teams") : -1;

    }

    @Override
    public List<String> getAliases() {
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Set" + locationType.locationName + ".Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.Set " + locationType.locationName + ".Permission");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if (teams == -1 && locationType == LocationType.ENTRY && !TeamArgsEvaluator.of(messageDispatcher).evaluate(sender, teams, args, "IncorrectSetEntryCommand")) {
            return false;
        }

        val globalMessageDispatcher = plugin.getGlobalMessageDispatcher();

        if (game.getGameState() != GameState.STOPPED) {
            globalMessageDispatcher.dispatch(sender, "NeedStopGame");
            return false;
        }

        val teamNumber = locationType == LocationType.ENTRY ? Integer.parseInt(args[0]) : -1;

        val playerLocation = ((Player) sender).getLocation();

        val inPersonGame = (InPerson) game;

        val data = configProvider.provide(ConfigType.DATA);

        data.set(locationType.locationName + (teamNumber != -1 ? "." + teamNumber : ""), plugin.getLocationAdapter().restore(playerLocation));
        data.saveConfig();

        switch (locationType) {
            case ENTRY:
                val entries = inPersonGame.getEntries();
                entries.put(Math.max(0, teamNumber), playerLocation);
                break;
            case EXIT:
                inPersonGame.setExit(playerLocation);
                break;
            case CABIN:
                inPersonGame.setCabin(playerLocation);
                break;
            case LOBBY:
                inPersonGame.setLobby(playerLocation);
                break;
            default:
                return false;
        }

        messageDispatcher.dispatch(sender, locationType.messagePath, it -> it.replace("{team}", String.valueOf(teamNumber)));
        return true;
    }

    @AllArgsConstructor
    public enum LocationType {

        LOBBY("Lobby", "SetLobbySuccessfully"),
        CABIN("Cabin", "SetCabinSuccessfully"),
        EXIT("Exit", "SetExitSuccessfully"),
        ENTRY("Entry", "SetEntrySuccessfully");

        private final String locationName;
        private final String messagePath;

    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {

        if (teams == -1)
            return ImmutableList.of();

        return IntStream.rangeClosed(1, teams)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }


}