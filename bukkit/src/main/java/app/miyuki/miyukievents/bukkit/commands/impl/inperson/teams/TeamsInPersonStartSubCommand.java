package app.miyuki.miyukievents.bukkit.commands.impl.inperson.teams;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.commands.SubCommand;
import app.miyuki.miyukievents.bukkit.commands.evaluator.GenericStartConditionEvaluator;
import app.miyuki.miyukievents.bukkit.commands.evaluator.InPersonStartConditionEvaluator;
import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.inperson.Teams;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TeamsInPersonStartSubCommand extends SubCommand {

    private final GameConfigProvider configProvider;
    private final MessageDispatcher messageDispatcher;
    private final Teams<?> game;

    public TeamsInPersonStartSubCommand(
            @NotNull MiyukiEvents plugin,
            @NotNull Teams<?> game,
            @NotNull GameConfigProvider configProvider,
            @NotNull MessageDispatcher messageDispatcher
    ) {
        super(plugin, true);
        this.game = game;
        this.configProvider = configProvider;
        this.messageDispatcher = messageDispatcher;
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

        if (!GenericStartConditionEvaluator.of(globalMessageDispatcher).evaluate(sender, game.getGameState()))
            return false;

        if (!InPersonStartConditionEvaluator.of(messageDispatcher).evaluate(sender, game))
            return false;

        val entries = game.getEntries();

        if (entries.size() < 2) {
            messageDispatcher.dispatch(sender, "NoEntrySet");
            return false;
        }

        if (game.isKitRequired()) {

            val kits = game.getKits();

            val missingKits = IntStream.range(1, entries.size()).boxed()
                    .filter(it -> !kits.containsKey(it))
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            if (!missingKits.isEmpty()) {
                messageDispatcher.dispatch(
                        sender,
                        "SetMissingKits",
                        it -> it.replace("{teams}", String.join(", ", missingKits))
                );
                return false;
            }

        }

//        game.setGameState(GameState.QUEUE);
//        plugin.getQueue().register(game);

        globalMessageDispatcher.dispatch(sender, "GameAddedToQueue");

        return true;
    }

}
