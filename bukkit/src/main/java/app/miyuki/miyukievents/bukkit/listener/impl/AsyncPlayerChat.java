package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.GameState;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@AllArgsConstructor
public class AsyncPlayerChat implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        val currentGame = plugin.getGameManager().getCurrentGame();

        if (!(currentGame instanceof Chat))
            return;

        Bukkit.getScheduler().runTask(plugin, () -> {
            if (currentGame.getGameState() == GameState.HAPPENING)
                ((Chat) currentGame).onChat(event.getPlayer(), event.getMessage().split(" ")[0]);
        });
    }

}
