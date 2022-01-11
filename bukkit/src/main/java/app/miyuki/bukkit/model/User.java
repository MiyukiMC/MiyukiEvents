package app.miyuki.bukkit.model;

import app.miyuki.bukkit.database.Cacheable;

import java.util.Locale;
import java.util.Map;

public class User implements Cacheable<String> {

    private String playerName;
    private Map<String, Integer> wins;

    @Override
    public String getKey() {
        return playerName.toLowerCase(Locale.ROOT);
    }

}
