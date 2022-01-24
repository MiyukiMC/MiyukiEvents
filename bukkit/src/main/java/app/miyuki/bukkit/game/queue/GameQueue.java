package app.miyuki.bukkit.game.queue;

import app.miyuki.bukkit.MiyukiEvents;
import app.miyuki.bukkit.game.Game;
import app.miyuki.bukkit.game.GameState;
import app.miyuki.bukkit.game.manager.GameManager;
import lombok.val;
import lombok.var;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class GameQueue extends BukkitRunnable {


    private final MiyukiEvents plugin;
    private final GameManager gameManager;

    private final Queue<Game> queue = new LinkedList<>();

    public GameQueue(@NotNull MiyukiEvents plugin, @NotNull GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
        this.runTaskTimerAsynchronously(plugin, 20L, 20L);
    }

    @Override
    public void run() {

        var happening = false;

        for (Game game : gameManager.getGames().values()) {
            if (game.getGameState() == GameState.HAPPENING)
                happening = true;
        }

        if (!happening)
            return;

        val game = queue.poll();

        if (game == null)
            return;

        game.start();


    }

    public void register(@NotNull Game game) {
        queue.offer(game);
    }

    public void unregister(@NotNull Game game) {
        queue.remove(game);
    }

}
