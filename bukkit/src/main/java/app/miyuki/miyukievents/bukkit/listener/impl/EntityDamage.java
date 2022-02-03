package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.InPerson;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@AllArgsConstructor
public class EntityDamage implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        val currentGame = plugin.getGameManager().getCurrentGame();

        if (!(currentGame instanceof InPerson))
            return;

        ((InPerson) currentGame).onEntityDamage(event);
    }

}