package app.miyuki.miyukievents.bukkit.hook.cash.impl;

import app.miyuki.miyukievents.bukkit.hook.cash.CashAPI;
import com.ystoreplugins.ypoints.api.yPointsAPI;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class YPoints implements CashAPI {

    @Override
    public void deposit(@NotNull UUID player, @NotNull BigDecimal amount) {
        val account = yPointsAPI.getAccount(Bukkit.getOfflinePlayer(player).getName());

        if (account != null)
            account.deposit(amount.doubleValue());
    }


}
