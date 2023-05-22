package goldenshadow.taqminigames.minigames.aura_and_volley;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.AuraAndVolley;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Volley extends Attack{

    private final Location location;
    private Location missileLoc;

    public Volley(Location location) {
        this.location = location;
    }

    @Override
    public void tick() {

        if (tick == 2) {

            drawCircle(location);
            missileLoc = location.add(0, 20, 5);
        }
        if (tick > 12 && tick < 45) {
            assert missileLoc.getWorld() != null;
            missileLoc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, missileLoc, 10, 0.4,0.4,0.4, 0);
            missileLoc.getWorld().spawnParticle(Particle.FLAME, missileLoc, 10, 0.4,0.4,0.4, 0);
            missileLoc = missileLoc.add(0,-0.6,-0.2);
        }

        if (tick == 45) {
            assert missileLoc.getWorld() != null;
            missileLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, missileLoc.clone().add(0, 0.5,0), 0);
            missileLoc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, missileLoc.clone().add(0, 0.5,0), 10, 1,1,1,0);
            missileLoc.getWorld().spawnParticle(Particle.LAVA, missileLoc.clone().add(0, 0.5,0), 10, 1,1,1,0);
            missileLoc.getWorld().playSound(missileLoc, Sound.ENTITY_GENERIC_EXPLODE, 5,1);
            killNearby(missileLoc);
            isDone = true;
        }
        super.tick();
    }

    private void drawCircle(Location location) {
        for (double angle = 0; angle < 2 * Math.PI; angle += Math.PI / 36) {
            final double x = Math.cos(angle)*4;
            final double z = Math.sin(angle)*4;
            assert location.getWorld() != null;
            location.add(x, 0, z);
            location.getWorld().spawnParticle(Particle.FLAME, location, 0);
            location.subtract(x, 0, z);
        }
    }

    private void killNearby(Location location) {
        if (TAqMinigames.minigame instanceof AuraAndVolley game) {
            List<Player> killed = new ArrayList<>();
            for (Player p : game.alivePlayers) {
                if (p.getWorld().equals(location.getWorld())) {
                    if (p.getLocation().distance(location) <= 4.5) {
                        killed.add(p);
                    }
                }
            }
            for (Player p : killed) {
                game.onDeath(p);
            }
        }
    }
}
