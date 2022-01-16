package app.miyuki.bukkit.game.repository;

import app.miyuki.bukkit.database.Cacheable;
import com.google.common.collect.Maps;

import java.util.Map;

public abstract class GameRepository<K, V extends Cacheable<K>> {

    protected final Map<K, V> cached = Maps.newHashMap();

    protected void add(V value) {
        cached.put(value.getKey(), value);
    }

    protected void remove(K key) {
        cached.remove(key);
    }

    protected boolean contains(K key) {
        return cached.containsKey(key);
    }

    protected V get(K key) {
        return cached.get(key);
    }

    protected Map<K, V> getAll() {
        return cached;
    }

}
