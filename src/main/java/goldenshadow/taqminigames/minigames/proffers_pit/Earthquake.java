package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Trigger;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class Earthquake extends ProfEvent {

    private final Trigger trigger;
    private final BlockData dirt = Material.DIRT.createBlockData();
    private final BoundingBox box;

    public Earthquake(EventLocation location) {
        super ("An earthquake has occurred somewhere in the %s!", location);
        BoundingBox box = new BoundingBox(location.location().getX()+10,location.location().getY()+10,location.location().getZ()+10,location.location().getX()-10,location.location().getY()-10,location.location().getZ()-10);
        this.box = box;
        trigger = new Trigger(box, Constants.WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
            p.damage(1);
            p.setVelocity(new Vector(ThreadLocalRandom.current().nextDouble(0,0.2),ThreadLocalRandom.current().nextDouble(0,0.2),ThreadLocalRandom.current().nextDouble(0,0.2)));
        }, Utilities.secondsToMillis(1), true, false);
        Trigger.register(trigger);
    }

    @Override
    public void tick() {
        assert Constants.WORLD != null;
        Constants.WORLD.spawnParticle(Particle.BLOCK_CRACK, location.location(), 100, box.getWidthX()/4, 0.1, box.getWidthZ()/4, dirt);
        if (tick > 60 ) {
            Trigger.unregister(trigger);
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("The earthquake in the " + location.area().getName() + " has ended!"));
            isDone = true;
        }
        super.tick();
    }
}
