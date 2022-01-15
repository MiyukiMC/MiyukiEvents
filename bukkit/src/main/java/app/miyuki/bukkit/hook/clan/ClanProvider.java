package app.miyuki.bukkit.hook.clan;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.hook.clan.impl.SimpleClans;
import app.miyuki.bukkit.hook.clan.impl.YClans;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;


public class ClanProvider {

    private Clan clan = null;

    public ClanProvider(MiyukiEvents plugin) {

        val pluginManager = Bukkit.getPluginManager();
        if (pluginManager.getPlugin("yClans") != null) {
            Bukkit.getServicesManager().register(Clan.class, new YClans(), plugin, ServicePriority.Highest);
        } else if (pluginManager.getPlugin("SimpleClans") != null) {
            Bukkit.getServicesManager().register(Clan.class, new SimpleClans(), plugin, ServicePriority.Highest);
        }

    }

    public boolean hook() {
        val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(Clan.class);

        if (registeredServiceProvider == null)
            return false;

        val provider = registeredServiceProvider.getProvider();
        return provider != null;
    }

    public Clan getProvider() {
        return clan;
    }


}
