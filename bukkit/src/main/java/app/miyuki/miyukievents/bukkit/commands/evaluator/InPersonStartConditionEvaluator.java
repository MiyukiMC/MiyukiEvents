package app.miyuki.miyukievents.bukkit.commands.evaluator;

import app.miyuki.miyukievents.bukkit.game.inperson.InPerson;
import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(staticName = "of")
public class InPersonStartConditionEvaluator {

    private final MessageDispatcher messageDispatcher;

    public boolean evaluate(@NotNull CommandSender sender, InPerson<?> game) {

        if (game.getLobby() == null) {
            this.messageDispatcher.dispatch(sender, "LobbyIsNotSet");
            return false;
        }

        if (game.getCabin() == null) {
            this.messageDispatcher.dispatch(sender, "CabinIsNotSet");
            return false;
        }

        if (game.getExit() == null) {
            this.messageDispatcher.dispatch(sender, "ExitIsNotSet");
            return false;
        }

        return true;
    }

}