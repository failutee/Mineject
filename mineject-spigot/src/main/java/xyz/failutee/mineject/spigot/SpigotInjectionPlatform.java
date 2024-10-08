package xyz.failutee.mineject.spigot;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;
import xyz.failutee.mineject.commons.task.ScheduledTask;
import xyz.failutee.mineject.commons.task.TaskService;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.dependency.DependencyContext;
import xyz.failutee.mineject.processor.ProcessorConfigurer;
import xyz.failutee.mineject.spigot.annotation.Controller;
import xyz.failutee.mineject.spigot.annotation.Task;
import xyz.failutee.mineject.spigot.task.BukkitTaskService;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class SpigotInjectionPlatform implements InjectionPlatform {

    private final Plugin plugin;
    private final TaskService<BukkitTask> scheduledTask;

    public SpigotInjectionPlatform(DependencyContext dependencyContext) {
        this.plugin = dependencyContext.dependencyProvider().getDependency(Plugin.class);
        this.scheduledTask = new BukkitTaskService(this.plugin);
    }

    @Override
    public ProcessorConfigurer getProcessorConfigurer() {
        return beanProcessor -> beanProcessor.onProcess(Controller.class, Listener.class, (controller, listener) -> {
            PluginManager pluginManager = this.plugin.getServer().getPluginManager();
            pluginManager.registerEvents(listener, this.plugin);
        })
        .onProcess(Task.class, ScheduledTask.class, (task, object) -> {
            Class<?> clazz = object.getClass();

            Duration delayDuration = this.toDuration(task.delay(), task.unit());
            Duration repeatDuration = this.toDuration(task.period(), task.unit());

            try {
                this.scheduledTask.runTaskTimer(ReflectionUtil.unsafeCast(object), this.toTicks(delayDuration), this.toTicks(repeatDuration), task.async());
            } catch (ClassCastException exception) {
                throw new DependencyException("Class '%s' does not implement ScheduledTask<BukkitTask>".formatted(clazz.getSimpleName()));
            }
        });
    }

    private Duration toDuration(long time, TimeUnit unit) {
        return Duration.ofMillis(unit.toMillis(time));
    }

    private long toTicks(Duration duration) {
        return duration.toMillis() / 50;
    }
}
