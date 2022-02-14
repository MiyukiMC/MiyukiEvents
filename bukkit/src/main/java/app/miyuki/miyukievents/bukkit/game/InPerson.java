package app.miyuki.miyukievents.bukkit.game;

import lombok.Getter;
import lombok.Setter;
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

@Getter
@Setter
public abstract class InPerson<W> extends Game<W> {

    @Getter
    protected final HashMap<Integer, @Nullable Location> entries = new HashMap<>();

    @Getter
    protected final HashMap<Integer, @NotNull ItemStack[]> kits = new HashMap<>();

    @Nullable
    protected Location lobby = null;

    @Nullable
    protected Location cabin = null;

    @Nullable
    protected Location exit = null;

    public InPerson(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    public abstract void join(Player player);

    public abstract void leave(Player player);

    public abstract void onPlayerQuit(PlayerQuitEvent event);

    public abstract void onPlayerDeath(PlayerDeathEvent event);

    public abstract void onPlayerInteract(PlayerInteractEvent event);

    public abstract void onEntityDamage(EntityDamageEvent event);

    public abstract void onEntityDamageByEntity(EntityDamageByEntityEvent event);

    public abstract void onBlockBreak(BlockBreakEvent event);

    public abstract void onBlockPlace(BlockPlaceEvent event);

    public abstract boolean isKitRequired();

    public abstract boolean isClanRequired();

    public abstract boolean isWorldEditRequired();

    public abstract boolean isTeamsEnabled();

}
