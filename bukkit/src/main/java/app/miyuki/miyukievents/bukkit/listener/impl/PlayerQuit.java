package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.InPerson;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
public class PlayerQuit implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        val game = plugin.getGameManager().getCurrentGame();

        if (!(game instanceof InPerson))
            return;

        ((InPerson) game).onPlayerQuit(event);
    }

}
