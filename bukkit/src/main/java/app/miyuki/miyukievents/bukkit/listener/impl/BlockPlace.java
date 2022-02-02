package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.InPerson;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@AllArgsConstructor
public class BlockPlace implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        val currentgame = plugin.getGameManager().getCurrentGame();

        if (!(currentgame instanceof InPerson))
            return;

        ((InPerson) currentgame).onBlockPlace(event);
    }

}
