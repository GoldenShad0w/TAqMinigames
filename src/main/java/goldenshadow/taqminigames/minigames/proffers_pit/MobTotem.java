package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.event.BossbarWrapper;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class MobTotem extends ProfEvent {

    private final UUID totemUUID;
    private final UUID barUUID;

    public MobTotem(EventLocation location) {
        super("Someone has placed a mob totem in the %s! Dangerous mobs will now spawn there!", location, true);
        totemUUID = spawnTotem();
        barUUID = BossbarWrapper.createBossbar(ChatColor.RED +"Mob Totem in the " + location.area().getName(), BarColor.RED, BarStyle.SOLID, 1);
    }

    @Override
    public void tick() {
        if (tick % 12 == 0) {
            spawnMob();
            spawnMob();
        }
        if (tick > 60) {
            isDone = true;
            deleteTotem();
            BossbarWrapper.destroyBossbar(barUUID);
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("The mob totem in the " + location.area().getName() + " has run out!"));
        }
        super.tick();

    }

    private UUID spawnTotem() {
        assert location.location().getWorld() != null;
        ArmorStand a = (ArmorStand) location.location().getWorld().spawnEntity(location.location(), EntityType.ARMOR_STAND, false);
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
        assert location.location().getWorld() != null;
        ZombieVillager mob = (ZombieVillager) location.location().getWorld().spawnEntity(location.location(), EntityType.ZOMBIE_VILLAGER, false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "aurum api spawn_mob " + location.location().getWorld().getName() + " " + location.location().getX() + " " + location.location().getY() + " " + location.location().getZ() + " " + Constants.PROF_MOB_NAME + " " + mob.getUniqueId());
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
