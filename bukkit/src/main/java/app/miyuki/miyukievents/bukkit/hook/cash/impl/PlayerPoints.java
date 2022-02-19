package app.miyuki.miyukievents.bukkit.hook.cash.impl;

import app.miyuki.miyukievents.bukkit.hook.cash.CashAPI;
import lombok.val;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;

import java.math.BigDecimal;

public class PlayerPoints implements CashAPI {

    private final PlayerPointsAPI cashAPI = org.black_ixx.playerpoints.PlayerPoints.getInstance().getAPI();

    @Override
    public void deposit(String playerName, BigDecimal amount) {
        val uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();

        cashAPI.give(uuid, amount.intValue());
    }

}
