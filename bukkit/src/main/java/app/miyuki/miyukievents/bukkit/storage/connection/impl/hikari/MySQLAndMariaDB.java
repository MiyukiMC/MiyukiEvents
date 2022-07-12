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
import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class MySQLAndMariaDB implements ConnectionFactory {

    private final HikariDataSource hikariDataSource;
    private final MiyukiEvents plugin;

    public MySQLAndMariaDB(MiyukiEvents plugin, StorageType storageType, ConfigurationSection section) {
        this.plugin = plugin;

        val host = section.getString("Host") + ":" + section.getInt("Port");
        val database = section.getString("Database");

        val url = "jdbc:" + storageType.name().toLowerCase(Locale.ROOT) + "://" + host + "/" + database;

        val hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(section.getString("Username"));
        hikariConfig.setPassword(section.getString("Password"));
        hikariConfig.setPoolName("MiyukiEvents-Pool");

        hikariConfig.setDriverClassName(storageType.getDriver());

        Map<String, String> properties = Maps.newHashMap();

        for (val property : section.getConfigurationSection("Properties").getKeys(false))
            properties.put(property, section.getString("Properties." + property));

        properties.putIfAbsent("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

        properties.forEach(hikariConfig::addDataSourceProperty);

        hikariDataSource = new HikariDataSource(hikariConfig);
    }

    @Override
    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            // adapt to language system.. (pt-br, en-us) (custom message)
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
