package app.miyuki.miyukievents.bukkit.storage.datasource;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite implements DataSource {

    private final Connection connection;

    public SQLite(MiyukiEvents plugin) throws ClassNotFoundException, SQLException {
        val url = "jdbc:sqlite:" + new File(plugin.getDataFolder(), "database.sqlite.db");
        Class.forName("org.sqlite.JDBC");

        connection = DriverManager.getConnection(url);
    }

    @Override
    public Connection getConnection() {
        return  connection;
    }

    @SneakyThrows
    @Override
    public void close() {
        connection.close();
    }

}
