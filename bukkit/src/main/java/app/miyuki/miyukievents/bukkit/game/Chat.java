package app.miyuki.miyukievents.bukkit.game;

import org.bukkit.event.player.AsyncPlayerChatEvent;

public interface Chat<T> {

    void onChat(T t, String message);

}
