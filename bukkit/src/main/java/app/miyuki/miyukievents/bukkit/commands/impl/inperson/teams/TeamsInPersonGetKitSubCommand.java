package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teams;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
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

public class TeamsInPersonGetKitSubCommand extends SubCommand {

    private final Teams<?> game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    public TeamsInPersonGetKitSubCommand(
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
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Kit.Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.Kit.Permission");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        if (args.length == 0 || !StringUtils.isNumeric(args[0])) {
            messageDispatcher.dispatch(sender, "IncorrectKitCommand");
            return false;
        }

        val team = Integer.parseInt(args[0]);

        val kit = game.getKits().getOrDefault(team, null);

        if (kit == null) {
            messageDispatcher.dispatch(sender, "KitNotFound", it -> it.replace("{team}", String.valueOf(team)));
            return false;
        }

        val player = (Player) sender;

        player.getInventory().setContents(kit);

        messageDispatcher.dispatch(sender, "KitSuccessfullyObtained", it -> it.replace("{team}", String.valueOf(team)));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return IntStream.rangeClosed(1, game.getKits().size())
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

}