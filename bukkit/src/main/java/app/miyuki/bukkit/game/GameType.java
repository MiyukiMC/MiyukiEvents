package app.miyuki.bukkit.game;

import app.miyuki.bukkit.config.ConfigProvider;
import app.miyuki.bukkit.game.impl.chat.FastClick;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

@AllArgsConstructor
public enum GameType  {

    FAST_CLICK(FastClick.class);

    private final Class<? extends Game> gameClass;

    public @Nullable Game instantiate(@NotNull ConfigProvider configProvider) {
        try {
            return gameClass.getConstructor(ConfigProvider.class).newInstance(configProvider);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

}
