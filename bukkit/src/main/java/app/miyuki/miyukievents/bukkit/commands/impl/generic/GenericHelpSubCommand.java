package app.miyuki.miyukievents.bukkit.commands.impl.generic;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GenericHelpSubCommand extends SubCommand {

    private final Game<?> game;
    private final MessageDispatcher messageDispatcher;

    public GenericHelpSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game<?> game,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, true);
        this.game = game;
        this.messageDispatcher = messageDispatcher;
    }

    @SneakyThrows
    @Override
    public List<String> getAliases() {
        val aliases = game.getConfig().getRoot().node("SubCommands", "Help", "Names").getList(String.class, ArrayList::new);

        aliases.add("help");

        return aliases;
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        val permission = game.getConfig().getRoot().node("SubCommands", "HelpAdmin", "Permission").getString();

        if (permission != null && sender.hasPermission(permission))
            messageDispatcher.dispatch(sender, "HelpAdmin");
        else
            messageDispatcher.dispatch(sender, "Help");

        return true;
    }


}
