package app.miyuki.miyukievents.bukkit.hook.cash.impl;

import app.miyuki.miyukievents.bukkit.hook.cash.CashAPI;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class PlayerPoints implements CashAPI {

    private final PlayerPointsAPI cashAPI = org.black_ixx.playerpoints.PlayerPoints.getInstance().getAPI();

    @Override
    public void deposit(@NotNull UUID player, @NotNull BigDecimal amount) {
        cashAPI.give(player, amount.intValue());
    }

}
