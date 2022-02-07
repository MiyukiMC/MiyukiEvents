package app.miyuki.miyukievents.bukkit.game.impl.inperson;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.InPerson;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class Deathmatch extends InPerson<List<Player>> {

    private final HashMap<Player, Integer> players = new HashMap<>();

    @Getter
    private final HashMap<Integer, @Nullable ItemStack[]> kits = new HashMap<>();

    @Getter
    private final HashMap<Integer, @Nullable Location> entries = new HashMap<>();

    @Getter
    @Setter
    @Nullable
    private Location lobby = null;

    @Getter
    @Setter
    @Nullable
    private Location cabin = null;

    @Getter
    @Setter
    @Nullable
    private Location exit = null;

    private int teams;

    public Deathmatch(@NotNull GameConfigProvider configProvider) {
        super(configProvider);

        val config = configProvider.provide(ConfigType.CONFIG);

        val data = configProvider.provide(ConfigType.DATA);

        val locationAdapter = plugin.getLocationAdapter();
        val itemSerialAdapter = plugin.getItemSerialAdapter();

        teams = config.contains("Teams") ? config.getInt("Teams") : -1;

        if (teams != -1) {
            for (int i = 1; i <= config.getInt("Teams"); i++) {

                ItemStack[] kit = null;
                Location entry = null;

                if (data.contains("Entry." + i))
                    kit = itemSerialAdapter.restore(data.getString("Kit." + i));

                if (data.contains("Entry." + i))
                    entry = locationAdapter.adapt(data.getString("Entry." + i));

                kits.put(i, kit);
                entries.put(i, entry);
            }
        } else if (data.contains("Entry")) {

            entries.put(0, locationAdapter.adapt(data.getString("Entry")));

        }


        if (data.contains("Lobby"))
            this.lobby = locationAdapter.adapt(data.getString("Lobby"));

        if (data.contains("Cabin"))
            this.cabin = locationAdapter.adapt(data.getString("Cabin"));

        if (data.contains("Exit"))
            this.exit = locationAdapter.adapt(data.getString("Exit"));

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onWin(List<Player> players) {

    }

    @Override
    protected void giveReward(List<Player> players) {

    }

    @Override
    public void join(Player player) {
        players.put(player, -1);
//        messageDispatcher.dispatch();
    }

    @Override
    public void leave(Player player) {

    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {

    }

    @Override
    public void onPlayerDeath(PlayerDeathEvent event) {

    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {

    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {

    }

    @Override
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {

    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {

    }

}
