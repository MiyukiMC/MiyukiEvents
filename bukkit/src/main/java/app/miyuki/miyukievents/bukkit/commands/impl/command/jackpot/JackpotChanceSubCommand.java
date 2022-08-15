package app.miyuki.miyukievents.bukkit.commands.impl.command.jackpot;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.game.command.impl.Jackpot;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class JackpotChanceSubCommand extends SubCommand {

    private final Jackpot game;
    private final MessageDispatcher messageDispatcher;

    public JackpotChanceSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Jackpot game,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, true);
        this.game = game;
        this.messageDispatcher = messageDispatcher;
    }

    @SneakyThrows
    @Override
    public List<String> getAliases() {
        return game.getConfig().getRoot().node("SubCommands", "Chance", "Names").getList(String.class, ArrayList::new);
    }

    @Override
    public @Nullable String getPermission() {
        return game.getConfig().getRoot().node("SubCommands", "Chance", "Permission").getString();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return false;

        val player = (Player) sender;

        val user = plugin.getUserRepository().findById(player.getUniqueId()).get(); // null check

        if (game.getPlayers().containsKey(user))
            messageDispatcher.dispatch(player, "YourChance", message -> message
                    .replace("{chance}", String.valueOf(RandomUtils.getChance(game.getPlayers(), user))));
        else
            messageDispatcher.dispatch(player, "YouAreNotInTheJackpot");

        return false;
    }
}
