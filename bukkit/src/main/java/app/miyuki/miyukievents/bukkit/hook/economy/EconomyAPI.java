package app.miyuki.miyukievents.bukkit.hook.economy;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public interface EconomyAPI {

    void deposit(@NotNull UUID player, @NotNull BigDecimal amount);

    boolean has(@NotNull UUID player, @NotNull BigDecimal amount);

    void withdraw(@NotNull UUID player, @NotNull BigDecimal amount);

}
