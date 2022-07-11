package app.miyuki.miyukievents.bukkit.commands.impl.inperson;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.inperson.InPerson;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InPersonCabinSubCommand extends SubCommand {

    private final InPerson<?> game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    public InPersonCabinSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull InPerson<?> game,
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
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Cabin.Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.Cabin.Permission");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (game.getGameState() == GameState.STOPPED || game.getGameState() == GameState.STOPPING || game.getGameState() == GameState.QUEUE) {
            messageDispatcher.dispatch(sender, "CabinIsClosed");
            return false;
        }

        // game.getCabin() can be null ?
        ((Player) sender).teleport(game.getCabin());
        messageDispatcher.dispatch(sender, "EnterCabinSuccessfully");
        return true;
    }

}
