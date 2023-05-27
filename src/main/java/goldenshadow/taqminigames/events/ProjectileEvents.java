package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.minigames.NesaakFight;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileEvents implements Listener {

    @EventHandler
    public void projectileHit(ProjectileHitEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof NesaakFight game) {
                if (event.getEntity() instanceof Snowball snowball) {
                    if (event.getHitEntity() instanceof Player p) {
                        if (ParticipantManager.getParticipants().contains(p)) {
                            if (snowball.getShooter() instanceof Player) {
                                game.playerHit(p, ((Player) snowball.getShooter()));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void projectileShoot(ProjectileLaunchEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof NesaakFight game) {
                if (event.getEntity() instanceof Snowball snowball) {
                    if (snowball.getShooter() instanceof Player p) {
                        if (ParticipantManager.getParticipants().contains(p)) {
                            game.snowballShoot(snowball, p);
                        }
                    }
                }
            }
        }
    }
}
