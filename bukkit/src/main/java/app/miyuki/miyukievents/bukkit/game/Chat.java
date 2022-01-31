package app.miyuki.miyukievents.bukkit.game;

import org.bukkit.entity.Player;

public interface Chat<T> {

    void onChat(Player player, String message);

}
