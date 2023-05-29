package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MobTotem extends ProfEvent {

    private final UUID totemUUID;

    public MobTotem() {
        super("Someone has placed a mob totem in the %s! Dangerous mobs will now spawn there!");
        totemUUID = spawnTotem();
    }

    @Override
    public void tick() {
        if (tick % 5 == 0) {
            spawnMob();
            spawnMob();
            spawnMob();
        }
        if (tick > 60) {
            isDone = true;
            deleteTotem();
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("The mob totem in the " + area + " has run out!"));
        }
        super.tick();

    }

    private UUID spawnTotem() {
        assert location.getWorld() != null;
        ArmorStand a = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, false);
        assert a.getEquipment() != null;
        ItemStack i = new ItemStack(Material.STICK);
        ItemMeta meta = i.getItemMeta();
        assert meta != null;
        meta.setCustomModelData(100023);
        i.setItemMeta(meta);
        a.getEquipment().setHelmet(i);
        a.setMarker(true);
        a.setVisible(false);
        return a.getUniqueId();
    }

    private void spawnMob() {
        assert location.getWorld() != null;
        ZombieVillager mob = (ZombieVillager) location.getWorld().spawnEntity(location, EntityType.ZOMBIE_VILLAGER, false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "aurum api spawn_mob " + location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + Constants.PROF_MOB_NAME + " " + mob.getUniqueId());
            mob.setVelocity(getRandomVelocityVector());
        }, 1L);

    }

    private static Vector getRandomVelocityVector() {
        return new Vector(ThreadLocalRandom.current().nextDouble(-0.8,0.8),0.5, ThreadLocalRandom.current().nextDouble(-0.8,0.8));
    }

    public void deleteTotem() {
        Entity e = Bukkit.getEntity(totemUUID);
        if (e != null) {
            e.remove();
        }
    }
}
