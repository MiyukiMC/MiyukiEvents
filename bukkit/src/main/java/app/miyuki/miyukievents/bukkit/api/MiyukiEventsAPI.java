package app.miyuki.miyukievents.bukkit.api;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.user.User;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MiyukiEventsAPI {

    private final MiyukiEvents plugin = JavaPlugin.getPlugin(MiyukiEvents.class);

    @NotNull
    public User getUser(@NotNull String playerName) {
        return null;
    }

}
