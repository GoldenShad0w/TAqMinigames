package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Trigger;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

public class GasLeak extends ProfEvent {

    private final Trigger trigger;
    int offset = 1;

    public GasLeak() {
        super ("A gas leak has occurred somewhere in the %s!");
        BoundingBox box = new BoundingBox(location.getX()+1,location.getY()+1,location.getZ()+1,location.getX()-1,location.getY()-1,location.getZ()-1);
        trigger = new Trigger(box, Constants.WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 1, false, true,true)), Utilities.secondsToMillis(2), false, false);
    }

    @Override
    public void tick() {
        assert Constants.WORLD != null;
        Constants.WORLD.spawnParticle(Particle.SNEEZE, trigger.boundingBox.getCenter().toLocation(Constants.WORLD), 100*offset, offset, offset, offset, 0);
        if (tick % 10 == 0) {
            trigger.boundingBox = trigger.boundingBox.expand(1.6,1.6,1.6);
            offset++;
        }
        if (tick > 60) {
            Trigger.unregister(trigger);
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("The gas in the " + area + " has dissipated!"));
            isDone = true;
        }
        super.tick();
    }
}
