package app.miyuki.miyukievents.bukkit.storage;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.storage.datasource.DataSource;
import app.miyuki.miyukievents.bukkit.storage.datasource.MySQL;
import app.miyuki.miyukievents.bukkit.storage.datasource.SQLite;
import app.miyuki.miyukievents.bukkit.util.logger.LoggerHelper;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.logging.Level;

public class StorageFactory {

    public DataSource create(StorageType storageType, MiyukiEvents plugin) {

        try {

            switch (storageType) {
                case MYSQL:
                    return new MySQL(plugin.getGlobalConfig());
                case SQLITE:
                    return new SQLite(plugin);
                default:
                    LoggerHelper.log(Level.SEVERE, "An error occurred while attempting to initialize the " + storageType);
                    return null;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            LoggerHelper.log(Level.SEVERE, "An error occurred while attempting to initialize the " + storageType);
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        return null;
    }

}
