package app.miyuki.miyukievents.bukkit.hook.cash.impl;

import app.miyuki.miyukievents.bukkit.hook.cash.CashAPI;
import com.ystoreplugins.ypoints.api.yPointsAPI;
import lombok.val;

public class YPoints implements CashAPI {

    private final yPointsAPI cashAPI = yPointsAPI.ypointsapi;

    @Override
    public void deposit(String playerName, Double amount) {
        val account = cashAPI.getPlayer(playerName);

        if (account == null)
            return;

        account.deposit(amount);
    }

}
