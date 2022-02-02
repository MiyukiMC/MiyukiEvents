package app.miyuki.miyukievents.bukkit.hook.cash;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.hook.ProviderService;
import app.miyuki.miyukievents.bukkit.hook.cash.impl.NextCash;
import app.miyuki.miyukievents.bukkit.hook.cash.impl.YPoints;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class CashProvider implements ProviderService {

    @Getter
    private final CashAPI cashAPI = null;

    public CashProvider(MiyukiEvents plugin) {
        val pluginManager = Bukkit.getPluginManager();

        if (pluginManager.getPlugin("NextPoints") != null) {
            Bukkit.getServicesManager().register(CashAPI.class, new YPoints(), plugin, ServicePriority.Highest);
        } else if (pluginManager.getPlugin("yPoints") != null) {
            Bukkit.getServicesManager().register(CashAPI.class, new NextCash(), plugin, ServicePriority.Highest);
        }

    }

    @Override
    public boolean hook() {
        val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(CashAPI.class);

        if (registeredServiceProvider == null)
            return false;

        val provider = registeredServiceProvider.getProvider();
        return provider != null;
    }

}
