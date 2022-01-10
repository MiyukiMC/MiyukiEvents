package app.miyuki.bukkit.game;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface Chat {

    void onChat(AsyncPlayerChatEvent event);

}
