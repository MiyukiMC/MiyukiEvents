package app.miyuki.miyukievents.bukkit.storage.connection.impl.hikari;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.storage.StorageType;
import app.miyuki.miyukievents.bukkit.storage.connection.ConnectionFactory;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.val;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class MySQLAndMariaDB implements ConnectionFactory {

    private final HikariDataSource hikariDataSource;
    private final MiyukiEvents plugin;

    public MySQLAndMariaDB(MiyukiEvents plugin, StorageType storageType, CommentedConfigurationNode node) {
        this.plugin = plugin;

        val host = node.node("Host").getString() + ":" + node.node("Port").getString();
        val database = node.node("Database").getString();

        val url = "jdbc:" + storageType.name().toLowerCase(Locale.ROOT) + "://" + host + "/" + database;

        val hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(node.node("Username").getString());
        hikariConfig.setPassword(node.node("Password").getString());
        hikariConfig.setPoolName("MiyukiEvents-Pool");

        hikariConfig.setDriverClassName(storageType.getDriver());

        Map<String, String> properties = Maps.newHashMap();

        for (Map.Entry<Object, CommentedConfigurationNode> property : node.node("Properties").childrenMap().entrySet())
            properties.put((String) property.getKey(), property.getValue().getString());

        properties.putIfAbsent("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

        properties.forEach(hikariConfig::addDataSourceProperty);

        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            LoggerHelper.log(Level.SEVERE, "An error occurred while trying to initialize the database connection");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return null;
        }
    }

    @Override
    public void close() {
        try {
            hikariDataSource.close();
        } catch (Exception ignored) {
        }
    }

}
