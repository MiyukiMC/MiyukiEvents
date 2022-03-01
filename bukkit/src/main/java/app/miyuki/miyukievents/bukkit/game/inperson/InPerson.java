package app.miyuki.miyukievents.bukkit.game.inperson;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import lombok.Getter;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Getter
public abstract class InPerson<W> extends Game<W> {

    @Nullable
    protected Location lobby;

    @Nullable
    protected Location cabin;

    @Nullable
    protected Location exit;

    public InPerson(@NotNull GameConfigProvider configProvider) {
        super(configProvider);

        val data = configProvider.provide(ConfigType.DATA);

        val locationAdapter = plugin.getLocationAdapter();

        this.lobby = data.contains("Lobby") ? locationAdapter.adapt(data.getString("Lobby")) : null;
        this.cabin = data.contains("Cabin") ? locationAdapter.adapt(data.getString("Cabin")) : null;
        this.exit = data.contains("Exit") ? locationAdapter.adapt(data.getString("Exit")) : null;

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

    public void setLobby(@NotNull Location lobby) {
        saveLocation("Lobby", lobby);
        this.lobby = lobby;
    }

    public void setCabin(@NotNull Location cabin) {
        saveLocation("Cabin", cabin);
        this.cabin = cabin;
    }

    public void setExit(@NotNull Location exit) {
        saveLocation("Exit", exit);
        this.exit = exit;
    }

    protected void saveLocation(@NotNull String path, @NotNull Location location) {
        val data = configProvider.provide(ConfigType.DATA);
        data.set(path, plugin.getLocationAdapter().restore(location));
        data.saveConfig();
    }



}
