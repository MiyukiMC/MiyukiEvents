package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.util.RandomUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

@RequiredArgsConstructor
public class Lottery implements Game<Player>, Chat {

    private final MiyukiEvents PLUGIN;

    private final List<String> PLAYERS = Lists.newArrayList();

    private int number;

    @Override
    public void onChat(AsyncPlayerChatEvent event) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void start() {
        int min = 1;
        int max = 300;
        this.number = RandomUtils.generateRandomNumber(min, max);

    }

    @Override
    public void stop() {

    }

    @Override
    public void onWin(Player player) {

    }

}
