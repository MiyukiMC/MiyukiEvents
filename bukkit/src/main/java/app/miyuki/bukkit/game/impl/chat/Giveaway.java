package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.config.GameConfigProvider;
import app.miyuki.bukkit.config.ConfigType;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class Giveaway extends Game<Player> implements Chat {

    public Giveaway(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onChat(AsyncPlayerChatEvent event) {

    }

    @Override
    public String getTypeName() {
        return configProvider.provide(ConfigType.CONFIG).getString("Type");
    }

    @Override
    public String getName() {
        return configProvider.provide(ConfigType.CONFIG).getString("Name");
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

    }

}
