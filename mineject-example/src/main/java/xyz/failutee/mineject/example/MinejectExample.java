package xyz.failutee.mineject.example;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.failutee.mineject.Mineject;
import xyz.failutee.mineject.MinejectFactory;
import xyz.failutee.mineject.spigot.SpigotInjectionPlatform;

public class MinejectExample extends JavaPlugin {

    private Mineject mineject;

    @Override
    public void onEnable() {
        this.mineject = new MinejectFactory()
                .dependencySettings(settings -> settings.packageName("xyz.failutee.mineject.example"))
                .platformProvider(SpigotInjectionPlatform::new)
                .withBean(Plugin.class, this)
                .build(true);
    }
}
