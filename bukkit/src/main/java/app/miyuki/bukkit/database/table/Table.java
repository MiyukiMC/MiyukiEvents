package app.miyuki.bukkit.database.table;

import app.miyuki.bukkit.database.Cacheable;

import java.util.List;
import java.util.Map;

public interface Table<K, V extends Cacheable<K>> {

    void create();

    void insert(V value);

    void update(V value);

    V get(K key);

    Map<K, V> getAll();

}
