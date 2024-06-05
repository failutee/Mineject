package xyz.failutee.mineject.example.player;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import xyz.failutee.mineject.annotation.Injectable;
import xyz.failutee.mineject.commons.task.ScheduledTask;
import xyz.failutee.mineject.spigot.annotation.Task;

@Task(repeat = 20 * 3)
class CurrentOnlinePlayersTask implements ScheduledTask<BukkitTask> {

    private final Server server;

    @Injectable
    public CurrentOnlinePlayersTask(Plugin plugin) {
        this.server = plugin.getServer();
    }

    @Override
    public void runTask(BukkitTask task) {

        var onlinePlayers = this.server.getOnlinePlayers();

        onlinePlayers.forEach(player -> player.sendMessage("OnlinePlayers: %s".formatted(onlinePlayers.size())));

    }
}
