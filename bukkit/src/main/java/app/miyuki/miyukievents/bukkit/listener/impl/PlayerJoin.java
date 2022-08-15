package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.user.UserFactory;
import app.miyuki.miyukievents.bukkit.user.UserGameHistory;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.stream.Collectors;

public class PlayerJoin implements Listener {

    private final MiyukiEvents plugin;
    private final UserFactory userFactory;

    public PlayerJoin(MiyukiEvents plugin) {
        this.plugin = plugin;
        this.userFactory = new UserFactory(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();

        val uniqueId = player.getUniqueId();
        val playerName = player.getName();

        val storage = plugin.getStorage();

        storage.getUser(uniqueId).thenAcceptAsync(user -> {
            val finalUser = user.orElseGet(() -> userFactory.create(uniqueId, playerName));

            if (!finalUser.getPlayerName().equals(playerName)) {
                finalUser.setPlayerName(playerName);
            }

            val games = plugin.getGameManager().getGames().keySet();
            val userGameHistories = finalUser.getGameHistories();

            val toRemove = userGameHistories.stream()
                    .map(UserGameHistory::getGameName).
                    filter(gameName -> !games.contains(gameName)).toArray(String[]::new);

            val toAdd = games.stream()
                    .filter(game -> userGameHistories.stream().noneMatch(history -> history.getGameName().equals(game)))
                    .collect(Collectors.toList());

            storage.deleteHistory(toRemove);

            toAdd.forEach(game -> userGameHistories.add(new UserGameHistory(game, 0, 0, 0, 0)));

            finalUser.save();
        }).handle((value, exception) -> {
            exception.printStackTrace();
            return null;
        });


    }


}
