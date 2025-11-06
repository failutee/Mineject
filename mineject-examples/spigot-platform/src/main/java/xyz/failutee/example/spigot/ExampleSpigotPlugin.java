package xyz.failutee.example.spigot;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.failutee.example.spigot.processor.ExamplePluginProcessorConfigurer;
import xyz.failutee.mineject.Mineject;
import xyz.failutee.mineject.MinejectFactory;
import xyz.failutee.mineject.event.EventDispatcher;
import xyz.failutee.mineject.spigot.SpigotInjectionPlatform;

public class ExampleSpigotPlugin extends JavaPlugin {

    private Mineject mineject;

    @Override
    public void onEnable() {
        this.mineject = MinejectFactory.create()
                .platformProvider(SpigotInjectionPlatform::new)
                .dependencySettings((settings, ctx) -> {
                    settings.packageName("xyz.failutee.example.spigot");
                    settings.processorConfigurer(new ExamplePluginProcessorConfigurer());
                })
                .withBean(Plugin.class, this)
                .withBean(Server.class, this.getServer())
                .build();

        EventDispatcher eventDispatcher = this.mineject.getEventDispatcher();

        eventDispatcher.dispatchEvent(new ExamplePluginInitializationEvent());
    }

    @Override
    public void onDisable() {
        if (this.mineject != null && !this.mineject.isShutdown()) {
            this.mineject.shutdown();
        }
    }
}
