package app.miyuki.bukkit.api;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.user.User;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class MiyukiEventsAPI {

    private final MiyukiEvents plugin = JavaPlugin.getPlugin(MiyukiEvents.class);

    @NotNull
    public User getUser(@NotNull String playerName) {
        return null;
    }

}
