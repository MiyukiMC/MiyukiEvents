package app.miyuki.miyukievents.bukkit.hook.cash.impl;

import app.miyuki.miyukievents.bukkit.hook.cash.CashAPI;
import com.nextplugins.cash.api.NextCashAPI;
import lombok.val;

import java.math.BigDecimal;

public class NextCash implements CashAPI {

    private final NextCashAPI cashAPI = NextCashAPI.getInstance();

    @Override
    public void deposit(String playerName, BigDecimal amount) {
        val account = cashAPI.findAccountByOwner(playerName);

        if (!(account.isPresent()))
            return;

        account.get().depositAmount(amount.doubleValue());
    }

}
