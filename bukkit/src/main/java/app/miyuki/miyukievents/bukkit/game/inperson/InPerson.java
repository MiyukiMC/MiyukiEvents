package app.miyuki.miyukievents.bukkit.game.inperson;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.user.User;
import lombok.Getter;
import lombok.SneakyThrows;
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

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


@Getter
public abstract class InPerson<W> extends Game<W> {

    @Nullable
    protected Location lobby;

    @Nullable
    protected Location cabin;

    @Nullable
    protected Location exit;

    @Nullable
    protected Location positionOne;

    @Nullable
    protected Location positionTwo;

    private final Map<String, File> schematics = new HashMap<>();

    protected final Map<UUID, User> players = new HashMap<>();

    private final Map<UUID, Integer> score = new HashMap<>();

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

    public void setPos1(@NotNull Location positionOne) {
        saveLocation("Pos1", positionOne);
        this.positionOne = positionOne;
    }

    public void setPos2(@NotNull Location positionTwo) {
        saveLocation("Pos2", positionTwo);
        this.positionTwo = positionTwo;
    }

    public void addSchematic(@NotNull String name) {
        val data = configProvider.provide(ConfigType.DATA);

        val schematic = new File(data.getFile().getParentFile(), "schematics/" + name + ".schematic");

        data.set("Schematics." + name.toLowerCase(Locale.ROOT), schematic.toString());
        data.saveConfig();

        this.schematics.put(name.toLowerCase(Locale.ROOT), schematic);

        plugin.getWorldEditProvider().provide()
                .ifPresent(worldEditAPI -> worldEditAPI.copySchematic(schematic, positionOne, positionTwo));
    }

    @SneakyThrows
    public void removeSchematic(@NotNull String name) {
        val data = configProvider.provide(ConfigType.DATA);

        val schematicDataPath = "Schematics." + name.toLowerCase(Locale.ROOT);

        val schematic = new File(data.getString(schematicDataPath));

        Files.deleteIfExists(schematic.toPath());

        schematics.remove(name.toLowerCase(Locale.ROOT));

        data.set(schematicDataPath, null);
        data.saveConfig();
    }



}
