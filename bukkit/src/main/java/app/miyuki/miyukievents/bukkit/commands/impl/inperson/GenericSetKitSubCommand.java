package app.miyuki.miyukievents.bukkit.commands.impl.inperson;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.commands.evaluator.TeamArgsEvaluator;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.impl.inperson.Deathmatch;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.util.player.PlayerUtils;
import com.google.common.collect.ImmutableList;
import lombok.val;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GenericSetKitSubCommand extends SubCommand {


    private final Game game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    private final int teams;

    public GenericSetKitSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, false);
        this.game = game;
        this.messageDispatcher = messageDispatcher;
        this.configProvider = configProvider;

        val config = configProvider.provide(ConfigType.CONFIG);

        teams = config.contains("Teams") ? config.getInt("Teams") : -1;
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

        if (teams != -1 && !TeamArgsEvaluator.of(messageDispatcher).evaluate(sender, teams, args, "IncorrectSetKitCommand")) {
            return false;
        }

        val teamNumber = teams != -1 ? Integer.parseInt(args[0]) : -1;

        if (game.getGameState() != GameState.STOPPED) {
            plugin.getGlobalMessageDispatcher().dispatch(sender, "NeedStopGame");
            return false;
        }

        val player = (Player) sender;

        val inventory = player.getInventory();
        ItemStack[] items = (ItemStack[]) ArrayUtils.addAll(inventory.getContents(), inventory.getArmorContents());

        val serializedInventory = plugin.getItemSerialAdapter().adapt(items);

        val data = configProvider.provide(ConfigType.DATA);

        data.set("Kit" + (teamNumber != -1 ? "." + teamNumber : ""), serializedInventory);

        data.saveConfig();

        val kits = ((Deathmatch) game).getKits();

        kits.put(teamNumber != -1 ? teamNumber : 0, items);

        PlayerUtils.setInventoryFromString(player, serializedInventory);

        messageDispatcher.dispatch(sender, "SetKitSuccessfully", it -> it.replace("{team}", String.valueOf(teamNumber)));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {

        if (teams == -1)
            return ImmutableList.of();

        return IntStream.rangeClosed(1, teams)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

}