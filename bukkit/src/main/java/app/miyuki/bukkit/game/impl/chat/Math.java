package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.config.ConfigType;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.GameState;
import app.miyuki.bukkit.util.random.RandomUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Math extends Game<Player> implements Chat {

    public Math(@NotNull ConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onChat(AsyncPlayerChatEvent event) {

    }

    @Override
    public String getTypeName() {
        return getConfigProvider().provide(ConfigType.CONFIG).getString("Type");
    }

    @Override
    public String getName() {
        return getConfigProvider().provide(ConfigType.CONFIG).getString("Name");
    }

    @Override
    public String getPermission() {
        return getConfigProvider().provide(ConfigType.CONFIG).getString("Permission");
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onWin(Player player) {
        giveReward(player);
    }

    @Override
    public void giveReward(Player player) {
        this.reward.execute(player);
    }

}
