package goldenshadow.taqminigames.minigames.haunting.ghosts;

import goldenshadow.taqminigames.minigames.haunting.HauntingLocation;
import jdk.jfr.Percentage;
import org.bukkit.inventory.ItemStack;

public abstract class Ghost {

    private final HauntingLocation hauntingLocation;
    @Percentage
    private float anger;


    public Ghost(HauntingLocation hauntingLocation) {
        this.hauntingLocation = hauntingLocation;
        anger = 0;

    }

    public abstract ItemStack getBountyPoster();
    
}
