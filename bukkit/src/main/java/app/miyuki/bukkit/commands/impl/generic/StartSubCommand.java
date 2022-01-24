package app.miyuki.bukkit.commands.impl.generic;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.commands.SubCommand;
import app.miyuki.bukkit.config.ConfigType;
import app.miyuki.bukkit.config.GameConfigProvider;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StartSubCommand extends SubCommand {


    private final GameConfigProvider configProvider;
    private final Game game;

    public StartSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game game,
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

        plugin.getQueue().register(game);

        globalMessageDispatcher.dispatch(sender, "GameAddedToQueue");

        return true;
    }

}
