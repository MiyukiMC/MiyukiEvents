package app.miyuki.miyukievents.bukkit.user;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UserFactory {

    private final MiyukiEvents plugin;

    public User create(@NotNull UUID uuid, @Nullable String playerName) {
        List<UserGameHistory> userGameHistories = new ArrayList<>();

        for (String game : plugin.getGameManager().getGames().keySet())
            userGameHistories.add(new UserGameHistory(game, 0, 0, 0, 0));

        return new User(uuid, playerName, BigDecimal.valueOf(0), BigDecimal.valueOf(0), UserState.FREE, userGameHistories);
    }

}
