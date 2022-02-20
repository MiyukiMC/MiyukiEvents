package app.miyuki.miyukievents.bukkit.hook.cash;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public interface CashAPI {

    void deposit(@NotNull UUID player, @NotNull BigDecimal amount);

}
