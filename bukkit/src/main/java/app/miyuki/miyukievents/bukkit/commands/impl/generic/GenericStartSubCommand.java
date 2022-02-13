package app.miyuki.miyukievents.bukkit.commands.impl.generic;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.InPerson;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import com.google.common.collect.Lists;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GenericStartSubCommand extends SubCommand {


    private final MessageDispatcher messageDispatcher;
    private final GameConfigProvider configProvider;
    private final Game<?> game;

    public GenericStartSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Game<?> game,
            @NotNull MessageDispatcher messageDispatcher,
            @NotNull GameConfigProvider configProvider
    ) {
        super(plugin, true);
        this.game = game;
        this.messageDispatcher = messageDispatcher;
        this.configProvider = configProvider;
    }

    @Override
    public List<String> getAliases() {
        return configProvider.provide(ConfigType.CONFIG).getStringList("SubCommands.Start.Names");
    }

    @Override
    public @Nullable String getPermission() {
        return configProvider.provide(ConfigType.CONFIG).getString("SubCommands.Start.Permission");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {

        val globalMessageDispatcher = plugin.getGlobalMessageDispatcher();

        if (game.getGameState() == GameState.QUEUE) {
            globalMessageDispatcher.dispatch(sender, "GameAlreadyInQueue");
            return false;
        }

        if (game.getGameState() != GameState.STOPPED) {
            globalMessageDispatcher.dispatch(sender, "GameAlreadyStarted");
            return false;
        }

        if (game instanceof InPerson) {

            val inPersonGame = (InPerson<?>) game;

            if (inPersonGame.getLobby() == null) {
                messageDispatcher.dispatch(sender, "LobbyIsNotSet");
                return false;
            }

            if (inPersonGame.getCabin() == null) {
                messageDispatcher.dispatch(sender, "CabinIsNotSet");
                return false;
            }

            if (inPersonGame.getExit() == null) {
                messageDispatcher.dispatch(sender, "ExitIsNotSet");
                return false;
            }

            val entries = inPersonGame.getEntries();

            val config = configProvider.provide(ConfigType.CONFIG);

            if (inPersonGame.isTeamsEnabled()) {

                List<String> undefinedEntries = Lists.newArrayList();
                List<String> undefinedKits = Lists.newArrayList();

                for (int i = 1; i <= config.getInt("Teams"); i++) {
                    if (entries.containsKey(i)) {
                        undefinedEntries.add(String.valueOf(i));
                    }
                }

                if (undefinedEntries.isEmpty()) {
                    messageDispatcher.dispatch(sender, "EntryIsNotSet",
                            message -> message.replace("{entry}", String.join(", ", undefinedEntries)));
                    return false;
                }

            } else if (entries.isEmpty()) {
                messageDispatcher.dispatch(sender, "EntryIsNotSet");
                return false;
            }




        }


        game.setGameState(GameState.QUEUE);
        plugin.getQueue().register(game);

        globalMessageDispatcher.dispatch(sender, "GameAddedToQueue");

        return true;
    }

}
