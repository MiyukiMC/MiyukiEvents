package app.miyuki.miyukievents.bukkit.commands.evaluator;

import app.miyuki.miyukievents.bukkit.messages.MessageDispatcher;
import app.miyuki.miyukievents.bukkit.user.UserRepository;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandEvaluator {

    public static boolean evaluate(@NotNull MessageDispatcher messageDispatcher, @NotNull CommandSender sender, String permission, boolean console) {
        if (permission != null && !sender.hasPermission(permission)) {
            messageDispatcher.dispatch(sender, "NoPermission");
            return false;
        }

        if (!console && !(sender instanceof Player)) {
            messageDispatcher.dispatch(sender, "OnlyPlayers");
            return false;
        }

        return true;
    }

}
