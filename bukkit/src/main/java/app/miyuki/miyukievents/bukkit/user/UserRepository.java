package app.miyuki.miyukievents.bukkit.user;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.async.Async;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {


    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    private final MiyukiEvents plugin;

    public UserRepository(MiyukiEvents plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> Async.run(this::cleanUp), 20L);
    }

    public @Nullable User findById(@NotNull UUID id) {
        return users.get(id);
    }

    public void save(User user) {
        users.put(user.getUuid(), user);
    }

    public void remove(UUID id) {
        users.remove(id);
    }

    public void cleanUp() {
        users.entrySet().removeIf(entries ->
                Bukkit.getPlayer(entries.getKey()) == null
                        && plugin.getQueue().isEmpty()
                        && plugin.getGameManager().getCurrentGame() == null);
    }


}
