package app.miyuki.miyukievents.bukkit.user;

import app.miyuki.miyukievents.bukkit.database.Cacheable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Data
public class User implements Cacheable<String> {

    private final UUID uuid;
    private final List<UserGameHistory> gameHistories;
    private final String playerName;

    @Setter
    private UserState userState;

    private User(String playerName, UUID uuid, UserState userState, List<UserGameHistory> gameHistories) {
        this.playerName = playerName;
        this.uuid = uuid;
        this.userState = userState;
        this.gameHistories = gameHistories;
    }


    @Override
    public String getKey() {
        return uuid.toString();
    }


}
