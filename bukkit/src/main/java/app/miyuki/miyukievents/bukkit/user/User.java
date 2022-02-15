package app.miyuki.miyukievents.bukkit.user;

import app.miyuki.miyukievents.bukkit.storage.Cacheable;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
public class User implements Cacheable<String> {

    private final UUID uuid;
    private final String playerName;
    private BigDecimal totalMoney;
    private BigDecimal totalCash;
    private UserState userState;
    private final List<UserGameHistory> gameHistories;

    @Override
    public String getKey() {
        return uuid.toString();
    }


}
