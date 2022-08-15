package app.miyuki.miyukievents.bukkit.game.command;

import app.miyuki.miyukievents.bukkit.config.Config;
import app.miyuki.miyukievents.bukkit.game.Game;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Command<W> extends Game<W> {


    public Command(@NotNull Config config, @NotNull Config messages, @NotNull Config data) {
        super(config, messages, data);
    }

    public abstract void onCommand(Player player, String[] args);

}
