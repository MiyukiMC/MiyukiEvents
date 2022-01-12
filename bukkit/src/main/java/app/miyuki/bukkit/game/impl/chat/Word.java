package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.util.random.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class Word implements Game<Player>, Chat {

    private final MiyukiEvents PLUGIN;

    private FileConfiguration config;

    private String permission;

    private Double cost;

    private String word;

    @Override
    public void onChat(AsyncPlayerChatEvent event) {

    }

    @Override
    public String getName() {
        return "Palavra";
    }

    @Override
    public void start() {
        this.config = PLUGIN.getConfig(); // Só para testes, arrumar depois

        this.permission = config.getString("Permission");
        this.cost = config.getDouble("Cost");

        val calls = config.getInt("Calls");
        val callInterval = config.getInt("CallInterval");

        if (config.getBoolean("Words.Random.Enabled"))
            this.word = RandomUtils.generateRandomString(config.getString("Words.Random.Characters").toCharArray(), config.getInt("Words.Random.MaxCharacters"));
        else
            this.word = RandomUtils.getRandomElement(config.getStringList("Words.Words"));

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

}
