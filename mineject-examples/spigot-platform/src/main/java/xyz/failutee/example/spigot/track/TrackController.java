package xyz.failutee.example.spigot.track;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.failutee.mineject.annotation.Injectable;
import xyz.failutee.mineject.spigot.annotation.Controller;

import java.util.UUID;

@Controller
class TrackController implements Listener {

    private final TrackerService trackerService;

    @Injectable
    public TrackController(TrackerService trackerService) {
        this.trackerService = trackerService;
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!this.trackerService.isTracked(uuid)) {
            TrackInfo trackInfo = new TrackInfo();

            this.trackerService.track(uuid, trackInfo);

            player.sendMessage("I see you :>");
        }

        TrackInfo trackInfo = this.trackerService.getTrackInfo(uuid);
        trackInfo.incrementJoinCount();

        player.sendMessage("You have joined this server '%s' times since restart.".formatted(trackInfo.getJoinCount()));
    }
}
