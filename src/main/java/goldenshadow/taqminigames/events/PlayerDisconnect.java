package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.event.Participant;
import goldenshadow.taqminigames.event.ParticipantManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnect implements Listener {

    @EventHandler
    public void event(PlayerQuitEvent event) {
        if (TAqMinigames.isRunning()) {
            Participant p = ParticipantManager.getParticipant(event.getPlayer());
            if (p != null) {
                if (TAqMinigames.minigame != null) {
                    p.lastGame = TAqMinigames.minigame.getGame();
                    TAqMinigames.minigame.playerDisconnect(event.getPlayer());
                } else p.lastGame = null;
                p.lastGameMode = event.getPlayer().getGameMode();
            }
        }
    }
}
