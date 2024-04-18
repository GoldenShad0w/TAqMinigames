package goldenshadow.taqminigames.util;

import goldenshadow.taqminigames.TAqMinigames;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;

import java.util.*;
import java.util.function.Predicate;

/**
 * A class representing a trigger that can be activated when a player enters its bounding box
 */
public class Trigger {

    private static final List<Trigger> registeredTriggers = new ArrayList<>();
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public BoundingBox boundingBox;
    private final Predicate<Player> predicate;
    private final Consumer<Player> task;
    private final long cooldown;
    private final boolean isCooldownGlobal;
    private long globalCooldown = 0;
    private final World world;
    private final boolean removeIfTriggered;
    private final UUID uuid;

    /**
     * Used to create a new trigger. The new object will automatically be saved in a list
     * @param boundingBox The bounding box of the trigger
     * @param world The world the bounding box is in
     * @param predicate A condition that must be met for the trigger to activate
     * @param task The task that should be performed when the trigger is activated
     * @param cooldown A cooldown in milliseconds determining how fast the trigger can be triggered again
     * @param isCooldownGlobal Whether the cooldown should apply to all players or just the player who activated the trigger
     * @param removeIfTriggered Whether the trigger object should be deleted after it has been activated
     */
    public Trigger(BoundingBox boundingBox, World world, Predicate<Player> predicate, Consumer<Player> task, long cooldown, boolean isCooldownGlobal, boolean removeIfTriggered) {
        this.boundingBox = boundingBox;
        this.predicate = predicate;
        this.task = task;
        this.world = world;
        this.cooldown = cooldown;
        this.isCooldownGlobal = isCooldownGlobal;
        this.removeIfTriggered = removeIfTriggered;
        uuid = UUID.randomUUID();

    }

    /**
     * Used to check if a player has activated a trigger and run the task connected to it if that is the case
     * @param player The player who should be checked
     */
    public static void checkTriggers(Player player) {
        Iterator<Trigger> it = registeredTriggers.iterator();
        while (it.hasNext()) {
            Trigger trigger = it.next();
            if (player.getWorld().equals(trigger.world)) {
                if (TAqMinigames.debugTriggers) {
                    debugHighlightBoxes(player, trigger.boundingBox);
                }
                if (trigger.boundingBox.overlaps(player.getBoundingBox())) {
                    if (trigger.predicate.test(player)) {
                        if (trigger.globalCooldown <= System.currentTimeMillis()) {
                            if (!trigger.cooldowns.containsKey(player.getUniqueId()) || trigger.cooldowns.get(player.getUniqueId()) <= System.currentTimeMillis()) {
                                trigger.cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + trigger.cooldown);
                                if (trigger.isCooldownGlobal) {
                                    trigger.globalCooldown = System.currentTimeMillis() + trigger.cooldown;
                                }
                                trigger.task.accept(player);
                                if (trigger.removeIfTriggered) {
                                    it.remove();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Used to register a new trigger
     * @param trigger The trigger to be registered
     */
    public static void register(Trigger trigger) {
        if (!registeredTriggers.contains(trigger)) {
            registeredTriggers.add(trigger);
        }
    }

    /**
     * Used to get the triggers uuid
     * @return The UUID
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Used to delete all triggers
     */
    public static void unregisterAll() {
        registeredTriggers.clear();
    }

    /**
     * Used to check if the list of registered triggers is empty
     * @return True if it is, false if it isn't
     */
    public static boolean isRegisterEmpty() {
        return registeredTriggers.isEmpty();
    }

    /**
     * Used to delete a specific trigger
     * @param trigger The trigger that should be removed
     */
    public static void unregister(Trigger trigger) {
        registeredTriggers.remove(trigger);
    }

    /**
     * Used to highlight trigger boxes for debug purposes
     * @param player The player who will see the particles
     * @param box The bounding box of the trigger
     */
    private static void debugHighlightBoxes(Player player, BoundingBox box) {
        // edges
        for (int i = 0; i < box.getWidthX(); i++) {
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMinX() + i, box.getMinY(), box.getMinZ()), 5, 0,0,0,0);
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMinX() + i, box.getMinY(), box.getMaxZ()), 5, 0,0,0,0);
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMinX() + i, box.getMaxY(), box.getMinZ()), 5, 0,0,0,0);
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMinX() + i, box.getMaxY(), box.getMaxZ()), 5, 0,0,0,0);
        }
        for (int i = 0; i < box.getWidthZ(); i++) {
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMinX(), box.getMinY(), box.getMinZ() + i), 5, 0,0,0,0);
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMaxX(), box.getMinY(), box.getMinZ() + i), 5, 0,0,0,0);
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMinX(), box.getMaxY(), box.getMinZ() + i), 5, 0,0,0,0);
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMaxX(), box.getMaxY(), box.getMinZ() + i), 5, 0,0,0,0);
        }
        for (int i = 0; i < box.getHeight(); i++) {
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMinX(), box.getMinY() + i, box.getMinZ()), 5, 0,0,0,0);
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMaxX(), box.getMinY() + i, box.getMinZ()), 5, 0,0,0,0);
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMinX(), box.getMinY() + i, box.getMaxZ()), 5, 0,0,0,0);
            player.spawnParticle(Particle.DRAGON_BREATH, new Location(player.getWorld(), box.getMaxX(), box.getMinY() + i, box.getMaxZ()), 5, 0,0,0,0);
        }
        //corners
        player.getWorld().spawnParticle(Particle.END_ROD, new Location(player.getWorld(), box.getMinX(), box.getMinY(), box.getMinZ()), 10, 0, 0, 0, 0);
        player.getWorld().spawnParticle(Particle.END_ROD, new Location(player.getWorld(), box.getMaxX(), box.getMinY(), box.getMinZ()), 10, 0, 0, 0, 0);
        player.getWorld().spawnParticle(Particle.END_ROD, new Location(player.getWorld(), box.getMinX(), box.getMinY(), box.getMaxZ()), 10, 0, 0, 0, 0);
        player.getWorld().spawnParticle(Particle.END_ROD, new Location(player.getWorld(), box.getMaxX(), box.getMinY(), box.getMaxZ()), 10, 0, 0, 0, 0);

        player.getWorld().spawnParticle(Particle.END_ROD, new Location(player.getWorld(), box.getMinX(), box.getMaxY(), box.getMinZ()), 10, 0, 0, 0, 0);
        player.getWorld().spawnParticle(Particle.END_ROD, new Location(player.getWorld(), box.getMaxX(), box.getMaxY(), box.getMinZ()), 10, 0, 0, 0, 0);
        player.getWorld().spawnParticle(Particle.END_ROD, new Location(player.getWorld(), box.getMinX(), box.getMaxY(), box.getMaxZ()), 10, 0, 0, 0, 0);
        player.getWorld().spawnParticle(Particle.END_ROD, new Location(player.getWorld(), box.getMaxX(), box.getMaxY(), box.getMaxZ()), 10, 0, 0, 0, 0);

    }
}
