package goldenshadow.taqminigames.minigames.sky_island;

import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public record CheckpointData(Location respawnPoint, Location pos1, Location pos2) {

    public BoundingBox getBoundingBox() {
        return new BoundingBox(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
    }
}
