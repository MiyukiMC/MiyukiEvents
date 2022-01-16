package app.miyuki.bukkit.game.impl.chat;

import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.game.Chat;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.messages.MessageDispatcher;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class FastClick extends Game<Player> implements Chat {

    public FastClick(@NotNull ConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public void onChat(AsyncPlayerChatEvent event) {

    }

    @Override
    public String getTypeName() {
        return null;
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

    @Override
    public void giveReward(Player player) {

    }

}
