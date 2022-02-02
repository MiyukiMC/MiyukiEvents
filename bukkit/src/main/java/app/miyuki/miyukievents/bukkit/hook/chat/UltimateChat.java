package app.miyuki.miyukievents.bukkit.hook.chat;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Chat;
import br.net.fabiozumbi12.UltimateChat.Bukkit.API.SendChannelMessageEvent;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class UltimateChat implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onPlayerChat(SendChannelMessageEvent event) {

        val sender = event.getSender();

        if (!(sender instanceof Player))
            return;

        val currentgame = plugin.getGameManager().getCurrentGame();

        if (!(currentgame instanceof Chat))
            return;

        ((Chat) currentgame).onChat((Player) sender, event.getMessage().split(" ")[0]);
    }

}
