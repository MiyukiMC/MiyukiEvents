package app.miyuki.miyukievents.bukkit.hook.chat;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import com.nickuc.chat.api.events.PublicMessageEvent;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class NChat implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerChat(PublicMessageEvent event) {

    }

}
