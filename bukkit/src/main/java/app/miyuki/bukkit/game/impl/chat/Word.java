package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class Word implements Game<Player>, Chat {

    private final MiyukiEvents PLUGIN;

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
