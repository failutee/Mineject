package xyz.failutee.mineject.example.player;

import org.bukkit.entity.Player;
import xyz.failutee.mineject.annotation.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
class PlayerServiceImpl implements PlayerService {

    private final Set<MinejectPlayer> minejectPlayers = new HashSet<>();

    @Override
    public MinejectPlayer registerMinejectPlayer(Player player) {
        MinejectPlayer minejectPlayer = new MinejectPlayer(player);

        this.minejectPlayers.add(minejectPlayer);

        return minejectPlayer;
    }

    @Override
    public void removeMinejectPlayer(Player player) {
        this.getMinejectPlayer(player).ifPresent(this.minejectPlayers::remove);
    }

    @Override
    public Optional<MinejectPlayer> getMinejectPlayer(Player player) {
        return this.minejectPlayers.stream()
                .filter(minejectPlayer -> minejectPlayer.getPlayer().getUniqueId().equals(player.getUniqueId()))
                .findFirst();
    }

    @Override
    public boolean isMinejectPlayer(Player player) {
        return this.getMinejectPlayer(player).isPresent();
    }
}
