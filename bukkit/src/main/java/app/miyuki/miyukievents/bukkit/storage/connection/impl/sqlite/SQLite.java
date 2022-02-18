package app.miyuki.miyukievents.bukkit.storage.connection.impl.sqlite;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.storage.connection.ConnectionFactory;
import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite implements ConnectionFactory {

    private final NonClosableConnection connection;

    public SQLite(MiyukiEvents plugin) throws ClassNotFoundException, SQLException {

        val url = "jdbc:sqlite:" + new File(plugin.getDataFolder(), "database.sqlite.db");
        Class.forName("org.sqlite.JDBC");

        connection = new NonClosableConnection(DriverManager.getConnection(url));
    }

    @Override
    public Connection getConnection() {
        return  connection;
    }

    @SneakyThrows
    @Override
    public void close() {
        connection.shutdown();
    }

}
