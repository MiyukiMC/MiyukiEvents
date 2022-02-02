package app.miyuki.miyukievents.bukkit.hook.chat;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import br.net.fabiozumbi12.UltimateChat.Bukkit.API.SendChannelMessageEvent;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class UltimateChat implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerChat(SendChannelMessageEvent event) {

    }

}
