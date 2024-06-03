package xyz.failutee.mineject.example.player;

import org.bukkit.entity.Player;

import java.util.Optional;

public interface PlayerService {

    MinejectPlayer registerMinejectPlayer(Player player);

    void removeMinejectPlayer(Player player);

    Optional<MinejectPlayer> getMinejectPlayer(Player player);

    boolean isMinejectPlayer(Player player);

}
