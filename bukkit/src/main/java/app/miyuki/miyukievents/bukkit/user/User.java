package app.miyuki.miyukievents.bukkit.user;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.storage.Cacheable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Data
public class User implements Cacheable<String> {

    private final UUID uuid;
    private String playerName;
    private BigDecimal totalMoney;
    private BigDecimal totalCash;
    private UserState userState;
    private final List<UserGameHistory> gameHistories;

    @Override
    public String getKey() {
        return uuid.toString();
    }

    public CompletableFuture<Void> save() {

        val plugin = JavaPlugin.getPlugin(MiyukiEvents.class);



        return plugin.getStorage().updateUser(this);
    }

}
