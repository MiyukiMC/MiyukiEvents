package app.miyuki.miyukievents.bukkit.commands.evaluator;

import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.IntRange;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(staticName = "of")
public class TeamArgsEvaluator {

    private final MessageDispatcher messageDispatcher;

    public boolean evaluate(@NotNull CommandSender sender, int teams, String[] args, String incorrectMessage) {

        if (args.length == 0) {
            messageDispatcher.dispatch(sender, incorrectMessage);
            return false;
        }

        if (StringUtils.isNumeric(args[0])) {
            messageDispatcher.dispatch(sender, "InvalidTeamNumber");
            return false;
        }

        if (!(new IntRange(1, 3).containsInteger(Integer.parseInt(args[0])))) {
            messageDispatcher.dispatch(sender, "NumberOutOfTeamRange", it -> it.replace("{maxteam}", String.valueOf(teams)));
            return false;
        }

        return true;
    }

}
