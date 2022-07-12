package app.miyuki.miyukievents.bukkit.hook.cash;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.hook.ProviderService;
import app.miyuki.miyukievents.bukkit.hook.cash.impl.NextCash;
import app.miyuki.miyukievents.bukkit.hook.cash.impl.PlayerPoints;
import app.miyuki.miyukievents.bukkit.hook.cash.impl.YPoints;
import app.miyuki.miyukievents.bukkit.util.singlemap.Pair;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

import java.util.Map;
import java.util.Optional;

public class CashProvider implements ProviderService<CashAPI> {

    private static final Map<String, ? extends CashAPI> CASH_APIS = ImmutableMap.of(
            "NextCash", new NextCash(),
            "yPoints", new YPoints(),
            "PlayerPoints", new PlayerPoints()
    );

    private CashAPI cashAPI = null;

    public CashProvider(MiyukiEvents plugin) {
        val pluginManager = Bukkit.getPluginManager();

        CASH_APIS.forEach((key, value) -> {
            if (pluginManager.isPluginEnabled(key))
                Bukkit.getServicesManager().register(CashAPI.class, value, plugin, ServicePriority.Highest);
        });
    }

    @Override
    public boolean hook() {
        val registeredServiceProvider = Bukkit.getServicesManager().getRegistration(CashAPI.class);

        if (registeredServiceProvider == null)
            return false;

        this.cashAPI = registeredServiceProvider.getProvider();
        return true;
    }

    @Override
    public Optional<CashAPI> provide() {
        return Optional.ofNullable(cashAPI);
    }


}
