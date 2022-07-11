package app.miyuki.miyukievents.bukkit.commands.impl.inperson.solo;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.inperson.Solo;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoloInPersonSetKitSubCommand  extends SubCommand {

    private final Solo<?> game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;


    public SoloInPersonSetKitSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Solo<?> game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, false);
        this.messageDispatcher = messageDispatcher;
        this.configProvider = configProvider;
        this.game = game;
    }

    @Override
    public List<String> getAliases() {
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.SetKit.Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.SetKit.Permission");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (game.getGameState() != GameState.STOPPED) {
            plugin.getGlobalMessageDispatcher().dispatch(sender, "NeedStopGame");
            return false;
        }

        val player = (Player) sender;

        val inventory = player.getInventory();

        game.setKit(inventory);

        messageDispatcher.dispatch(sender, "SetKitSuccessfully");
        return true;
    }


}
