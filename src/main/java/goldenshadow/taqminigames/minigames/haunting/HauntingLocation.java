package goldenshadow.taqminigames.minigames.haunting;

import org.bukkit.Location;

public class HauntingLocation {

    private final String description;
    private final Location[] nodes;
    private int nodeIndex;
    private Location currentLocation;

    public HauntingLocation(String description, Location... nodes) {
        this.description = description;
        this.nodes = nodes;
        nodeIndex = 0;
        currentLocation = nodes[0];
    }

    public void init() {

    }
}
