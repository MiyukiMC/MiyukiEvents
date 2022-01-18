package app.miyuki.bukkit.listener.impl;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.GameState;
import app.miyuki.bukkit.game.InPerson;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerQuit implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (Game game : plugin.getGameManager().getGames().values()) {

            if (game.getGameState() == GameState.HAPPENING && game instanceof InPerson) {
                ((InPerson) game).onPlayerQuit(event);
            }

        }
    }

}
