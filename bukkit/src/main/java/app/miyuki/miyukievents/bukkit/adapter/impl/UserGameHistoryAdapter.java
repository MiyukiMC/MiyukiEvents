package app.miyuki.miyukievents.bukkit.adapter.impl;

import app.miyuki.miyukievents.bukkit.adapter.Adapter;
import app.miyuki.miyukievents.bukkit.user.UserGameHistory;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserGameHistoryAdapter implements Adapter<UserGameHistory, ResultSet> {

    @Override
    @Nullable
    public UserGameHistory adapt(@NotNull ResultSet resultSet) {
        try {
            val name = resultSet.getString("game");
            val wins = resultSet.getInt("wins");
            val defeats = resultSet.getInt("defeats");
            val kills = resultSet.getInt("kills");
            val deaths = resultSet.getInt("deaths");

            return new UserGameHistory(name, wins, defeats, kills, deaths);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
