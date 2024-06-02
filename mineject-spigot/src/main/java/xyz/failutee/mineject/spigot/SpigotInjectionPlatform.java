package xyz.failutee.mineject.spigot;

import org.bukkit.plugin.Plugin;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.processor.ClassProcessorManager;
import xyz.failutee.mineject.processor.ClassProcessorManagerFactory;
import xyz.failutee.mineject.spigot.processor.ListenerProcessor;
import xyz.failutee.mineject.spigot.processor.TaskProcessor;

public final class SpigotInjectionPlatform implements InjectionPlatform {

    private final Plugin plugin;

    public SpigotInjectionPlatform(DependencyProvider dependencyProvider) {
        this.plugin = dependencyProvider.getDependency(Plugin.class);
    }

    @Override
    public ClassProcessorManager getClassProcessorManager() {
        return new ClassProcessorManagerFactory()
                .processor(new ListenerProcessor(this.plugin))
                .processor(new TaskProcessor(this.plugin))
                .build();
    }
}
