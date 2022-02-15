package app.miyuki.miyukievents.bukkit.storage.tables;

import app.miyuki.miyukievents.bukkit.storage.StorageType;
import app.miyuki.miyukievents.bukkit.storage.datasource.DataSource;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.user.UserGameHistory;
import app.miyuki.miyukievents.bukkit.user.UserState;
import com.google.common.collect.ImmutableMap;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Users extends Table<String, User> {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `miyukievents_user` (" +
            "uuid CHAR(36)," +
            "playername VARCHAR(60)," +
            "totalmoney TEXT," +
            "totalcash TEXT," +
            "PRIMARY KEY (uuid)) CHARSET=utf8 COLLATE utf8_bin;" +
            "CREATE TABLE IF NOT EXISTS `miyukievents_userhistory` (" +
            "uuid CHAR(36)," +
            "wins INT," +
            "defeats INT," +
            "kills INT," +
            "deaths INT," +
            "FOREIGN KEY (uuid) REFERENCES `miyukievents_user`(uuid)) CHARSET=utf8 COLLATE utf8_bin;" +
            "CREATE TABLE IF NOT EXISTS `miyukievents_userhistory` (" +
            "uuid CHAR(36)," +
            "game VARCHAR(255)," +
            "wins INT," +
            "defeats INT," +
            "kills INT," +
            "deaths INT," +
            "FOREIGN KEY (uuid) REFERENCES `miyukievents_user`(uuid)) CHARSET=utf8 COLLATE utf8_bin;";

    private static final Map<StorageType, String> USER_INSERT = ImmutableMap.of(
            StorageType.MYSQL,
            "INSERT INTO `miyukievents_user` (uuid,playername,totalmoney,totalcash) VALUES(?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "uuid=uuid, playername=playername, totalmoney=totalmoney, totalcash=totalcash",
            StorageType.SQLITE,
            "INSERT OR IGNORE INTO `miyukievents_user` (uuid,playername,totalmoney,totalcash) VALUES(?,?,?,?)"
    );

    private static final Map<StorageType, String> USER_HISTORY_INSERT = ImmutableMap.of(
            StorageType.MYSQL,
            "INSERT INTO `miyukievents_userhistory` (uuid,game,wins,defeats,kills,deaths) VALUES (?,?,?,?,?,?) " +
                    "ON DUPLICATE KEY UPDATE uuid=uuid, game=game, wins=wins, defeats=defeats, kills=kills, deaths=deaths",
            StorageType.SQLITE,
            "INSERT OR IGNORE INTO `miyukievents_userhistory` (uuid,game,wins,defeats,kills,deaths) VALUES (?,?,?,?,?,?)"
    );

    private static final String SELECT_USER = "SELECT * FROM `miyukievents_user` WHERE uuid = ? LIMIT 1";
    private static final String SELECT_HISTORY = "SELECT * FROM `miyukievents_userhistory` WHERE uuid = ?";


    private final StorageType storageType;

    public Users(DataSource dataSource, StorageType storageType) {
        super(dataSource);
        this.storageType = storageType;
    }


    @SneakyThrows
    @Override
    public void create() {
        val connection = dataSource.getConnection();
        @Cleanup val statement = connection.prepareStatement(CREATE_TABLE);
        statement.execute();
    }

    @SneakyThrows
    @Override
    public void update(@NotNull User value) {
        val connection = dataSource.getConnection();

        @Cleanup val historyStatement = connection.prepareStatement(USER_INSERT.get(storageType));
        @Cleanup val userStatement = connection.prepareStatement(USER_HISTORY_INSERT.get(storageType));

        for (UserGameHistory gameHistory : value.getGameHistories()) {

            historyStatement.clearParameters();

            historyStatement.setString(1, value.getKey());
            historyStatement.setString(2, gameHistory.getName());
            historyStatement.setInt(3, gameHistory.getWins());
            historyStatement.setInt(4, gameHistory.getDefeats());
            historyStatement.setInt(5, gameHistory.getKills());
            historyStatement.setInt(6, gameHistory.getDeaths());

            historyStatement.addBatch();

        }

        historyStatement.executeBatch();

        userStatement.setString(1, value.getKey());
        userStatement.setString(2, value.getPlayerName());
        userStatement.setString(3, value.getTotalMoney().toPlainString());
        userStatement.setString(4, value.getTotalCash().toPlainString());

        userStatement.executeUpdate();

    }

    @SneakyThrows
    @Override
    public @Nullable User get(@NotNull String key) {
        val connection = dataSource.getConnection();
        @Cleanup val statementHistory = connection.prepareStatement(SELECT_HISTORY);
        @Cleanup val statementUser = connection.prepareStatement(SELECT_USER);

        @Cleanup val resultHistory = statementHistory.getResultSet();

        List<UserGameHistory> userGameHistories = new ArrayList<>();

        while (resultHistory.next()) {

            val name = resultHistory.getString("game");
            val wins = resultHistory.getInt("wins");
            val defeats = resultHistory.getInt("defeats");
            val kills = resultHistory.getInt("kills");
            val deaths = resultHistory.getInt("deaths");

            userGameHistories.add(new UserGameHistory(name, wins, defeats, kills, deaths));

        }

        @Cleanup val resultUser = statementUser.getResultSet();

        if (resultUser.next()) {

            val uuid = UUID.fromString(resultUser.getString("uuid"));
            val playerName = resultUser.getString("playername");
            val totalMoney = new BigDecimal(resultUser.getString("totalmoney"));
            val totalCash = new BigDecimal(resultUser.getString("totalcash"));

            return new User(uuid, playerName, totalMoney, totalCash, UserState.FREE, userGameHistories);

        } else {
            return null;
        }
    }

    @Override
    public void delete(@NotNull String key) {

    }
}
