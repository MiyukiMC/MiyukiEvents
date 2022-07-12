package app.miyuki.miyukievents.bukkit.storage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum StorageType {

    MYSQL("MYSQL", "com.mysql.cj.jdbc.Driver", "MYSQL.sql"),
    MARIADB("MARIADB", "org.mariadb.jdbc.Driver", "MYSQL.sql"),
    H2("H2", "org.h2.Driver", "H2.sql"),
    SQLITE("SQLITE", "org.sqlite.JDBC", "SQLITE.sql");

    private final String name;
    private final String driver;
    private final String schema;

    public static StorageType of(@NotNull String name) {
        return Arrays.stream(values())
                .filter(storageType -> storageType.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(SQLITE);
    }

}
