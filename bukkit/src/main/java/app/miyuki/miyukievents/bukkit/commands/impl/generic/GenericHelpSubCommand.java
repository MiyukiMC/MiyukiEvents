package app.miyuki.miyukievents.bukkit.commands.impl.generic;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GenericHelpSubCommand extends SubCommand {

    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    public GenericHelpSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, true);
        this.messageDispatcher = messageDispatcher;
        this.configProvider = configProvider;
    }

    @Override
    public List<String> getAliases() {
        val names = configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Help.Names");

        if (names.isEmpty())
            names.add("help");

        return names;
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        val config = configProvider.provide(ConfigType.CONFIG);
        val permission = config.getString("SubCommands.HelpAdmin.Permission");

        if (sender.hasPermission(permission))
            messageDispatcher.dispatch(sender, "HelpAdmin");
        else
            messageDispatcher.dispatch(sender, "Help");

        return true;
    }



}
