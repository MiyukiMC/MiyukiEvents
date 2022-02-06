package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teamdeathmatch;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.impl.inperson.TeamDeathmatch;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.util.player.PlayerUtils;
import lombok.val;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SetKitSubCommand extends SubCommand {


    private final Game game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    private final int teams;

    public SetKitSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, false);
        this.game = game;
        this.messageDispatcher = messageDispatcher;
        this.configProvider = configProvider;

        teams = configProvider.provide(ConfigType.CONFIG).getInt("Teams");
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

        if (args.length == 0) {
            messageDispatcher.dispatch(sender, "IncorrectSetKitCommand");
            return false;
        }

        if (StringUtils.isNumeric(args[0])) {
            messageDispatcher.dispatch(sender, "InvalidTeamNumber");
            return false;
        }

        val teamNumber = Integer.parseInt(args[0]);

        if (!(new IntRange(1, 3).containsInteger(teamNumber))) {
            messageDispatcher.dispatch(sender, "NumberOutOfTeamRange", it -> it.replace("{maxteam}", String.valueOf(teams)));
            return false;
        }

        if (game.getGameState() != GameState.STOPPED) {
            messageDispatcher.dispatch(sender, "NeedStopGame");
            return false;
        }

        val player = (Player) sender;

        val inventory = player.getInventory();
        ItemStack[] items = (ItemStack[]) ArrayUtils.addAll(inventory.getContents(), inventory.getArmorContents());

        val serializedInventory = plugin.getItemSerialAdapter().adapt(items);

        val data = configProvider.provide(ConfigType.DATA);

        data.set("Kit." + teamNumber, serializedInventory);
        data.saveConfig();

        val kits = ((TeamDeathmatch) game).getKits();

        kits.put(teamNumber, items);

        PlayerUtils.setInventoryFromString(player, serializedInventory);

        messageDispatcher.dispatch(sender, "SetKitSuccessfully", it -> it.replace("{team}", String.valueOf(teamNumber)));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return IntStream.rangeClosed(1, teams)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

}