package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Giveaway implements Game<Player>, Chat {

    @Override
    public void onChat(AsyncPlayerChatEvent event) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onWin(Player player) {

    }
}
