package app.miyuki.bukkit.commands.impl.generic;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.commands.SubCommand;
import app.miyuki.bukkit.config.ConfigType;
import app.miyuki.bukkit.config.GameConfigProvider;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.messages.Message;
import app.miyuki.bukkit.messages.MessageDispatcher;
import com.google.common.collect.Lists;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HelpSubCommand extends SubCommand {


    private final MessageDispatcher messageDispatcher;

    public HelpSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, true);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public List<String> getAliases() {
        return Lists.newArrayList("help");
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if ()

        return true;
    }



}
