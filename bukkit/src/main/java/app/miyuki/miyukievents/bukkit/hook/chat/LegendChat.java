package app.miyuki.miyukievents.bukkit.hook.chat;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class LegendChat implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerChat(ChatMessageEvent event) {
        if (event.getMessage().isEmpty())
            return;

        for (Game game : plugin.getGameManager().getGames().values()) {

            if (game.getGameState() == GameState.HAPPENING && game instanceof Chat) {
                ((Chat) game).onChat(event.getSender(), event.getMessage().split(" ")[0]);
            }

        }
    }

}
