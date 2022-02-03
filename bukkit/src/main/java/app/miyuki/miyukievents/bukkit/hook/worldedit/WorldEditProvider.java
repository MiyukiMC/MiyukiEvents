package app.miyuki.miyukievents.bukkit.hook.worldedit;

import app.miyuki.miyukievents.bukkit.hook.ProviderService;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

public class WorldEditProvider implements ProviderService<WorldEditAPI> {

    private static String WORLDEDIT6 = "app.miyuki.miyukievents.bukkit.impl.WorldEdit6";
    private static String WORLDEDIT7 = "app.miyuki.miyukievents.bukkit.impl.WorldEdit7";

    private WorldEditAPI worldEditAPI = null;

    @Override
    @SneakyThrows
    public boolean hook() {
        val plugin = Bukkit.getPluginManager().getPlugin("WorldEdit");

        if (plugin == null)
            return false;

        val version = plugin.getDescription().getVersion();

        Class<?> clasz = version.startsWith("6.") ? Class.forName(WORLDEDIT6) : Class.forName(WORLDEDIT7);

        worldEditAPI = (WorldEditAPI) clasz.getConstructor().newInstance();
        return true;

    }

    @Override
    public @Nullable WorldEditAPI provide() {
        return worldEditAPI;
    }

}
