package app.miyuki.miyukievents.bukkit.hook.worldedit;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public interface WorldEditAPI {

    @Nullable
    Location[] getSelector(@NotNull Player player);

    void pasteSchematic(@NotNull File file, @NotNull Location pos1, @NotNull Location pos2);

    void copySchematic(@NotNull File file, @NotNull Location pos1, @NotNull Location pos2);

}
