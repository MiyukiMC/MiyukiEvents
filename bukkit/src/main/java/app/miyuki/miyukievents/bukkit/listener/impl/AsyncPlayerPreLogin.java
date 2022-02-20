package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.user.UserFactory;
import app.miyuki.miyukievents.bukkit.user.UserGameHistory;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.stream.Collectors;

public class AsyncPlayerPreLogin implements Listener {

    private final MiyukiEvents plugin;
    private final UserFactory userFactory;

    public AsyncPlayerPreLogin(MiyukiEvents plugin) {
        this.plugin = plugin;
        userFactory = new UserFactory(plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;

        val uuid = event.getUniqueId();
        val playerName = event.getName();

        val storage = plugin.getStorage();

        storage.getUser(uuid.toString()).thenAccept(user -> {
            val finalUser = user.orElseGet(() -> userFactory.create(uuid, playerName));

            if (!finalUser.getPlayerName().equals(playerName)) {
                finalUser.setPlayerName(playerName);
            }

            val games = plugin.getGameManager().getGames().keySet();
            val userGameHistories = finalUser.getGameHistories();

            val toRemove = userGameHistories.stream().map(UserGameHistory::getGameName).filter(gameName -> !games.contains(gameName)).toArray(String[]::new);
            val toAdd = games.stream().filter(game -> userGameHistories.stream().noneMatch(history -> history.getGameName().equals(game))).collect(Collectors.toList());

            storage.deleteHistory(toRemove);

            toAdd.forEach(game -> userGameHistories.add(new UserGameHistory(game, 0, 0, 0, 0)));

            finalUser.save();
        });
    }

}
