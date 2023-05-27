package goldenshadow.taqminigames.events;


import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.NesaakFight;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof NesaakFight nesaakFight) {
                nesaakFight.onDeath(event.getEntity());
            }
        }
    }

}
