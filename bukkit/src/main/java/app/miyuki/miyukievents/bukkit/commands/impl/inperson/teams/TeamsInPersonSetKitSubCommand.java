package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teams;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.inperson.Teams;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import com.google.common.collect.ImmutableList;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeamsInPersonSetKitSubCommand extends SubCommand {

    private final Teams<?> game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;


    public TeamsInPersonSetKitSubCommand(
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

        if (args.length != 1 || !StringUtils.isNumeric(args[0])) {
            messageDispatcher.dispatch(sender, "IncorrectSetKitCommand");
            return false;
        }

        val entries = game.getEntries();

        if (entries.isEmpty()) {
            messageDispatcher.dispatch(sender, "NeedSetEntries");
            return false;
        }

        val team = Integer.parseInt(args[0]);

        if (team < 0 || team > entries.size()) {
            messageDispatcher.dispatch(sender, "InvalidTeamNumber", it -> it.replace("{teams}", String.valueOf(team)));
            return false;
        }

        val player = (Player) sender;

        val inventory = player.getInventory();

        game.setKit(team, inventory);

        messageDispatcher.dispatch(sender, "SetKitSuccessfully", it -> it.replace("{team}", String.valueOf(team)));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {

        val entries = game.getEntries();

        if (entries.isEmpty())
            return ImmutableList.of();

        return IntStream.rangeClosed(1, entries.size())
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

}