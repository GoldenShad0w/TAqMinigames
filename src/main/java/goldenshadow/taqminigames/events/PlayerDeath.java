package goldenshadow.taqminigames.events;


import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.AvosRace;
import goldenshadow.taqminigames.minigames.ExcavationSiteE;
import goldenshadow.taqminigames.minigames.NesaakFight;
import goldenshadow.taqminigames.minigames.ProffersPit;
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
            if (TAqMinigames.minigame instanceof ProffersPit pit) {
                pit.onDeath(event.getEntity());
            }
            if (TAqMinigames.minigame instanceof ExcavationSiteE e) {
                e.onDeath(event.getEntity());
            }
            if (TAqMinigames.minigame instanceof AvosRace r) {
                r.onDeath(event.getEntity());
            }
        }
    }

}
