package app.miyuki.miyukievents.bukkit.database.table;

import app.miyuki.miyukievents.bukkit.database.Cacheable;

import java.util.Map;

public interface Table<K, V extends Cacheable<K>> {

    void create();

    void insert(V value);

    void update(V value);

    V get(K key);

    Map<K, V> getAll();

}
