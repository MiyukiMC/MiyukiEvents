package app.miyuki.miyukievents.bukkit.user;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.async.Async;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {

    private final MiyukiEvents plugin;

    private final Cache<UUID, User> users = CacheBuilder.newBuilder()
            .build();

    public UserRepository(MiyukiEvents plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> Async.run(this::cleanUp), 20L);
    }

    public Optional<User> findById(@NotNull UUID uniqueId) {
        return Optional.ofNullable(users.getIfPresent(uniqueId));
    }

    public void save(@NotNull User user) {
        users.put(user.getUuid(), user);
    }

    public void remove(@NotNull UUID uniqueId) {
        users.invalidate(uniqueId);
    }

    public void cleanUp() {
        val gameManager = plugin.getGameManager();

        users.asMap().entrySet().removeIf(entries ->
                Bukkit.getPlayer(entries.getKey()) == null
                        && gameManager.getQueue().isEmpty()
                        && gameManager.getCurrentGame() == null);
    }


}
