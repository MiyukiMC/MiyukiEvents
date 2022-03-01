package app.miyuki.miyukievents.bukkit.commands.evaluator;

import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(staticName = "of")
public class GenericStartConditionEvaluator {

    private final MessageDispatcher messageDispatcher;

    public boolean evaluate(@NotNull CommandSender sender, GameState gameState) {

        if (gameState == GameState.QUEUE) {
            messageDispatcher.dispatch(sender, "GameAlreadyInQueue");
            return false;
        }

        if (gameState != GameState.STOPPED) {
            messageDispatcher.dispatch(sender, "GameAlreadyStarted");
            return false;
        }

        return true;
    }

}