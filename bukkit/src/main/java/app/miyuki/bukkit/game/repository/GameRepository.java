package app.miyuki.bukkit.game.repository;

import app.miyuki.bukkit.database.Cacheable;
import com.google.common.collect.Maps;

import java.util.Map;

public abstract class GameRepository<K, V extends Cacheable<K>> {

    protected final Map<K, V> CACHED = Maps.newHashMap();

    protected void add(V value) {
        CACHED.put(value.getKey(), value);
    }

    protected void remove(K key) {
        CACHED.remove(key);
    }

    protected boolean contains(K key) {
        return CACHED.containsKey(key);
    }

    protected V get(K key) {
        return CACHED.get(key);
    }

    protected Map<K, V> getAll() {
        return CACHED;
    }

}
