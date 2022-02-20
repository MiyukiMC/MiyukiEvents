package app.miyuki.miyukievents.bukkit.user;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.async.Async;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class UserRepository {


    private final Cache<UUID, User> users;

    public UserRepository(MiyukiEvents plugin) {
        users = Caffeine.newBuilder()
                .executor(Async.getWorker())
                .removalListener((RemovalListener<UUID, User>) (key, value, cause) -> {

                    if (value == null)
                        return;

                    System.out.println("DEBUG[Cache]: " + value.getPlayerName());

                    plugin.getStorage().updateUser(value);
                })
                .maximumWeight(1L)
                .weigher(
                        (key, value) -> (Bukkit.getPlayer(key) == null && plugin.getQueue().isEmpty() && plugin.getGameManager().getCurrentGame() == null) ? 1 : 0
                )
                .build();
    }

    public @Nullable User findById(@NotNull UUID id) {
        try {
            return users.getIfPresent(id);
        } catch (NullPointerException exception) {
            return null;
        }
    }

    public void save(User user) {
        users.put(user.getUuid(), user);
    }

    public void remove(UUID id) {
        users.invalidate(id);
    }


}
