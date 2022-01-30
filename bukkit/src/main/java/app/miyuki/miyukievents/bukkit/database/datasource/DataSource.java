package app.miyuki.miyukievents.bukkit.database.datasource;

import java.sql.Connection;

public interface DataSource {

    Connection getConnection();

    void close();

}
