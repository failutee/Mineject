package xyz.failutee.mineject.spigot.processor;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import xyz.failutee.mineject.processor.ClassProcessor;

public class ListenerProcessor implements ClassProcessor {

    private final Plugin plugin;

    public ListenerProcessor(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean shouldProcess(Class<?> clazz) {
        return Listener.class.isAssignableFrom(clazz);
    }

    @Override
    public void processClass(Object instance) {
        if (!(instance instanceof Listener listener)) {
            return;
        }

        System.out.println("Created even without annotation łot");

        PluginManager pluginManager = this.plugin.getServer().getPluginManager();
        pluginManager.registerEvents(listener, this.plugin);
    }
}
