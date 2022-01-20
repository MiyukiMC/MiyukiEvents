package app.miyuki.bukkit.api.events;

import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.impl.chat.Lottery;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

@AllArgsConstructor
public class StartedGameEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Getter
    private Game game;

    @Getter
    private final Instant moment;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

}
