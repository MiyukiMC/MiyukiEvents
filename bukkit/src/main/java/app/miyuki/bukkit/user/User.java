package app.miyuki.bukkit.user;

import app.miyuki.bukkit.database.Cacheable;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;
import java.util.Map;

@Getter
public class User implements Cacheable<String> {

    private String playerName;

    @Setter
    private UserState userState;

    @Setter
    private String clanTag;

    private Map<String, Integer> wins = Maps.newHashMap();

    public User(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String getKey() {
        return playerName.toLowerCase(Locale.ROOT);
    }

}
