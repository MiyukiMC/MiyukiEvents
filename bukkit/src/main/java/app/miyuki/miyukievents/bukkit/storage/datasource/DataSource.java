package app.miyuki.miyukievents.bukkit.storage.datasource;

import java.sql.Connection;

public interface DataSource {

    Connection getConnection();

    void close();

}
