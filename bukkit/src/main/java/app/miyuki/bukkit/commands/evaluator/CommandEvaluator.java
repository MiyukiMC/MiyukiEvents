package app.miyuki.bukkit.commands.evaluator;

import app.miyuki.bukkit.MiyukiEvents;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(staticName = "of")
public class CommandEvaluator {

    private final MiyukiEvents plugin;

    public boolean evaluate(@NotNull CommandSender sender, String permission, boolean console) {
        val messageDispatcher = plugin.getGlobalMessageDispatcher();
        if (permission != null && sender.hasPermission(permission)) {
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
