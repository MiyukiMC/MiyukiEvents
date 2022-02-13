package app.miyuki.miyukievents.bukkit.util;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@UtilityClass
public class Logger {

    private static final java.util.logging.Logger logger = JavaPlugin.getPlugin(MiyukiEvents.class).getLogger();

    private static final String PREFIX = ChatUtils.colorize("[MiyukiEvents] ");

    public static void log(@NotNull String log) {
        logger.log(Level.INFO, PREFIX + log);
    }

    public static void log(@NotNull Level level, @NotNull String log) {
        logger.log(level, PREFIX + log);
    }

    public static void console(@NotNull String log) {
        Bukkit.getConsoleSender().sendMessage(log);
    }


}
