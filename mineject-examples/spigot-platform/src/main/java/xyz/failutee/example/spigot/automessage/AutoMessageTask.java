package xyz.failutee.example.spigot.automessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.scheduler.BukkitTask;
import xyz.failutee.mineject.annotation.Injectable;
import xyz.failutee.mineject.commons.task.ScheduledTask;
import xyz.failutee.mineject.spigot.annotation.Task;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Task(delay = 0, period = 5, unit = TimeUnit.SECONDS)
public class AutoMessageTask implements ScheduledTask<BukkitTask> {

    private int messageIndex = 0;

    private static final List<String> HARD_CODED_MESSAGES = List.of(
      "Mineject is great dependency injection framework!!!",
      "This is how you can easily create task!",
      "This is example HARD-CODED message :>"
    );

    private final Server server;
    private final MiniMessage miniMessage;

    @Injectable
    public AutoMessageTask(Server server, MiniMessage miniMessage) {
        this.server = server;
        this.miniMessage = miniMessage;
    }

    @Override
    public void runTask(BukkitTask task) {
        String message = HARD_CODED_MESSAGES.get(this.messageIndex);
        this.server.broadcast(this.miniMessage.deserialize(message));

        this.messageIndex = (this.messageIndex + 1) % HARD_CODED_MESSAGES.size();
    }
}
