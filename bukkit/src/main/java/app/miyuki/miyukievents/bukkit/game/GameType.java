package app.miyuki.miyukievents.bukkit.game;

import app.miyuki.miyukievents.bukkit.commands.Command;
import app.miyuki.miyukievents.bukkit.commands.impl.chat.GenericChatCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.command.FastClickCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.command.GenericCommandCommand;
import app.miyuki.miyukievents.bukkit.commands.impl.inperson.teamdeathmatch.TeamDeathmatchCommand;
import app.miyuki.miyukievents.bukkit.game.chat.FastQuiz;
import app.miyuki.miyukievents.bukkit.game.chat.Math;
import app.miyuki.miyukievents.bukkit.game.chat.Word;
import app.miyuki.miyukievents.bukkit.game.command.FastClick;
import app.miyuki.miyukievents.bukkit.game.command.Jackpot;
import app.miyuki.miyukievents.bukkit.game.command.Lottery;
import app.miyuki.miyukievents.bukkit.game.command.Pool;
import app.miyuki.miyukievents.bukkit.game.inperson.impl.TeamDeathmatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

@AllArgsConstructor
@Getter
public enum GameType {

    WORD(Word.class, GenericChatCommand.class),
    MATH(Math.class, GenericChatCommand.class),
    LOTTERY(Lottery.class, GenericCommandCommand.class),
    POOL(Pool.class, GenericCommandCommand.class),
    FASTQUIZ(FastQuiz.class, GenericChatCommand.class),
    JACKPOT(Jackpot.class, GenericCommandCommand.class),
    FASTCLICK(FastClick.class, FastClickCommand.class),
    TEAMDEATHMATCH(TeamDeathmatch.class, TeamDeathmatchCommand.class);

    private final Class<? extends Game<?>> gameClass;
    private final Class<? extends Command> commandClass;

    public @Nullable Game<?> instantiate(@NotNull GameConfigProvider configProvider) {
        try {
            return gameClass.getConstructor(GameConfigProvider.class).newInstance(configProvider);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

}
