package app.miyuki.bukkit.listener;

import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.GameState;
import app.miyuki.bukkit.game.manager.GameManager;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@AllArgsConstructor
public class AsyncPlayerChat implements Listener {

    private final GameManager gameManager;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        gameManager.getGames().values()
                .stream().filter(game -> game.getState() == GameState.HAPPENING && game instanceof Chat)
                .forEach(game -> ((Chat) game).onChat(event));
    }

}
