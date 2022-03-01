package app.miyuki.miyukievents.bukkit.game.chat;

import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Chat<W> extends Game<W> {

    public Chat(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    public abstract void onChat(Player player, String[] args);

}
