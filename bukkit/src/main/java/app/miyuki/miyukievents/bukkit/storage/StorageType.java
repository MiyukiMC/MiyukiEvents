package app.miyuki.miyukievents.bukkit.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StorageType {

    MYSQL("com.mysql.cj.jdbc.Driver", "MYSQL.sql"),
    MARIADB("org.mariadb.jdbc.Driver", "MYSQL.sql"),
    H2("org.h2.Driver", "H2.sql"),
    SQLITE("org.sqlite.JDBC", "SQLITE.sql");

    private final String driver;
    private final String schema;

    public static StorageType of(String name) {
        try {
            return StorageType.valueOf(name);
        } catch (NullPointerException exception) {
            return SQLITE;
        }
    }

}
