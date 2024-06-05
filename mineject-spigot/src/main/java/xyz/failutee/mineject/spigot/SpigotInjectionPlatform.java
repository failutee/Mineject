package xyz.failutee.mineject.spigot;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import xyz.failutee.mineject.commons.task.TaskService;
import xyz.failutee.mineject.platform.AbstractInjectionPlatform;
import xyz.failutee.mineject.platform.PlatformContext;
import xyz.failutee.mineject.processor.ClassProcessorManager;
import xyz.failutee.mineject.spigot.processor.ListenerProcessor;
import xyz.failutee.mineject.spigot.processor.TaskProcessor;
import xyz.failutee.mineject.spigot.task.BukkitTaskService;

import java.util.function.Consumer;

public final class SpigotInjectionPlatform extends AbstractInjectionPlatform {

    private final Plugin plugin;
    private final TaskService<BukkitTask> scheduledTask;

    public SpigotInjectionPlatform(PlatformContext platformContext) {
        this.plugin = platformContext.dependencyProvider().getDependency(Plugin.class);
        this.scheduledTask = new BukkitTaskService(this.plugin);
    }

    @Override
    public Consumer<ClassProcessorManager> configureProcessors() {
        return (manager) -> {
            manager.registerProcessor(new ListenerProcessor(this.plugin));
            manager.registerProcessor(new TaskProcessor(this.scheduledTask));
        };
    }
}
