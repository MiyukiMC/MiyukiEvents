package app.miyuki.miyukievents.bukkit.hook.economy.impl;

import app.miyuki.miyukievents.bukkit.hook.economy.EconomyAPI;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

import java.math.BigDecimal;
import java.util.UUID;

public class Vault implements EconomyAPI {

    private final Economy economy;

    public Vault() throws IllegalAccessException {
        val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (registeredServiceProvider == null)
            throw new IllegalAccessException();

        economy = registeredServiceProvider.getProvider();
    }


    @Override
    public void deposit(UUID player, BigDecimal amount) {
        economy.depositPlayer(Bukkit.getOfflinePlayer(player), amount.doubleValue());
    }

    @Override
    public boolean has(UUID player, BigDecimal amount) {
        return economy.has(Bukkit.getOfflinePlayer(player), amount.doubleValue());
    }

    @Override
    public void withdraw(UUID player, BigDecimal amount) {
        economy.withdrawPlayer(Bukkit.getOfflinePlayer(player), amount.doubleValue());
    }
}
