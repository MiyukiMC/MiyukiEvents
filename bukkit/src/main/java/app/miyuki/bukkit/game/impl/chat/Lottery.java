package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.config.ConfigType;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class Lottery extends Game<Player> implements Chat {

    public Lottery(@NotNull ConfigProvider configProvider) {
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
    public GameState getState() {
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

    @Override
    public void giveReward(Player player) {

    }

}
