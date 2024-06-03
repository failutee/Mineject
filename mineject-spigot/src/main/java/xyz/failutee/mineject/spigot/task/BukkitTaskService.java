package xyz.failutee.mineject.spigot.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import task.ScheduledTask;
import task.TaskService;

public class BukkitTaskService implements TaskService<BukkitTask> {

    private final Plugin plugin;
    private final BukkitScheduler scheduler;

    public BukkitTaskService(Plugin plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
    }

    @Override
    public void runTaskTimer(ScheduledTask<BukkitTask> scheduledTask, long delay, long repeat, boolean async) {
        if (async) {
            this.scheduler.runTaskTimerAsynchronously(this.plugin, scheduledTask::runTask, delay, repeat);
        }
        this.scheduler.runTaskTimer(this.plugin, scheduledTask::runTask, delay, repeat);
    }
}
