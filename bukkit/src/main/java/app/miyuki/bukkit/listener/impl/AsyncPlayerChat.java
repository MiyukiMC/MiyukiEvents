package app.miyuki.bukkit.listener.impl;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.GameState;
import app.miyuki.bukkit.game.manager.GameManager;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@AllArgsConstructor
public class AsyncPlayerChat implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        for (Game game : plugin.getGameManager().getGames().values()) {

            if (game.getState() == GameState.HAPPENING && game instanceof Chat) {
                ((Chat) game).onChat(event);
            }

        }
    }

}
