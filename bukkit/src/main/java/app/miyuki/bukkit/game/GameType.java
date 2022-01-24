package app.miyuki.bukkit.game;

import app.miyuki.bukkit.config.GameConfigProvider;
import app.miyuki.bukkit.game.impl.chat.FastClick;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

@AllArgsConstructor
public enum GameType  {

    FAST_CLICK(FastClick.class);

    private final Class<? extends Game> gameClass;

    public @Nullable Game instantiate(@NotNull GameConfigProvider configProvider) {
        try {
            return gameClass.getConstructor(GameConfigProvider.class).newInstance(configProvider);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

}
