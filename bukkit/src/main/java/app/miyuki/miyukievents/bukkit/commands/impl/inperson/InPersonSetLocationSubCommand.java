package app.miyuki.miyukievents.bukkit.commands.impl.inperson;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.inperson.InPerson;
import app.miyuki.miyukievents.bukkit.game.inperson.Solo;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InPersonSetLocationSubCommand extends SubCommand {

    private final GameConfigProvider configProvider;
    private final MessageDispatcher messageDispatcher;
    private final LocationType locationType;
    private final InPerson<?> game;

    public InPersonSetLocationSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull InPerson<?> game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher,
            @NotNull InPersonSetLocationSubCommand.LocationType locationType
    ) {
        super(plugin, false);
        this.game = game;
        this.configProvider = configProvider;
        this.messageDispatcher = messageDispatcher;
        this.locationType = locationType;
    }

    @Override
    public List<String> getAliases() {
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Set" + locationType.locationName + ".Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.Set" + locationType.locationName + ".Permission");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        val globalMessageDispatcher = plugin.getGlobalMessageDispatcher();

        if (game.getGameState() != GameState.STOPPED) {
            globalMessageDispatcher.dispatch(sender, "NeedStopGame");
            return false;
        }

        val player = (Player) sender;

        val location = player.getLocation();

        switch (locationType) {
            case EXIT:
                game.setExit(location);
                break;
            case CABIN:
                game.setCabin(location);
                break;
            case LOBBY:
                game.setLobby(location);
                break;
            case ENTRY:
                ((Solo<?>) game).setEntry(location);
            default:
                return false;
        }

        messageDispatcher.dispatch(sender, locationType.messagePath);
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

}