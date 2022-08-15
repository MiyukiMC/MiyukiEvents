package app.miyuki.miyukievents.bukkit.game.chat;

import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.Game;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Chat<W> extends Game<W> {


    public Chat(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
        super(config, messages, data);
    }

    public abstract void onChat(Player player, String[] args);

}
