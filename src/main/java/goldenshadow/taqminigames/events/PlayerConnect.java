package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerConnect implements Listener {

    public void playerConnect(PlayerLoginEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame != null) {
                TAqMinigames.minigame.playerReconnect(event.getPlayer());
            }
        }
    }
}
