package app.miyuki.miyukievents.bukkit.hook.cash.impl;

import app.miyuki.miyukievents.bukkit.hook.cash.CashAPI;
import com.nextplugins.cash.api.NextCashAPI;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class NextCash implements CashAPI {

    private final NextCashAPI cashAPI = NextCashAPI.getInstance();

    @Override
    public void deposit(@NotNull UUID player, @NotNull BigDecimal amount) {
        cashAPI.findAccountByOwner(Bukkit.getOfflinePlayer(player).getName())
                .ifPresent(account -> account.depositAmount(amount.doubleValue()));
    }


}
