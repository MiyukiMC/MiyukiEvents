package app.miyuki.bukkit.game;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface InPerson {

    void join(Player player);

    void quit(Player player);

    void onPlayerQuit(PlayerQuitEvent event);

    void onPlayerDeath(PlayerDeathEvent event);

    void onPlayerInteract(PlayerInteractEvent event);

    void onEntityDamage(EntityDamageEvent event);

    void onEntityDamageByEntity(EntityDamageByEntityEvent event);

}
