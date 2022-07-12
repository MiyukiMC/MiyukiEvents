package app.miyuki.miyukievents.bukkit.adapter.impl;

import app.miyuki.miyukievents.bukkit.adapter.Adapter;
import app.miyuki.miyukievents.bukkit.adapter.Restorable;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class LocationAdapter implements Adapter<Location, String>, Restorable<String, Location> {

    @Override
    @Nullable
    public Location adapt(@NotNull String string) {
        val split = string.split(";");

        return new Location(
                Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[4]),
                Float.parseFloat(split[5])
        );
    }

    @Override
    @Nullable
    public String restore(@NotNull Location location) {
        return location.getWorld().getName() +
                ";" + location.getX() +
                ";" + location.getY() +
                ";" + location.getZ() +
                ";" + location.getYaw() +
                ";" + location.getPitch();
    }

}
