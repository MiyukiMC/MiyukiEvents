package app.miyuki.miyukievents.bukkit.hook.vault;

import app.miyuki.miyukievents.bukkit.hook.ProviderService;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class VaultAPI implements ProviderService<Economy> {

    private Economy economy = null;

    @Override
    public boolean hook() {
        val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (registeredServiceProvider == null)
            return false;

        economy = registeredServiceProvider.getProvider();
        return false;
    }

    @Override
    public @Nullable Economy provide() {
        return economy;
    }


}
