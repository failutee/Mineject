package xyz.failutee.mineject.example.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.failutee.mineject.annotation.Component;
import xyz.failutee.mineject.annotation.Injectable;

@Component
class PlayerJoinListener implements Listener {

    private final PlayerService playerService;

    @Injectable
    public PlayerJoinListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        MinejectPlayer minejectPlayer = this.playerService.getMinejectPlayer(player)
                .orElseGet(() -> this.playerService.registerMinejectPlayer(player));

        if (minejectPlayer.isGreeted()) {
            return;
        }

        player.sendMessage("Hi :)");
        minejectPlayer.setGreeted(true);
    }
}
