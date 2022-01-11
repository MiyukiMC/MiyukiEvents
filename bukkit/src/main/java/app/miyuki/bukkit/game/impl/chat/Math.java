package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class Math implements Game<Player>, Chat {

    private final MiyukiEvents PLUGIN;

    private final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private final List<Character> OPERATION_CHARS = Lists.newArrayList();

    {
        OPERATION_CHARS.add('+');
        OPERATION_CHARS.add('-');
        OPERATION_CHARS.add('*');
    }

    private String sumString;

    private int result;

    @Override
    public void onChat(AsyncPlayerChatEvent event) {
        if (Integer.parseInt(event.getMessage()) == this.result)
            onWin(event.getPlayer());
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void start() {
        int numberOne = RANDOM.nextInt(10000);
        int numberTwo = RANDOM.nextInt(10000);

        switch (getRandomOperation()) {
            case '+':
                this.sumString = numberOne + " + " + numberTwo;
                this.result = numberOne + numberTwo;
                break;
            case '-':
                this.sumString = numberOne + " - " + numberTwo;
                this.result = numberOne - numberTwo;
                break;
            case '*':
                this.sumString = numberOne + " * " + numberTwo;
                this.result = numberOne * numberTwo;
                break;
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void onWin(Player player) {

    }

    public Character getRandomOperation() {
        return OPERATION_CHARS.get(RANDOM.nextInt(OPERATION_CHARS.size()));
    }

}
