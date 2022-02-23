package app.miyuki.miyukievents.bukkit.storage;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.storage.connection.ConnectionFactory;
import app.miyuki.miyukievents.bukkit.storage.connection.impl.file.SQLiteAndH2;
import app.miyuki.miyukievents.bukkit.storage.connection.impl.hikari.MySQLAndMariaDB;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Level;

@AllArgsConstructor
public class StorageFactory {

    private MiyukiEvents plugin;

    public Storage create() {

        val config = plugin.getGlobalConfig();
        val section = config.getConfigurationSection("Database");
        val typeName = section.getString("Type").toUpperCase(Locale.ROOT);

        val storageType = StorageType.of(typeName);

        val dependencyManager = plugin.getDependencyManager();
        dependencyManager.loadDependencies(dependencyManager.getDependencyRegistry().getStorageDependencies(storageType));

        ConnectionFactory connectionFactory;
        try {

            switch (storageType) {
                case MYSQL:
                case MARIADB:
                    connectionFactory = new MySQLAndMariaDB(plugin, storageType, section);
                    break;
                case SQLITE:
                case H2:
                    connectionFactory = new SQLiteAndH2(plugin, storageType);
                    break;
                default:
                    LoggerHelper.log(Level.SEVERE, "An error occurred while attempting to initialize the " + storageType);
                    return null;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            LoggerHelper.log(Level.SEVERE, "An error occurred while attempting to initialize the " + storageType);
            Bukkit.getPluginManager().disablePlugin(plugin);
            return null;
        }

        return new Storage(plugin, connectionFactory, storageType);
    }

}
