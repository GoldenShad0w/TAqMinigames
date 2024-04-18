package goldenshadow.taqminigames.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class NesaakData {

    public enum Map {
        VALLEY_TOWN
    }

    public final Map MAP;
    public final Location TUTORIAL_LOCATION;
    public final Location[] SPAWN_LOCATIONS;
    public final Location[] POWERUP_NODES;

    public NesaakData(Map map) {
        MAP = map;
        switch (map) {
            case VALLEY_TOWN -> {
                TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), -10012.5, 16.0, -10010.5, 555, 15);
                SPAWN_LOCATIONS = new Location[]{new Location(Bukkit.getWorld("minigames2"), -10030.5, 12.0, -9923.5, 904, 0), new Location(Bukkit.getWorld("minigames2"), -10046.5, 1.0, -9946.5, 1127, 0), new Location(Bukkit.getWorld("minigames2"), -10054.5, 1.0, -10011.5, 1381, -3), new Location(Bukkit.getWorld("minigames2"), -10059.5, 1.0, -10034.5, 988, -2), new Location(Bukkit.getWorld("minigames2"), -10026.5, 3.0, -10065.5, 1053, -2), new Location(Bukkit.getWorld("minigames2"), -9986.5, 6.0, -10050.5, 1248, 3), new Location(Bukkit.getWorld("minigames2"), -9988.5, 1.0, -10080.5, 1034, -1), new Location(Bukkit.getWorld("minigames2"), -9957.5, 1.0, -10069.5, 1085, -7), new Location(Bukkit.getWorld("minigames2"), -9978.5, 2.0, -10021.5, 1217, -5), new Location(Bukkit.getWorld("minigames2"), -9965.5, 1.0, -9996.5, 1143, 0), new Location(Bukkit.getWorld("minigames2"), -9928.5, 1.125, -9972.5, 1566, 0), new Location(Bukkit.getWorld("minigames2"), -9966.5, 10.0, -9936.5, 1561, 0), new Location(Bukkit.getWorld("minigames2"), -9988.5, 1.0, -9918.5, 1609, 0),new Location(Bukkit.getWorld("minigames2"), -9970.5, 3.0, -10044.5, 1571, -1)};
                POWERUP_NODES = new Location[]{new Location(Bukkit.getWorld("minigames2"), -10013.5, 9.0, -9977.5, 1990, -4), new Location(Bukkit.getWorld("minigames2"), -10016.5, 17.5, -9932.5, 1644, 7), new Location(Bukkit.getWorld("minigames2"), -10021.5, 1.0, -9932.5, 1756, -2), new Location(Bukkit.getWorld("minigames2"), -9985.5, 1.0, -9933.5, 1663, -2), new Location(Bukkit.getWorld("minigames2"), -9957.5, 10.0, -9957.5, 1566, 5), new Location(Bukkit.getWorld("minigames2"), -9950.5, 15.0, -9998.5, 1834, -3), new Location(Bukkit.getWorld("minigames2"), -9918.5, 5.0, -10025.5, 1531, 1), new Location(Bukkit.getWorld("minigames2"), -9946.5, 1.0, -10053.5, 1490, 0), new Location(Bukkit.getWorld("minigames2"), -10051.5, 1.0, -10057.5, 1387, 6), new Location(Bukkit.getWorld("minigames2"), -9990.5, 6.0, -10060.5, 1466, 1), new Location(Bukkit.getWorld("minigames2"), -10011.5, 6.0, -10028.5, 1627, 0), new Location(Bukkit.getWorld("minigames2"), -10077.5, 2.0, -10014.5, 1381, -1), new Location(Bukkit.getWorld("minigames2"), -10056.5, 5.0, -9972.5, 1352, -2)};
            }
            default -> throw new IllegalArgumentException("The selected map is null or doesn't exist!");
        }
    }
}
