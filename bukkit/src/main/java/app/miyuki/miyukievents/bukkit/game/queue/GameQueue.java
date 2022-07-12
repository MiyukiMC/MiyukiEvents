package app.miyuki.miyukievents.bukkit.game.queue;

import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import app.miyuki.miyukievents.bukkit.game.Game;
import app.miyuki.miyukievents.bukkit.game.manager.GameManager;
import lombok.val;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;

public class GameQueue {

    private final GameManager gameManager;

    private final Queue<Game<?>> queue = new LinkedList<>();

    public GameQueue(@NotNull MiyukiEvents plugin, @NotNull GameManager gameManager) {
        this.gameManager = gameManager;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (queue.isEmpty())
                    return;

                if (gameManager.getCurrentGame() != null)
                    return;

                val game = queue.poll();

                if (game == null)
                    return;

                game.start();
                gameManager.setLastGame(game);
            }
        }.runTaskTimerAsynchronously(plugin, 20L, 20L);
    }

    public boolean register(@NotNull Game<?> game) {
        return queue.offer(game);
    }

    public boolean unregister(@NotNull Game<?> game) {
        return queue.remove(game);
    }

    public boolean isRegistered(@NotNull Game<?> game) {
        return queue.contains(game);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

}
