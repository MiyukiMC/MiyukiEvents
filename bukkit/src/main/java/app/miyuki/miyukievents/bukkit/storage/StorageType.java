package app.miyuki.miyukievents.bukkit.storage;

public enum StorageType {

    MYSQL, SQLITE;

    public static StorageType of(String name) {
        try {
            return StorageType.valueOf(name);
        } catch (NullPointerException exception) {
            return SQLITE;
        }
    }

}
