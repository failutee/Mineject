package xyz.failutee.mineject.example;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.failutee.mineject.Mineject;
import xyz.failutee.mineject.MinejectFactory;
import xyz.failutee.mineject.spigot.SpigotInjectionPlatform;

public class MinejectExample extends JavaPlugin {

    @Override
    public void onEnable() {
        Mineject mineject = MinejectFactory.create()
                .dependencySettings((settings, context) -> settings.packageName("xyz.failutee.mineject.example"))
                .platformProvider(SpigotInjectionPlatform::new)
                .withBean(Plugin.class, this)
                .build(true);
    }
}
