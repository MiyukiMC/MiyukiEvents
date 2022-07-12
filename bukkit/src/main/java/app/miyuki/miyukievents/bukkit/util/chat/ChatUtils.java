package app.miyuki.miyukievents.bukkit.util.chat;

import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

@UtilityClass
public class ChatUtils {

    private boolean HEX_COLOR_SUPPORT;

    private final Pattern HEX_COLOR_PATTERN = Pattern.compile("&#([0-9A-Fa-f]{6})");

    static {
        try {
            net.md_5.bungee.api.ChatColor.class.getDeclaredMethod("of", String.class);
            HEX_COLOR_SUPPORT = true;
        } catch (NoSuchMethodException e) {
            HEX_COLOR_SUPPORT = false;
        }
    }

    /*
    https://github.com/RoinujNosde/SimpleClans/blob/dev/src/main/java/net/sacredlabyrinth/phaed/simpleclans/utils/ChatUtils.java
     */
    public String colorize(@NotNull String text) {
        var coloredText = text;

        if (HEX_COLOR_SUPPORT) {
            val matcher = HEX_COLOR_PATTERN.matcher(coloredText);
            val buffer = new StringBuffer();

            while (matcher.find())
                matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString());

            coloredText = matcher.appendTail(buffer).toString();
        }

        return ChatColor.translateAlternateColorCodes('&', coloredText);
    }

    /*
    https://github.com/RoinujNosde/SimpleClans/blob/dev/src/main/java/net/sacredlabyrinth/phaed/simpleclans/utils/ChatUtils.java
     */
    public String stripColors(String text) {
        var colorlessText = text;

        if (HEX_COLOR_SUPPORT) {
            val matcher = HEX_COLOR_PATTERN.matcher(colorlessText);
            val buffer = new StringBuffer();

            while (matcher.find())
                matcher.appendReplacement(buffer, "");

            colorlessText = matcher.appendTail(buffer).toString();
        }

        return ChatColor.stripColor(colorlessText);
    }

}