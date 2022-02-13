package app.miyuki.miyukievents.bukkit.api.events;

import app.miyuki.miyukievents.bukkit.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class StartedGameEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Getter
    private Game<?> game;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
