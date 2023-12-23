package goldenshadow.taqminigames.minigames.excavation;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Used to store data about a dart trap
 * @param location The location of where it is
 * @param vector The normalised vector of where it should shoot
 */
public record DartTrapData(Location location, Vector vector) {
}
