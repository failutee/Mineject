package xyz.failutee.example.spigot.track;

import org.bukkit.Server;
import xyz.failutee.example.spigot.ExamplePluginInitializationEvent;
import xyz.failutee.example.spigot.annotation.Service;
import xyz.failutee.mineject.subscribe.Subscribe;
import xyz.failutee.mineject.subscribe.Subscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
class PlayerTrackerServiceImpl implements TrackerService, Subscriber {

    private final Map<UUID, TrackInfo> trackedUuids = new HashMap<>();

    @Subscribe(ExamplePluginInitializationEvent.class)
    void onPluginInitialize(Server server) {
        server.getLogger().info("[TrackerService] >> TrackerService initialized!");
        server.getLogger().info("[TrackerService] >> Hello, i'm about to track some people :>");
    }

    @Override
    public void track(UUID uuid, TrackInfo info) {
        this.trackedUuids.put(uuid, info);
    }

    @Override
    public boolean isTracked(UUID uuid) {
        return this.trackedUuids.containsKey(uuid);
    }

    @Override
    public TrackInfo getTrackInfo(UUID uuid) {
        if (!this.isTracked(uuid)) {
            throw new RuntimeException("This uuid is not tracked!");
        }
        return this.trackedUuids.get(uuid);
    }
}
