package xyz.failutee.mineject.spigot;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;
import xyz.failutee.mineject.bean.BeanProcessor;
import xyz.failutee.mineject.commons.task.ScheduledTask;
import xyz.failutee.mineject.commons.task.TaskService;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.platform.PlatformContext;
import xyz.failutee.mineject.processor.ProcessorConfigurer;
import xyz.failutee.mineject.spigot.annotation.Task;
import xyz.failutee.mineject.spigot.task.BukkitTaskService;
import xyz.failutee.mineject.util.ReflectionUtil;

public final class SpigotInjectionPlatform implements InjectionPlatform {

    private final Plugin plugin;
    private final TaskService<BukkitTask> scheduledTask;

    public SpigotInjectionPlatform(PlatformContext platformContext) {
        this.plugin = platformContext.dependencyProvider().getDependency(Plugin.class);
        this.scheduledTask = new BukkitTaskService(this.plugin);
    }

    @Override
    public ProcessorConfigurer getProcessorConfigurer() {
        return beanProcessor -> beanProcessor.onProcess(Listener.class, (listener) -> {
            PluginManager pluginManager = this.plugin.getServer().getPluginManager();
            pluginManager.registerEvents(listener, this.plugin);
        })
        .onProcess(ScheduledTask.class, Task.class, (task, object) -> {
            Class<?> clazz = object.getClass();

            if (!BukkitTask.class.isAssignableFrom(clazz)) {
                throw new DependencyException("Class '%s' does not implement ScheduledTask<BukkitTask>".formatted(clazz.getSimpleName()));
            }

            this.scheduledTask.runTaskTimer(ReflectionUtil.unsafeCast(object),
                    task.delay(), task.repeat(), task.async());
        });
    }
}
