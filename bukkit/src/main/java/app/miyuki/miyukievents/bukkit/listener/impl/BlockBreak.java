package app.miyuki.miyukievents.bukkit.listener.impl;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.InPerson;
import lombok.AllArgsConstructor;
import lombok.val;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

@AllArgsConstructor
public class BlockBreak implements Listener {

    private final MiyukiEvents plugin;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        val currentgame = plugin.getGameManager().getCurrentGame();

        if (!(currentgame instanceof InPerson))
            return;

        ((InPerson) currentgame).onBlockBreak(event);
    }

}