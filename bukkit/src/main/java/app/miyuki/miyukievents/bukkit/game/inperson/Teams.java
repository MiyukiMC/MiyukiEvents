package app.miyuki.miyukievents.bukkit.game.inperson;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.user.User;
import lombok.Getter;
import lombok.val;
import lombok.var;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class Teams<W> extends InPerson<W> {

    protected final HashMap<User, Integer> users = new HashMap<>();

    @Getter
    protected final HashMap<Integer, Location> entries = new HashMap<>();

    @Getter
    protected final HashMap<Integer, ItemStack[]> kits = new HashMap<>();

    public Teams(@NotNull GameConfigProvider configProvider) {
        super(configProvider);

        val data = configProvider.provide(ConfigType.DATA);

        val locationAdapter = plugin.getLocationAdapter();
        val itemSerialAdapter = plugin.getItemSerialAdapter();

        val entriesSection = data.getConfigurationSection("Entries");
        if (entriesSection != null) {
            for (String entry : entriesSection.getKeys(false)) {
                entries.put(Integer.parseInt(entry), locationAdapter.adapt(data.getString("Entries." + entry)));
            }
        }

        val kitsSection = data.getConfigurationSection("Kits");
        if (kitsSection != null) {
            for (String kit : kitsSection.getKeys(false)) {
                kits.put(Integer.parseInt(kit), itemSerialAdapter.restore(data.getString("Kits." + kit)));
            }
        }

    }

    protected void organizeTeams() {
        val teamAmount = entries.size();

        var i = 1;

        val users = new ArrayList<>(this.users.keySet());
        Collections.shuffle(users);

        for (User user : users) {

            this.users.put(user, i);

            i++;

            if (i > teamAmount)
                i = 1;

        }

    }

    protected void teleportToDesignedEntry(User user) {
        user.getPlayer().get().teleport(entries.get(users.get(user)));
    }

    public void addEntry(int team, @NotNull Location location) {
        saveLocation(team, location);
        this.entries.put(team, location);
    }

    public void removeEntry(int team) {
        saveLocation(team, null);
        this.entries.remove(team);
    }

    public void setKit(int team, @NotNull PlayerInventory inventory) {
        val itemSerialAdapter = plugin.getItemSerialAdapter();

        val serializedInventory = itemSerialAdapter
                .adapt((ItemStack[]) ArrayUtils.addAll(inventory.getContents(), inventory.getArmorContents()));

        val kit = itemSerialAdapter.restore(serializedInventory);

        kits.put(team, kit);
        saveKit(team, kit);
    }

    public void removeKit(int team) {
        kits.remove(team);
        saveKit(team, null);
    }

    public void saveKit(int team, @Nullable ItemStack[] kit) {
        val data = configProvider.provide(ConfigType.DATA);
        data.set("Kits." + team, kit != null ? plugin.getItemSerialAdapter().adapt(kit) : null);
        data.saveConfig();
    }

    private void saveLocation(int team, @Nullable Location location) {
        val data = configProvider.provide(ConfigType.DATA);
        data.set("Entries." + team, location != null ? plugin.getLocationAdapter().restore(location) : null);
        data.saveConfig();
    }


}
