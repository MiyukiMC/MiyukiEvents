package app.miyuki.bukkit.api.events;

import app.miyuki.bukkit.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class WinGameEvent extends Event {

    private final Game game;

    @Getter
    private WinnerType winnerType;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return null;

    }

    public enum WinnerType {

        PLAYER,
        CLAN;

    }

}
