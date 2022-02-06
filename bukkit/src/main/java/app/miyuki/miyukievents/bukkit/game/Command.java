package app.miyuki.miyukievents.bukkit.game;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Command<W> extends Game<W> {

    public Command(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    public abstract void onCommand(Player player, String[] args);

}
