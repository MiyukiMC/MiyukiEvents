package app.miyuki.bukkit.util.chat;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class ChatUtils {

    public String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
