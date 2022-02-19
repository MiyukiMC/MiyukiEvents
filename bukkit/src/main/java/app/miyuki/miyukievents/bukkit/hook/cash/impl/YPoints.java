package app.miyuki.miyukievents.bukkit.hook.cash.impl;

import app.miyuki.miyukievents.bukkit.hook.cash.CashAPI;
import com.ystoreplugins.ypoints.api.yPointsAPI;
import lombok.val;

import java.math.BigDecimal;

public class YPoints implements CashAPI {

    @Override
    public void deposit(String playerName, BigDecimal amount) {
        val account = yPointsAPI.getAccount(playerName);

        if (account != null)
            account.deposit(amount.doubleValue());
    }

}
