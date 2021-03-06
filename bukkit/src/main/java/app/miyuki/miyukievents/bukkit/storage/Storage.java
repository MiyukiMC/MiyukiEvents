package app.miyuki.miyukievents.bukkit.storage;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.adapter.impl.UserGameHistoryAdapter;
import app.miyuki.miyukievents.bukkit.storage.connection.ConnectionFactory;
import app.miyuki.miyukievents.bukkit.user.User;
import app.miyuki.miyukievents.bukkit.user.UserGameHistory;
import app.miyuki.miyukievents.bukkit.user.UserState;
import app.miyuki.miyukievents.bukkit.util.async.Async;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import com.google.common.collect.ImmutableMap;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class Storage {

    private static final String DEFAULT_USER_INSERT = "INSERT INTO miyukievents_user " +
            "(uuid, playername, totalmoney, totalcash) VALUES(?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "uuid=uuid, playername=playername, totalmoney=totalmoney, totalcash=totalcash";

    private static final Map<StorageType, String> USER_INSERT = ImmutableMap.of(
            StorageType.H2,
            "MERGE INTO miyukievents_user (uuid, playername, totalmoney, totalcash) " +
                    "KEY (uuid, playername, totalmoney, totalcash) " +
                    "VALUES (?, ?, ?, ?)",
            StorageType.SQLITE,
            "INSERT OR IGNORE INTO miyukievents_user (uuid, playername,totalmoney, totalcash) VALUES(?, ?, ?, ?)"
    );

    private static final String DEFAULT_USER_HISTORY_INSERT = "INSERT INTO miyukievents_userhistory " +
            "(uuid, game, wins, defeats, kills, deaths) VALUES (?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "uuid=uuid, game=game, wins=wins, defeats=defeats, kills=kills, deaths=deaths";

    private static final Map<StorageType, String> USER_HISTORY_INSERT = ImmutableMap.of(
            StorageType.H2,
            "MERGE INTO miyukievents_userhistory (uuid, game, wins, defeats, kills, deaths) " +
                    "KEY (uuid, game, wins, defeats, kills, deaths) " +
                    "VALUES (?,?,?,?,?,?)",
            StorageType.SQLITE,
            "INSERT OR IGNORE INTO miyukievents_userhistory (uuid, game, wins, defeats, kills, deaths) VALUES (?, ?, ?, ?, ?, ?)"
    );

    private static final String SELECT_USER = "SELECT * FROM miyukievents_user WHERE uuid = ? LIMIT 1";
    private static final String SELECT_HISTORY = "SELECT * FROM miyukievents_userhistory WHERE uuid = ?";

    private static final String DELETE_HISTORY = "DELETE FROM miyukievents_userhistory WHERE game = ?";

    private final MiyukiEvents plugin;
    private final ConnectionFactory connectionFactory;
    private final StorageType storageType;

    private final UserGameHistoryAdapter userGameHistoryAdapter;

    public Storage(MiyukiEvents plugin, ConnectionFactory connectionFactory, StorageType storageType) {
        this.plugin = plugin;
        this.connectionFactory = connectionFactory;
        this.storageType = storageType;
        this.userGameHistoryAdapter = plugin.getUserGameHistoryAdapter();
    }

    /*
    Inspired by lucko: https://github.com/LuckPerms/LuckPerms/blob/master/common/src/main/java/me/lucko/luckperms/common/storage/implementation/sql/SchemaReader.java
     */
    public CompletableFuture<Void> createTables() {
        return Async.run(() -> {
            try {
                @Cleanup val connection = connectionFactory.getConnection();
                @Cleanup val statement = connection.createStatement();

                @Cleanup val inputStream = plugin.getResource("schema/" + storageType.name().toUpperCase(Locale.ROOT) + ".sql");

                if (inputStream == null) {
                    LoggerHelper.log(Level.SEVERE, "An error occurred while trying to create the database tables");
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    return;
                }

                @Cleanup val reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                var stringBuilder = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {

                    stringBuilder.append(line);

                    if (line.endsWith(";")) {

                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

                        val result = stringBuilder.toString().trim();

                        if (!result.isEmpty())
                            statement.addBatch(result);


                        stringBuilder = new StringBuilder();
                    }

                }

                statement.executeBatch();

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    @SneakyThrows
    public CompletableFuture<Void> updateUser(@NotNull User user) {
        return Async.run(() -> {
            try {
                @Cleanup val connection = connectionFactory.getConnection();

                @Cleanup val userStatement = connection.prepareStatement(USER_INSERT.getOrDefault(storageType, DEFAULT_USER_INSERT));
                @Cleanup val historyStatement = connection.prepareStatement(USER_HISTORY_INSERT.getOrDefault(storageType, DEFAULT_USER_HISTORY_INSERT));

                for (UserGameHistory gameHistory : user.getGameHistories()) {

                    historyStatement.setString(1, user.getUuid().toString());
                    historyStatement.setString(2, gameHistory.getGameName());
                    historyStatement.setInt(3, gameHistory.getWins());
                    historyStatement.setInt(4, gameHistory.getDefeats());
                    historyStatement.setInt(5, gameHistory.getKills());
                    historyStatement.setInt(6, gameHistory.getDeaths());

                    historyStatement.addBatch();

                }

                userStatement.setString(1, user.getUuid().toString());
                userStatement.setString(2, user.getPlayerName());
                userStatement.setString(3, user.getTotalMoney().toPlainString());
                userStatement.setString(4, user.getTotalCash().toPlainString());

                userStatement.addBatch();

                userStatement.executeUpdate();
                historyStatement.executeBatch();

            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    @SneakyThrows
    public CompletableFuture<Optional<User>> getUser(@NotNull UUID uniqueId) {
        return Async.run(() -> {
            try {
                @Cleanup val connection = connectionFactory.getConnection();
                @Cleanup val statementHistory = connection.prepareStatement(SELECT_HISTORY);
                @Cleanup val statementUser = connection.prepareStatement(SELECT_USER);

                statementUser.setString(1, uniqueId.toString());
                statementHistory.setString(1, uniqueId.toString());

                @Cleanup val resultHistory = statementHistory.executeQuery();

                final List<UserGameHistory> userGameHistories = new ArrayList<>();

                while (resultHistory.next())
                    userGameHistories.add(userGameHistoryAdapter.adapt(resultHistory));

                @Cleanup val resultUser = statementUser.executeQuery();

                if (resultUser.next()) {

                    val uuid = UUID.fromString(resultUser.getString("uuid"));
                    val playerName = resultUser.getString("playername");
                    val totalMoney = new BigDecimal(resultUser.getString("totalmoney"));
                    val totalCash = new BigDecimal(resultUser.getString("totalcash"));

                    return Optional.of(new User(uuid, playerName, totalMoney, totalCash, UserState.FREE, userGameHistories));

                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            return Optional.empty();
        });

    }

    public CompletableFuture<Void> deleteHistory(@NotNull String... games) {
        return Async.run(() -> {
            try {

                @Cleanup val connection = connectionFactory.getConnection();
                @Cleanup val statement = connection.prepareStatement(DELETE_HISTORY);

                for (String game : games) {

                    statement.clearParameters();
                    statement.setString(1, game);
                    statement.addBatch();

                }

                statement.executeBatch();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void shutdown() {
        this.connectionFactory.close();
    }

}
