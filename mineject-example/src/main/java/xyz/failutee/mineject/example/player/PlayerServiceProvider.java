package xyz.failutee.mineject.example.player;

import xyz.failutee.mineject.annotation.Bean;
import xyz.failutee.mineject.annotation.BeanSetup;

@BeanSetup
class PlayerServiceProvider {

    @Bean
    PlayerService getPlayerService() {
        return new PlayerServiceImpl();
    }

}
