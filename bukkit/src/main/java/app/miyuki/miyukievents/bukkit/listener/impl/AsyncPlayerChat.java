package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import lombok.AllArgsConstructor;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class AsyncPlayerChat implements Listener {

    private final MiyukiEvents plugin;

//    @EventHandler
//    public void onPlayerChat(AsyncPlayerChatEvent event) {
//        for (Game game : plugin.getGameManager().getGames().values()) {
//
//            if (game.getGameState() == GameState.HAPPENING && game instanceof Chat) {
//                ((Chat) game).onChat(event);
//            }
//
//        }
//    }

}
