package app.miyuki.miyukievents.bukkit.hook.worldedit;

import app.miyuki.miyukievents.bukkit.hook.ProviderService;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;

import java.util.Optional;

public class WorldEditProvider implements ProviderService<WorldEditAPI> {

    private static final String WORLD_EDIT_6 = "app.miyuki.miyukievents.bukkit.impl.WorldEdit6";
    private static final String WORLD_EDIT_7 = "app.miyuki.miyukievents.bukkit.impl.WorldEdit7";

    private WorldEditAPI worldEditAPI = null;

    @Override
    @SneakyThrows
    public boolean hook() {
        val plugin = Bukkit.getPluginManager().getPlugin("WorldEdit");

        if (plugin == null)
            return false;

        val version = plugin.getDescription().getVersion();

        Class<?> clazz = version.startsWith("6.") ? Class.forName(WORLD_EDIT_6) : Class.forName(WORLD_EDIT_7);

        worldEditAPI = (WorldEditAPI) clazz.getConstructor().newInstance();
        return true;

    }

    @Override
    public Optional<WorldEditAPI> provide() {
        return Optional.ofNullable(worldEditAPI);
    }


}
