package app.miyuki.miyukievents.bukkit.storage;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.storage.datasource.DataSource;
import app.miyuki.miyukievents.bukkit.storage.tables.Users;
import lombok.Getter;
import lombok.val;

import java.util.Locale;

public class Storage {

    @Getter
    private final DataSource dataSource;

    @Getter
    private final Users users;

    public Storage(MiyukiEvents plugin) {

        val config = plugin.getGlobalConfig();
        val typeName = config.getString("Database.Type").toUpperCase(Locale.ROOT);

        val type = StorageType.of(typeName);

        dataSource = new StorageFactory().create(type, plugin);

        users = new Users(dataSource, type);

    }

}
