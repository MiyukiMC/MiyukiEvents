package app.miyuki.miyukievents.bukkit.user;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.util.async.Async;
import com.google.common.collect.Maps;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {

    private final MiyukiEvents plugin;

    private final Map<UUID, User> users = Maps.newConcurrentMap();

    public UserRepository(MiyukiEvents plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> Async.run(this::cleanUp), 20L);
    }

    public Optional<User> findById(@NotNull UUID uniqueId) {
        return Optional.ofNullable(users.get(uniqueId));
    }

    public Optional<User> findByName(@NotNull String playerName) {
        val filteredUser = users.values()
                .stream()
                .filter(user -> user.getPlayerName().equalsIgnoreCase(playerName))
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(filteredUser);
    }

    public void save(@NotNull User user) {
        users.put(user.getUuid(), user);
    }

    public void remove(@NotNull UUID uniqueId) {
        users.remove(uniqueId);
    }

    public void cleanUp() {
        val gameManager = plugin.getGameManager();


        val removed = users.entrySet().removeIf(entries ->
                !entries.getValue().getPlayer().isPresent()
                        && gameManager.getQueue().isEmpty()
                        && gameManager.getCurrentGame() == null);

    }


}
