package app.miyuki.miyukievents.bukkit.game.impl.inperson;

import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.GameConfigProvider;
import app.miyuki.miyukievents.bukkit.game.GameState;
import app.miyuki.miyukievents.bukkit.game.InPerson;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class Killer extends Game<Player> implements InPerson {

    public Killer(@NotNull GameConfigProvider configProvider) {
        super(configProvider);
    }

    @Override
    public String getTypeName() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setGameState(GameState gameState) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onWin(Player player) {

    }

    @Override
    protected void giveReward(Player player) {

    }

    @Override
    public void join(Player player) {

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
