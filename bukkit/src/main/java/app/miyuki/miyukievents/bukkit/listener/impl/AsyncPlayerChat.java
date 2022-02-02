package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Chat;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@AllArgsConstructor
public class AsyncPlayerChat implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        val currentgame = plugin.getGameManager().getCurrentGame();

        if (!(currentgame instanceof Chat))
            return;

        ((Chat) currentgame).onChat(event.getPlayer(), event.getMessage().split(" ")[0]);
    }

}
