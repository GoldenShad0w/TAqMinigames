package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.minigames.AledarCartRacing;
import goldenshadow.taqminigames.minigames.AvosRace;
import goldenshadow.taqminigames.minigames.ProffersPit;
import org.bukkit.Bukkit;
import org.bukkit.entity.EnderCrystal;
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
            if (TAqMinigames.minigame instanceof AledarCartRacing) {
                if (event.getEntity() instanceof EnderCrystal) {
                    event.setCancelled(true);
                }
            }
        }
        if (event.getEntity() instanceof Player p) {
            if (TAqMinigames.minigame instanceof AvosRace) {
                if (Bukkit.getOnlinePlayers().contains(p)) {
                    if (!(event instanceof EntityDamageByEntityEvent)) {
                        if (((AvosRace) TAqMinigames.minigame).isHotFloor(p)) {
                            p.setHealth(0);
                            event.setCancelled(true);
                        }
                    }
                }
            }
            if (TAqMinigames.minigame instanceof ProffersPit game) {
                if (!event.isCancelled()) {
                    if (ParticipantManager.getParticipants().contains(p)) {
                        game.damageTaken(event);
                    }
                }
            }

        }
    }
}
