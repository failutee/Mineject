package xyz.failutee.mineject.spigot.processor;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.processor.ClassProcessor;
import xyz.failutee.mineject.spigot.annotation.Task;

import java.util.function.Consumer;

public class TaskProcessor implements ClassProcessor {

    private final Plugin plugin;

    public TaskProcessor(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean shouldProcess(Class<?> clazz) {
        return clazz.isAnnotationPresent(Task.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void processClass(Object instance) {
        BukkitScheduler scheduler = this.plugin.getServer().getScheduler();
        Task task = instance.getClass().getDeclaredAnnotation(Task.class);

        if (instance instanceof Runnable runnable) {

            if (task.async()) {
                scheduler.runTaskTimerAsynchronously(this.plugin, runnable, task.delay(), task.repeat());
            }
            else {
                scheduler.runTaskTimer(this.plugin, runnable, task.delay(), task.repeat());
            }

            return;
        }
        else if (instance instanceof Consumer<?> consumer && BukkitTask.class.isAssignableFrom(consumer.getClass())) {
            Consumer<BukkitTask> bukkitTaskConsumer = (Consumer<BukkitTask>) instance;

            if (task.async()) {
                scheduler.runTaskTimerAsynchronously(this.plugin, bukkitTaskConsumer, task.delay(), task.repeat());
            }
            else {
                scheduler.runTaskTimer(this.plugin, bukkitTaskConsumer, task.delay(), task.repeat());
            }

            return;
        }

        throw new DependencyException("Class '%s' does not implement Runnable or Consumer<BukkitTask>".formatted(instance.getClass().getSimpleName()));
    }
}
