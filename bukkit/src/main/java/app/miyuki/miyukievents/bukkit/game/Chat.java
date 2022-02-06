package app.miyuki.miyukievents.bukkit.game;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Chat<W> extends Game<W> {

    public Chat(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    public abstract void onChat(Player player, String[] args);

}
