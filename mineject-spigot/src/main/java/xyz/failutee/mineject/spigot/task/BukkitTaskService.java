package xyz.failutee.mineject.spigot.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import xyz.failutee.mineject.commons.task.ScheduledTask;
import xyz.failutee.mineject.commons.task.TaskService;
import xyz.failutee.mineject.lifecycle.Cleanupable;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitTaskService implements TaskService<BukkitTask>, Cleanupable {

    private final Set<BukkitTask> activeTasks = ConcurrentHashMap.newKeySet();

    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public BukkitTaskService(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public void runTaskTimer(ScheduledTask<BukkitTask> scheduledTask, long delay, long repeat, boolean async) {
        if (async) {
            this.scheduler.runTaskTimerAsynchronously(this.plugin,
                    (bukkitTask) -> this.executeAndTrackTask(scheduledTask, bukkitTask), delay, repeat);
        } else {
            this.scheduler.runTaskTimer(this.plugin,
                    (bukkitTask) -> this.executeAndTrackTask(scheduledTask, bukkitTask), delay, repeat);
        }
    }

    private void executeAndTrackTask(ScheduledTask<BukkitTask> scheduledTask, BukkitTask bukkitTask) {
        scheduledTask.runTask(bukkitTask);
        this.activeTasks.add(bukkitTask);
    }

    @Override
    public void cleanup() {
        for (BukkitTask task : this.activeTasks) {
            if (!task.isCancelled()) {
                task.cancel();
            }
        }
        this.activeTasks.clear();
    }
}
