package app.miyuki.miyukievents.bukkit.hook.economy;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.hook.ProviderService;
import app.miyuki.miyukievents.bukkit.hook.economy.impl.Vault;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.Optional;

public class EconomyProvider implements ProviderService<EconomyAPI> {

    private EconomyAPI economyAPI = null;

    public EconomyProvider(MiyukiEvents plugin) {
        val pluginManager = Bukkit.getPluginManager();

        if (pluginManager.getPlugin("Vault") != null) {
            try {
                Bukkit.getServicesManager().register(EconomyAPI.class, new Vault(), plugin, ServicePriority.Highest);
            } catch (IllegalAccessException ignored) {
            }
        }

    }

    @Override
    public boolean hook() {
        val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(EconomyAPI.class);

        if (registeredServiceProvider == null)
            return false;

        this.economyAPI = registeredServiceProvider.getProvider();
        return true;
    }

    @Override
    public Optional<EconomyAPI> provide() {
        return Optional.ofNullable(economyAPI);
    }

}
