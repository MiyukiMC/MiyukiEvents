package app.miyuki.miyukievents.bukkit.hook.cash;

import java.math.BigDecimal;

public interface CashAPI {

    void deposit(String playerName, BigDecimal amount);

}
