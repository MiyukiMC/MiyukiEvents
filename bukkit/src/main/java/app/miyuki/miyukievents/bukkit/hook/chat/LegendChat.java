package app.miyuki.miyukievents.bukkit.hook.chat;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Chat;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameState;
import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class LegendChat implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerChat(ChatMessageEvent event) {
        val currentGame = plugin.getGameManager().getCurrentGame();

        if (!(currentGame instanceof Chat))
            return;

        Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> ((Chat) currentGame).onChat(event.getSender(), event.getMessage().split(" ")[0]),
                3L
        );
    }

}
