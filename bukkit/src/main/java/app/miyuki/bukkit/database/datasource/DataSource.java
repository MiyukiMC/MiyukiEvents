package app.miyuki.bukkit.database.datasource;

import java.sql.Connection;

public interface DataSource {

    Connection getConnection();

    void close();

}
