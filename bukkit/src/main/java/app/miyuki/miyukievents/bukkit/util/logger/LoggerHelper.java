package app.miyuki.miyukievents.bukkit.util.logger;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.chat.ChatUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

@UtilityClass
public class LoggerHelper {

    private final java.util.logging.Logger logger = JavaPlugin.getPlugin(MiyukiEvents.class).getLogger();

    private final String PREFIX = ChatUtils.colorize("[MiyukiEvents] ");

    public void log(@NotNull String log) {
        logger.log(Level.INFO, log);
    }

    public void log(@NotNull Level level, @NotNull String log) {
        logger.log(level, log);
    }

    public void console(@NotNull String log) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + log);
    }

}
