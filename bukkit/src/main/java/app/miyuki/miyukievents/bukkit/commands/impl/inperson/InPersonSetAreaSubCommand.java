package app.miyuki.miyukievents.bukkit.commands.impl.inperson;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.inperson.InPerson;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InPersonSetAreaSubCommand extends SubCommand {

    private final InPerson<?> game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    public InPersonSetAreaSubCommand(
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
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.SetArea.Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.SetArea.Permission");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (game.getGameState() != GameState.STOPPED) {
            plugin.getGlobalMessageDispatcher().dispatch(sender, "NeedStopGame");
            return false;
        }

        val worldEditAPI = plugin.getWorldEditProvider().provide().orElseThrow(IllegalStateException::new);

        val player = (Player) sender;

        val selection = worldEditAPI.getSelector(player);

        if (selection == null) {
            messageDispatcher.dispatch(sender, "AreaNotSelected");
            return false;
        }

        game.setPos1(selection.getFirst());
        game.setPos2(selection.getSecond());

        messageDispatcher.dispatch(sender, "SetAreaSuccessfully");
        return true;
    }

}
