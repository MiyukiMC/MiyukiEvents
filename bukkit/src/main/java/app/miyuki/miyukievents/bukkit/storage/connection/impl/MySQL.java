package app.miyuki.miyukievents.bukkit.storage.connection.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class MySQL implements ConnectionFactory {

    private final HikariDataSource hikariDataSource;
    private final MiyukiEvents plugin;

    public MySQL(MiyukiEvents plugin, ConfigurationSection section) {
        this.plugin = plugin;

        String driver;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            driver = "com.mysql.cj.jdbc.Driver";
        } catch (ClassNotFoundException exception) {
            driver = "com.mysql.jdbc.Driver";
        }

        val url = "jdbc:mysql://" + section.getString("Host") + ":" + section.getInt("Port") + "/" + section.getString("Database");

        val hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(section.getString("Username"));
        hikariConfig.setPassword(section.getString("Password"));
        hikariConfig.setPoolName("MiyukiEvents-Pool");

        hikariConfig.setDriverClassName(driver);

        Map<String, String> properties = Maps.newHashMap();

        for (String property : section.getConfigurationSection("Properties").getKeys(false))
            properties.put(property, section.getString("Properties." + property));


        properties.putIfAbsent("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));

        for (Map.Entry<String, String> entry : properties.entrySet())
            hikariConfig.addDataSourceProperty(entry.getKey(), entry.getValue());

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
