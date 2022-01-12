package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.util.random.RandomUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

@RequiredArgsConstructor
public class Math implements Game<Player>, Chat {

    private final MiyukiEvents PLUGIN;

    private FileConfiguration config;

    private final List<Character> OPERATION_CHARS = Lists.newArrayList();

    private String permission;

    private Double cost;

    private String sumString;

    private Integer result;

    @Override
    public void onChat(AsyncPlayerChatEvent event) {

    }

    @Override
    public String getName() {
        return "Matemática";
    }

    @Override
    public void start() {
        this.config = PLUGIN.getConfig();

        config.getStringList("SumTypes").stream().map(sumType -> OPERATION_CHARS.add(sumType.charAt(0)));

        this.permission = config.getString("Permission");
        this.cost = config.getDouble("Cost");

        val min = config.getInt("MinNumber");
        val max = config.getInt("MaxNumber");

        val RANDOM_NUMBER_1 = RandomUtils.generateRandomNumber(min, max);
        val RANDOM_NUMBER_2 = RandomUtils.generateRandomNumber(min, max);

        switch (getRandomOperation()) {
            case '+':
                this.sumString = RANDOM_NUMBER_1 + " + " + RANDOM_NUMBER_2;
                this.result = RANDOM_NUMBER_1 + RANDOM_NUMBER_2;
                break;
            case '-':
                this.sumString = RANDOM_NUMBER_1 + " - " + RANDOM_NUMBER_2;
                this.result = RANDOM_NUMBER_1 - RANDOM_NUMBER_2;
                break;
            case '*':
                this.sumString = RANDOM_NUMBER_1 + " * " + RANDOM_NUMBER_2;
                this.result = RANDOM_NUMBER_1 * RANDOM_NUMBER_2;
                break;
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void onWin(Player player) {

    }

    @Override
    public void giveReward(Player player) {

    }

    public Character getRandomOperation() {
        return RandomUtils.getRandomElement(OPERATION_CHARS);
    }

}
