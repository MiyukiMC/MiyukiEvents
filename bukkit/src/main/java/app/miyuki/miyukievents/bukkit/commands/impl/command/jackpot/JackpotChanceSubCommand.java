package app.miyuki.miyukievents.bukkit.commands.impl.command.jackpot;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.impl.chat.Jackpot;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JackpotChanceSubCommand extends SubCommand {

    private final Game game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    public JackpotChanceSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, true);
        this.game = game;
        this.messageDispatcher = messageDispatcher;
        this.configProvider = configProvider;
    }

    @Override
    public List<String> getAliases() {
        val names = configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Chance.Names");

        if (names.isEmpty())
            names.add("chance");

        return names;
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        val jackpotGame = (Jackpot) game;

        if (sender instanceof Player) {
            val player = (Player) sender;
            if (!jackpotGame.getPlayers().containsKey(player.getName()))
                messageDispatcher.dispatch(player, "YouAreNotInTheJackpot");
            else
                messageDispatcher.dispatch(player, "YourChance", message -> message
                        .replace("{chance}", String.valueOf(RandomUtils.getChance(jackpotGame.getPlayers(), player.getName()))));
        }

        return false;
    }
}
