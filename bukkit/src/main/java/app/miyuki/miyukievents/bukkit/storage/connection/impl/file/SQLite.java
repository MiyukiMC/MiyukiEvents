package app.miyuki.miyukievents.bukkit.storage.connection.impl.file;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.dependency.Dependency;
import app.miyuki.miyukievents.bukkit.dependency.classloader.IsolatedClassLoader;
import app.miyuki.miyukievents.bukkit.storage.StorageType;
import app.miyuki.miyukievents.bukkit.storage.connection.ConnectionFactory;
import com.google.common.collect.ImmutableSet;
import lombok.SneakyThrows;
import lombok.val;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.Properties;

public class SQLite implements ConnectionFactory {

    private final NonClosableConnection connection;

    public SQLite(MiyukiEvents plugin, StorageType storageType) throws Exception {

        val file = plugin.getDataFolder().toPath().resolve("database.sqlite.db");

        String url = "jdbc:sqlite:" + file;

        Constructor<?> connectionConstructor;

        IsolatedClassLoader classLoader = plugin.getDependencyManager().getIsolatedClassLoader(ImmutableSet.of(Dependency.SQLITE_DRIVER));

        Class<?> connectionClass = classLoader.loadClass("org.sqlite.jdbc4.JDBC4Connection");
        connectionConstructor = connectionClass.getConstructor(String.class, String.class, Properties.class);

        Class.forName(storageType.getDriver());

        connection = new NonClosableConnection((Connection) connectionConstructor.newInstance(url, file.toString(), new Properties()));
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
