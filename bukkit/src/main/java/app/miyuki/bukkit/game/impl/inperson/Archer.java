package app.miyuki.bukkit.game.impl.inperson;

import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.InPerson;
import app.miyuki.bukkit.game.repository.GameRepository;
import app.miyuki.bukkit.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Archer extends GameRepository<String, User> implements Game<Player>, InPerson {

    @Override
    public String getName() {
        return null;
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
    public void giveReward(Player player) {

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

}
