package xyz.failutee.example.spigot.track;

import java.util.UUID;

public interface TrackerService {

    void track(UUID uuid, TrackInfo info);

    boolean isTracked(UUID uuid);

    TrackInfo getTrackInfo(UUID uuid);

}
