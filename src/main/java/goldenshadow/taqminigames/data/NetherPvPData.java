package goldenshadow.taqminigames.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class NetherPvPData {

    public enum Map {
        NETHER;
    }

    public final Map MAP;
    public final Location TUTORIAL_LOCATION;
    public final Location[] SPAWN_LOCATIONS;
    public final String[] ITEM_NAMES;
    public final int STAGE_COMPLETE_POINTS = 200;
    public final int FINISH_POINTS = 400;

    /**
     * Constructor for this data class
     * @param map The map that will be played
     */
    public NetherPvPData(@NotNull Map map) {
        MAP = map;
        switch (map) {
            case NETHER -> {
                TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), -19992.5, 33.0, -10018.5, 239, 14);

                SPAWN_LOCATIONS = new Location[]{new Location(Bukkit.getWorld("minigames2"), -19927.5, 16.0, -10028.5, 61, 5), new Location(Bukkit.getWorld("minigames2"), -19962.5, 16.0, -10054.5, 16, 4), new Location(Bukkit.getWorld("minigames2"), -20013.5, 16.0, -10090.5, -8, 12), new Location(Bukkit.getWorld("minigames2"), -20058.5, 16.0, -10060.5, -21, 5), new Location(Bukkit.getWorld("minigames2"), -20084.5, 36.0, -10001.5, -92, 5), new Location(Bukkit.getWorld("minigames2"), -20064.5, 16.0, -9938.5, 226, -3), new Location(Bukkit.getWorld("minigames2"), -20018.5, 16.0, -9909.5, 172, 4), new Location(Bukkit.getWorld("minigames2"), -19964.5, 16.0, -9918.5, 178, 11), new Location(Bukkit.getWorld("minigames2"), -19927.5, 16.0, -9957.5, 116, 3), new Location(Bukkit.getWorld("minigames2"), -19980.5, 16.0, -9978.5, 118, 0)};

                ITEM_NAMES = new String[]{"m_nether_1","m_nether_2","m_nether_3","m_nether_4","m_nether_5","m_nether_6","m_nether_7","m_nether_8"};
            }
            default -> throw new IllegalArgumentException("The selected map was null or doesn't exist!");
        }
    }
}
