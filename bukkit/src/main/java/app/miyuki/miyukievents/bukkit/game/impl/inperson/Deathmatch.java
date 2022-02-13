package app.miyuki.miyukievents.bukkit.game.impl.inperson;

import app.miyuki.miyukievents.bukkit.config.ConfigType;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.InPerson;
import app.miyuki.miyukievents.bukkit.util.player.PlayerUtils;
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

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Deathmatch extends InPerson<List<Player>> {

    private final HashMap<Player, Integer> players = new HashMap<>();

    @Getter
    private final HashMap<Integer, @Nullable ItemStack[]> kits = new HashMap<>();

    @Getter
    private final HashMap<Integer, @Nullable Location> entries = new HashMap<>();

    private final HashMap<Integer, Integer> score = new HashMap<>();

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

    private final int teams;

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

        val config = configProvider.provide(ConfigType.CONFIG);

        setGameState(GameState.STARTING);

        AtomicInteger calls = new AtomicInteger(config.getInt("Calls"));
        val interval = config.getInt("CallInterval");

        schedulerManager.runAsync(0L, interval * 20L, () -> {

            if (calls.get() > 0) {
                val seconds = calls.get() * interval;

                messageDispatcher.globalDispatch("Start", message -> message
                        .replace("{size}", String.valueOf(players.size()))
                        .replace("{cost}", String.valueOf(getCost()))
                        .replace("{seconds}", String.valueOf(seconds)));
            } else {

                schedulerManager.cancel();

                setGameState(GameState.STARTED);

                if (players.size() < config.getInt("MinimumPlayers")) {
                    messageDispatcher.globalDispatch("NotEnoughPlayers");
                    stop();
                    return;
                }

                if (isWorldEditRequired()) {


                    val worldEdit = plugin.getWorldEditProvider().provide().get();

                    val data = configProvider.provide(ConfigType.DATA);

                    val locationAdapter = plugin.getLocationAdapter();

                    val schematics = config.getStringList("Schematic.Schematics");

                    val randomSchematic = config.getBoolean("Schematic.Random");

                    if (randomSchematic) {
                        schematics.clear();

                        val section = data.getConfigurationSection("Schematic");

                        if (section != null) {
                            schematics.addAll(section.getKeys(false));
                        }

                        Collections.shuffle(schematics);
                    }


                    for (String schematic : schematics) {

                        long time = 0L;
                        String name;

                        if (schematic.contains(":")) {
                            name = schematic;
                        } else {
                            val split = schematic.split(":");
                            name = split[1].toUpperCase(Locale.ROOT);
                            time = Integer.parseInt(split[0]) * 20L;
                        }


                        val section = data.getConfigurationSection("Schematic." + name);

                        if (section == null)
                            continue;

                        val fileName = data.getString(section.getString("FileName"));
                        val pos1 = locationAdapter.adapt(section.getString("Pos1"));
                        val pos2 = locationAdapter.adapt(section.getString("Pos2"));

                        val file = new File(config.getFile().getParentFile(), "schematics/" + fileName);

                        if (!file.exists())
                            continue;

                        schedulerManager.run(time, () -> {
                            players.keySet().forEach(this::teleportToDesignatedEntrance);
                            worldEdit.pasteSchematic(file, pos1, pos2);
                        });


                    }
                }


            }

            calls.getAndDecrement();
        });

    }

    @Override
    public void stop() {
        setGameState(GameState.STOPPIMG);

        schedulerManager.cancel();
        players.keySet().forEach(this::leave);
        score.clear();

        setGameState(GameState.STOPPED);
    }

    @Override
    public void onWin(List<Player> players) {

    }

    @Override
    protected void giveReward(List<Player> players) {
        this.reward.execute(players);
    }

    @Override
    public boolean isEconomyRequired() {
        return false;
    }

    @Override
    public void join(Player player) {
        players.put(player, players.size());
        messageDispatcher.dispatch(player, "Join");
        player.teleport(this.lobby);
    }

    @Override
    public void leave(Player player) {
        players.remove(player);

        if (isKitRequired()) {
            PlayerUtils.clearInventory(player);
        }

        player.teleport(this.exit);

        checkWin();
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        val player = event.getPlayer();

        if (players.containsKey(player))
            return;

        leave(player);
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

    @Override
    public boolean isKitRequired() {
        return configProvider.provide(ConfigType.CONFIG).getBoolean("KitSetted");
    }

    @Override
    public boolean isClanRequired() {
        return false;
    }

    @Override
    public boolean isWorldEditRequired() {
        return configProvider.provide(ConfigType.CONFIG).getBoolean("Schematic.Enabled");
    }

    @Override
    public boolean isTeamsEnabled() {
        return teams == -1;
    }

    private void checkWin() {

        if (gameState != GameState.STARTING)
            return;

        val config = configProvider.provide(ConfigType.CONFIG);

        val type = config.getString("GameType.Type").toUpperCase(Locale.ROOT);
        val score = config.getInt("GameType.Score");

        if (isTeamsEnabled()) {

            if (type.equals("SCORE")) {

                for (Map.Entry<Player, Integer> entry : players.entrySet()) {

                    val playerScore = this.score.get(entry.getValue());

                    if (playerScore == score) {
                        onWin(Collections.singletonList(entry.getKey()));
                        break;
                    }
                }

            } else {

                if (players.size() == 1) {
                    onWin(Collections.singletonList((Player) players.values().toArray()[0]));
                }

            }

        } else {

            if (type.equals("SCORE")) {

                for (Map.Entry<Integer, Integer> entry : this.score.entrySet()) {

                    if (entry.getValue() == score) {

                        onWin(getPlayersByTeam(entry.getKey()));
                        break;
                    }

                }

            } else {

                int lastTeam = -1;

                for (Map.Entry<Player, Integer> entry : players.entrySet()) {

                    if (lastTeam == -1) {
                        lastTeam = entry.getValue();
                        continue;
                    }

                    if (lastTeam != entry.getValue()) {
                        onWin(getPlayersByTeam(entry.getValue()));
                        break;
                    }

                }

            }

        }

    }

    private List<Player> getPlayersByTeam(int team) {

        List<Player> players = new ArrayList<>();

        for (Map.Entry<Player, Integer> entry : this.players.entrySet()) {

            if (entry.getValue() == team)
                players.add(entry.getKey());

        }

        return players;
    }

    private void organizeTeams() {
        val players = new ArrayList<>(this.players.keySet());

        Collections.shuffle(players);

        int i = 1;
        for (Player player : players) {

            this.players.put(player, i);

            i++;

            if (i > teams)
                i = 1;

        }

    }

    private void teleportToDesignatedEntrance(Player player) {
        player.teleport(isTeamsEnabled() ? entries.get(players.get(player)) : entries.get(0));

    }


}
