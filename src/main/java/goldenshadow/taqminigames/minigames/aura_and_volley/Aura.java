package goldenshadow.taqminigames.minigames.aura_and_volley;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.AuraAndVolley;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * A class representing an aura attack
 */
public class Aura extends Attack{

    private final Location location;

    /**
     * Creates a new aura attack
     * @param sourceLoc The location from where it should originate
     */
    public Aura(Location sourceLoc) {
        location = sourceLoc.clone().add(0,0.2,0);
    }

    /**
     * 20hz loop
     */
    @Override
    public void tick() {

        if (tick > 0 && tick < 120) {
            assert location.getWorld() != null;
            if (tick % 2 == 0) {
                drawCircle(location.clone(), (tick/2));
            }
        }
        if (tick == 120) isDone = true;
        super.tick();
    }

    /**
     * Internal method used to draw the aura circle
     * @param location The location
     * @param size The size
     */
    private void drawCircle(Location location, int size) {
        double angle = 0;
        final double maxAngle = 2 * Math.PI;
        final double angleIncrement = Math.PI / 48;
        final Particle particle = Particle.CRIT;

        while (angle < maxAngle) {
            final double x = Math.cos(angle)*size;
            final double z = Math.sin(angle)*size;

            location.add(x, 0, z);
            assert location.getWorld() != null;
            location.getWorld().spawnParticle(particle, location, 10,0.2,0.2,0.2,0);
            killNearby(location);
            location.subtract(x, 0, z);

            angle += angleIncrement;
        }
    }

    /**
     * Internal method used to kill players hit by the aura
     * @param loc The location of the aura
     */
    private void killNearby(Location loc) {
        if (TAqMinigames.minigame instanceof AuraAndVolley game) {
            if (loc.getWorld() != null) {
                for (Entity e : loc.getWorld().getNearbyEntities(loc, 1, 0.2, 1)) {
                    if (e instanceof Player p) {
                        if (game.alivePlayers.contains(p)) {
                            game.onDeath(p);
                        }
                    }
                }
            }
        }
    }
}
