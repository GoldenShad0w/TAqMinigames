package goldenshadow.taqminigames.minigames.excavation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class Shockwave {

    private final Location center;
    private int tick;
    public boolean isDone = false;

    public Shockwave(Location location) {
        center = location.clone().add(0, 0.5, 0);
    }

    public void tick() {
        tick++;
        if (tick == 1) {
            assert center.getWorld() != null;
            center.getWorld().playSound(center, Sound.ENTITY_WARDEN_SONIC_BOOM, 1,1);
        }
        if (tick > 0 && tick < 30) {
            if (tick % 3 == 0) {
                drawCircle(center.clone(), (tick)/2);
            }
        }
        if (tick == 60) {
            isDone = true;
        }
    }


    private void drawCircle(Location location, int size) {
        double angle = 0;
        final double maxAngle = 2 * Math.PI;
        final double angleIncrement = Math.PI / 16;
        final Particle particle = Particle.CRIT;

        while (angle < maxAngle) {
            final double x = Math.cos(angle)*size;
            final double z = Math.sin(angle)*size;

            location.add(x, 0, z);
            assert location.getWorld() != null;
            location.getWorld().spawnParticle(particle, location, 5,0.2,0.2,0.2,0);
            damageNearby(location);
            location.subtract(x, 0, z);

            angle += angleIncrement;
        }
    }

    private void damageNearby(Location loc) {
        assert loc.getWorld() != null;
        for (Entity e : loc.getWorld().getNearbyEntities(loc,1,0.2,1)) {
            if (e instanceof Player p) {
                if (Bukkit.getOnlinePlayers().contains(p)) {
                    p.damage(1);
                    p.setVelocity(calculateKnockbackVector(center, p.getLocation()));
                }
            }
        }
    }

    private static Vector calculateKnockbackVector(Location source, Location target) {
        Vector v = target.toVector().subtract(source.toVector()).normalize();
        v.setX(v.getX()*0.4);
        v.setZ(v.getZ()*0.4);
        v.setY(0.5);
        return v;
    }
}
