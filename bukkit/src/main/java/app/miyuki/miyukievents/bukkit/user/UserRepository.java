package app.miyuki.miyukievents.bukkit.user;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class UserRepository {

    private final Map<UUID, User> users = Maps.newHashMap();

    public @Nullable User findById(UUID id) {
        return users.get(id);
    }

    public @Nullable User findByName(String name) {
        return users.values().stream().filter(user -> user.getPlayerName().equals(name)).findFirst().orElse(null);
    }

    public void save(User user) {
        users.put(user.getUuid(), user);
    }

    public void remove(UUID id) {
        users.remove(id);
    }

}