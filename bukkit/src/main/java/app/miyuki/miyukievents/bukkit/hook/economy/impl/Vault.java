package app.miyuki.miyukievents.bukkit.hook.economy.impl;

import app.miyuki.miyukievents.bukkit.hook.economy.EconomyAPI;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class Vault implements EconomyAPI {

    private final Economy economy;

    public Vault() throws IllegalAccessException {
        val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (registeredServiceProvider == null)
            throw new IllegalAccessException();

        this.economy = registeredServiceProvider.getProvider();
    }

    @Override
    public void deposit(@NotNull UUID player, @NotNull BigDecimal amount) {
        this.economy.depositPlayer(Bukkit.getOfflinePlayer(player), amount.doubleValue());
    }

    @Override
    public boolean has(@NotNull UUID player, @NotNull BigDecimal amount) {
        return this.economy.has(Bukkit.getOfflinePlayer(player), amount.doubleValue());
    }

    @Override
    public void withdraw(@NotNull UUID player, @NotNull BigDecimal amount) {
        economy.withdrawPlayer(Bukkit.getOfflinePlayer(player), amount.doubleValue());
    }
}
