package app.miyuki.miyukievents.bukkit.storage.tables;

import app.miyuki.miyukievents.bukkit.storage.Cacheable;
import app.miyuki.miyukievents.bukkit.storage.datasource.DataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Table<K, V extends Cacheable<K>> {

    protected final DataSource dataSource;

    public Table(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public abstract void create();

    public abstract void update(@NotNull V value);

    public abstract @Nullable V get(@NotNull K key);

    public abstract void delete(@NotNull K key);

}
