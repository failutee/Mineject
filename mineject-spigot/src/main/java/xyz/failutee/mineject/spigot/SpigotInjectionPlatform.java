package xyz.failutee.mineject.spigot;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import xyz.failutee.mineject.commons.task.TaskService;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.processor.ClassProcessorManager;
import xyz.failutee.mineject.processor.ClassProcessorManagerFactory;
import xyz.failutee.mineject.spigot.processor.ListenerProcessor;
import xyz.failutee.mineject.spigot.processor.TaskProcessor;
import xyz.failutee.mineject.spigot.task.BukkitTaskService;

public final class SpigotInjectionPlatform implements InjectionPlatform {

    private final Plugin plugin;
    private final TaskService<BukkitTask> scheduledTask;

    public SpigotInjectionPlatform(DependencyProvider dependencyProvider) {
        this.plugin = dependencyProvider.getDependency(Plugin.class);
        this.scheduledTask = new BukkitTaskService(this.plugin);
    }

    @Override
    public ClassProcessorManager getClassProcessorManager() {
        return new ClassProcessorManagerFactory()
                .processor(new ListenerProcessor(this.plugin))
                .processor(new TaskProcessor(this.scheduledTask))
                .build();
    }
}
