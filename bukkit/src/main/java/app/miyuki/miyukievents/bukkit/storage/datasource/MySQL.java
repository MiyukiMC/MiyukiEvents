package app.miyuki.miyukievents.bukkit.storage.datasource;

import app.miyuki.miyukievents.bukkit.config.Config;
import lombok.SneakyThrows;
import lombok.val;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL implements DataSource {

    private final Connection connection;

    public MySQL(Config config) throws SQLException {

        val section = config.getConfigurationSection("Database");

        val host = section.getString("Host");
        val port = section.getInt("Port");
        val database = section.getString("Database");
        val username = section.getString("Username");
        val password = section.getString("Password");

        val url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true";

        connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    @SneakyThrows
    public void close() {
        connection.close();
    }
}
