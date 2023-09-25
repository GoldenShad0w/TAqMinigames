package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.event.BossbarWrapper;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Trigger;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Earthquake extends ProfEvent {

    private final Trigger trigger;
    private final BlockData dirt = Material.DIRT.createBlockData();
    private final BoundingBox box;
    private final UUID barUUID;

    public Earthquake(EventLocation location) {
        super ("An earthquake has occurred somewhere in the %s!", location, true);
        BoundingBox box = new BoundingBox(location.location().getX()+10,location.location().getY()+10,location.location().getZ()+10,location.location().getX()-10,location.location().getY()-10,location.location().getZ()-10);
        this.box = box;
        trigger = new Trigger(box, Constants.WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
            p.damage(1);
            p.setVelocity(new Vector(ThreadLocalRandom.current().nextDouble(0,0.2),ThreadLocalRandom.current().nextDouble(0,0.2),ThreadLocalRandom.current().nextDouble(0,0.2)));
        }, Utilities.secondsToMillis(1), true, false);
        Trigger.register(trigger);
        barUUID = BossbarWrapper.createBossbar( ChatColor.LIGHT_PURPLE + "Earthquake in the " + location.area().getName(), BarColor.PURPLE, BarStyle.SOLID, 1);
    }

    @Override
    public void tick() {
        assert Constants.WORLD != null;
        Constants.WORLD.spawnParticle(Particle.BLOCK_CRACK, location.location(), 100, box.getWidthX()/4, 0.1, box.getWidthZ()/4, dirt);
        if (tick > 60 ) {
            Trigger.unregister(trigger);
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("The earthquake in the " + location.area().getName() + " has ended!"));
            BossbarWrapper.destroyBossbar(barUUID);
            isDone = true;
        }
        super.tick();
    }
}
