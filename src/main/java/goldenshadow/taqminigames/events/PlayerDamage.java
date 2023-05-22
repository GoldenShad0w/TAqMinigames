package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.minigames.AvosRace;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent byEntityEvent)  {
            if (byEntityEvent.getDamager() instanceof Firework) {
                if (byEntityEvent.getDamager().getScoreboardTags().contains("m_firework")) {
                    event.setCancelled(true);
                }
            }
        }
        if (event.getEntity() instanceof Player p) {
            if (TAqMinigames.minigame != null) {
                if (TAqMinigames.minigame instanceof AvosRace) {
                    if (((AvosRace) TAqMinigames.minigame).isHotFloor(p)) {
                        TAqMinigames.minigame.onDeath(p);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
