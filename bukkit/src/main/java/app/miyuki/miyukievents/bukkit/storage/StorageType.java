package app.miyuki.miyukievents.bukkit.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StorageType {

    MYSQL("com.mysql.cj.jdbc.Driver"),
    MARIADB("org.mariadb.jdbc.Driver"),
    H2("org.h2.Driver"),
    SQLITE("org.sqlite.JDBC");

    private final String driver;

    public static StorageType of(String name) {
        try {
            return StorageType.valueOf(name);
        } catch (NullPointerException exception) {
            return SQLITE;
        }
    }

}
