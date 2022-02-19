package app.miyuki.miyukievents.bukkit.hook.economy;

import java.math.BigDecimal;
import java.util.UUID;

public interface EconomyAPI {

    void deposit(UUID player, BigDecimal amount);

    boolean has(UUID player, BigDecimal amount);

    void withdraw(UUID player, BigDecimal amount);

}
