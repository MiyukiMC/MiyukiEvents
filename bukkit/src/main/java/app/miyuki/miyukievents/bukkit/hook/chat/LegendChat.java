package app.miyuki.miyukievents.bukkit.hook.chat;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class LegendChat implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerChat(ChatMessageEvent event) {
        val currentgame = plugin.getGameManager().getCurrentGame();

        if (!(currentgame instanceof Chat))
            return;

        ((Chat) currentgame).onChat(event.getSender(), event.getMessage().split(" ")[0]);
    }

}
