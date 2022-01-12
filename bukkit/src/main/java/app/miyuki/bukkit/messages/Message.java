package app.miyuki.bukkit.messages;

import org.bukkit.command.CommandSender;

import java.util.Map;

public interface Message {

    void dispatch(CommandSender sender);

    void dispatch(CommandSender sender, Map<String, String> placeholders);

}
