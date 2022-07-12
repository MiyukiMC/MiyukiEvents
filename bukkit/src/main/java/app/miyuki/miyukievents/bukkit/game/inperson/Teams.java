package app.miyuki.miyukievents.bukkit.game.inperson;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.user.User;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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

    protected final Multimap<Integer, User> teams = ArrayListMultimap.create();

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
        val users = new ArrayList<>(this.players.values());
        Collections.shuffle(users);

        val teamAmount = entries.size();
        var team = 1;

        for (User user : users) {

            this.teams.put(team, user);

            team++;

            if (team > teamAmount)
                team = 1;

        }

    }

    protected void teleportAllUsersToDesignatedEntrance(@NotNull User user) {
        teams.asMap().forEach((team, users) -> user.getPlayer().ifPresent(it -> it.teleport(entries.get(team))));
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
