package app.miyuki.miyukievents.bukkit.hook.clan;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.hook.ProviderService;
import app.miyuki.miyukievents.bukkit.hook.clan.impl.SimpleClans;
import app.miyuki.miyukievents.bukkit.hook.clan.impl.YClans;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.Map;
import java.util.Optional;


public class ClanProvider implements ProviderService<ClanAPI> {

    private static final Map<String, Class<? extends ClanAPI>> CLAN_APIS = ImmutableMap.of(
            "yClans", YClans.class,
            "SimpleClans", SimpleClans.class
    );

    private ClanAPI clanAPI = null;

    public ClanProvider(MiyukiEvents plugin) {
        val pluginManager = Bukkit.getPluginManager();

        CLAN_APIS.forEach((key, value) -> {
            try {
                if (pluginManager.isPluginEnabled(key))
                    Bukkit.getServicesManager().register(ClanAPI.class, value.newInstance(), plugin, ServicePriority.High);
            } catch (Exception ignored) { }
        });

    }

    @Override
    public boolean hook() {
        val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(ClanAPI.class);

        if (registeredServiceProvider == null)
            return false;

        this.clanAPI = registeredServiceProvider.getProvider();
        return true;
    }

    @Override
    public Optional<ClanAPI> provide() {
        return Optional.ofNullable(clanAPI);
    }


}
