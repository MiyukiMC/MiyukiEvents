package app.miyuki.miyukievents.bukkit.api;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.user.User;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class MiyukiEventsAPI {

    private final MiyukiEvents plugin = JavaPlugin.getPlugin(MiyukiEvents.class);

    /**
     *
     */
    @Nullable
    public Optional<User> getUser(@NotNull String playerName) {
        return null;
    }

    /**
     *
     */
    @Nullable
    public Optional<User> getUser(@NotNull UUID uuid) {
        return null;
    }

}
