package app.miyuki.miyukievents.bukkit.storage.connection.impl.file;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.storage.StorageType;
import app.miyuki.miyukievents.bukkit.storage.connection.ConnectionFactory;
import lombok.SneakyThrows;
import lombok.val;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2 implements ConnectionFactory {

    private final NonClosableConnection connection;

    public H2(MiyukiEvents plugin, StorageType storageType) throws ClassNotFoundException, SQLException {

        val pluginFolderPath = plugin.getDataFolder().toPath();

        String url = "jdbc:h2:./" + pluginFolderPath.resolve("database");

        Class.forName(storageType.getDriver());

        connection = new NonClosableConnection(DriverManager.getConnection(url));
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @SneakyThrows
    @Override
    public void close() {
        connection.shutdown();
    }


}
