package app.miyuki.miyukievents.bukkit.game.command;

import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Command<W> extends Game<W> {

    public Command(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    public abstract void onCommand(Player player, String[] args);

}
