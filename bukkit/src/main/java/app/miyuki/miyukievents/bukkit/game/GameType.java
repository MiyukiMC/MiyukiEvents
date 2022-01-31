package app.miyuki.miyukievents.bukkit.game;

import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.chat.WordCommand;
import app.miyuki.miyukievents.bukkit.game.impl.chat.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

@AllArgsConstructor
@Getter
public enum GameType  {

    WORD(Word.class, WordCommand.class);

    private final Class<? extends Game> gameClass;
    private final Class<? extends Command> commandClass;

    public @Nullable Game instantiate(@NotNull GameConfigProvider configProvider) {
        try {
            return gameClass.getConstructor(GameConfigProvider.class).newInstance(configProvider);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

}