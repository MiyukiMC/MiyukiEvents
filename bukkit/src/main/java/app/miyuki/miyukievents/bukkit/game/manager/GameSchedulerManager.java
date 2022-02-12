package app.miyuki.miyukievents.bukkit.game.manager;


import app.miyuki.miyukievents.bukkit.MiyukiEvents;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@AllArgsConstructor
public class GameSchedulerManager {

    private final MiyukiEvents plugin;

    private final Set<BukkitTask> tasks = new HashSet<>();

    public void run(Runnable runnable) {
        tasks.add(Bukkit.getScheduler().runTask(plugin, runnable));
    }

    public void run(long delay, Runnable runnable) {
        tasks.add(Bukkit.getScheduler().runTaskLater(plugin, runnable, delay));
    }

    public void run(long delay, long period, Runnable runnable) {
        tasks.add(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period));
    }

    public void run(long delay, long period, Consumer<BukkitTask> task) {
        Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
    }

    public void run(long delay, Consumer<BukkitTask> task) {
        Bukkit.getScheduler().runTaskLater(plugin, task, delay);
    }

    public void runAsync(Runnable runnable) {
        tasks.add(Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    public void runAsync(long delay, Runnable runnable) {
        tasks.add(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay));
    }

    public void runAsync(long delay, long period, Runnable runnable) {
        tasks.add(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, period));
    }

    public void runAsync(long delay, Consumer<BukkitTask> task) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
    }

    public void runAsync(long delay, long period, Consumer<BukkitTask> task) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
    }

    public void cancel() {
        for (BukkitTask task : tasks) {
            if (!task.isCancelled()) {
                task.cancel();
            }
        }
        tasks.clear();
    }


}
