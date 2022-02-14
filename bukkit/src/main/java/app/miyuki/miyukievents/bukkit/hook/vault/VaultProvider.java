package app.miyuki.miyukievents.bukkit.hook.vault;

import app.miyuki.miyukievents.bukkit.hook.ProviderService;
import lombok.val;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

import java.util.Optional;

public class VaultProvider implements ProviderService<Economy> {

    private Economy economy = null;

    @Override
    public boolean hook() {
        try {
            if (Bukkit.getPluginManager().getPlugin("Vault") == null)
                return false;

            val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);

            if (registeredServiceProvider == null)
                return false;

            economy = registeredServiceProvider.getProvider();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public Optional<Economy> provide() {
        return Optional.ofNullable(economy);
    }

}
