package xyz.failutee.mineject.example.player;

import org.bukkit.entity.Player;

public class MinejectPlayer {

    private final Player player;

    private boolean greeted;

    public MinejectPlayer(Player player) {
        this.player = player;
    }

    public void setGreeted(boolean greeted) {
        this.greeted = greeted;
    }

    public boolean isGreeted() {
        return this.greeted;
    }

    public Player getPlayer() {
        return this.player;
    }
}
