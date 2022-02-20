package app.miyuki.miyukievents.bukkit.user;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
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
public class User {

    private final UUID uuid;
    private String playerName;
    private BigDecimal totalMoney;
    private BigDecimal totalCash;
    private UserState userState;
    private final List<UserGameHistory> gameHistories;

    public CompletableFuture<Void> save() {
        val plugin = JavaPlugin.getPlugin(MiyukiEvents.class);
        plugin.getUserRepository().save(this);
        return plugin.getStorage().updateUser(this);
    }

}
