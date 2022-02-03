package app.miyuki.miyukievents.bukkit.hook.clan;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.hook.ProviderService;
import app.miyuki.miyukievents.bukkit.hook.clan.impl.SimpleClans;
import app.miyuki.miyukievents.bukkit.hook.clan.impl.YClans;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.Nullable;


public class ClanProvider implements ProviderService<ClanAPI> {

    private ClanAPI clanAPI = null;

    public ClanProvider(MiyukiEvents plugin) {

        val pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("yClans") != null) {
            Bukkit.getServicesManager().register(ClanAPI.class, new YClans(), plugin, ServicePriority.Highest);
        } else if (pluginManager.getPlugin("SimpleClans") != null) {
            Bukkit.getServicesManager().register(ClanAPI.class, new SimpleClans(), plugin, ServicePriority.Highest);
        }

    }

    @Override
    public boolean hook() {
        val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(ClanAPI.class);

        if (registeredServiceProvider == null)
            return false;

        clanAPI = registeredServiceProvider.getProvider();
        return true;
    }

    @Override
    public @Nullable ClanAPI provide() {
        return clanAPI;
    }

}
