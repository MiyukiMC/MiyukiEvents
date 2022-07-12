package app.miyuki.miyukievents.bukkit.commands.impl.command.jackpot;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.command.impl.Jackpot;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.util.random.RandomUtils;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JackpotChanceSubCommand extends SubCommand {

    private final Game<?> game;
    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;

    public JackpotChanceSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game<?> game,
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
        return this.configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Chance.Names");
    }

    @Override
    @Nullable
    public String getPermission() {
        return this.configProvider.provide(ConfigType.CONFIG).getString("SubCommand.Chance.Permission");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        val jackpotGame = (Jackpot) game;

        if (!(sender instanceof Player))
            return false;

        val player = (Player) sender;

        val user = plugin.getUserRepository().findById(player.getUniqueId()).get(); // null check

        if (jackpotGame.getPlayers().containsKey(user))
            messageDispatcher.dispatch(player, "YourChance", message -> message
                    .replace("{chance}", String.valueOf(RandomUtils.getChance(jackpotGame.getPlayers(), user))));
        else
            messageDispatcher.dispatch(player, "YouAreNotInTheJackpot");

        return false;
    }
}
