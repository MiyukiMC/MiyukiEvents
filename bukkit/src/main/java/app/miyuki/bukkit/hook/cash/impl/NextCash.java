package app.miyuki.bukkit.hook.cash.impl;

import app.miyuki.bukkit.hook.cash.CashAPI;
import com.nextplugins.cash.api.NextCashAPI;
import lombok.val;

public class NextCash implements CashAPI {

    private final NextCashAPI cashAPI = NextCashAPI.getInstance();

    @Override
    public void deposit(String playerName, Double amount) {
        val account = cashAPI.findAccountByOwner(playerName);

        if (!(account.isPresent()))
            return;

        account.get().depositAmount(amount);
    }

}
