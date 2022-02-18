package app.miyuki.miyukievents.bukkit.storage.connection;

import java.sql.Connection;

public interface ConnectionFactory {
    
    Connection getConnection();

    void close();

}
