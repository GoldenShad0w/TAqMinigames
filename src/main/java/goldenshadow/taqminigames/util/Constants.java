package goldenshadow.taqminigames.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.util.BoundingBox;

public class Constants {

    public static final String WORLD_NAME = "minigames2";
    public static final World WORLD = Bukkit.getWorld(WORLD_NAME);
    public static Location LOBBY = new Location(Bukkit.getWorld("minigames2"), 132.5, 102.0, 170.5, 136, -4);
    public static Location LOBBY_GAME_SELECTION = new Location(Bukkit.getWorld("minigames2"), 44.5, 118.0, 78.5, 180, 0);
    public static Location LOBBY_POSSIBLE_GAME_RED = new Location(Bukkit.getWorld("minigames2"), 35.5, 118.0, 68.5, 2113, 4);
    public static Location LOBBY_POSSIBLE_GAME_LIME = new Location(Bukkit.getWorld("minigames2"), 44.5, 118.0, 64.5, 2159, -2);
    public static Location LOBBY_POSSIBLE_GAME_BLUE = new Location(Bukkit.getWorld("minigames2"), 53.5, 118.0, 68.5, 2207, -5);
    public static Location[] LOBBY_SELECTION_AREA = {new Location(Bukkit.getWorld("minigames2"), 56.5, 117.0, 72.5, 2255, 4), new Location(Bukkit.getWorld("minigames2"), 32.5, 117.0, 62.5, 2430, 25)};
    public static Location[] AURA_MAP_LOCATIONS = {new Location(Bukkit.getWorld("minigames2"), 499.5, -25, 502.5, 0, 0), new Location(Bukkit.getWorld("minigames2"), 500.5, -1, 995.5, 0, 0), new Location(Bukkit.getWorld("minigames2"), 498.5, -2, 1495.5, 0, 0)};
    public static Location AURA_TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), 504.5, -20.0, 477.5, 0, 0);
    public static Location[] AURA_TOWER_CENTERS = {new Location(Bukkit.getWorld("minigames2"), 499.5, -25, 519.5, -179, 0), new Location(Bukkit.getWorld("minigames2"), 500.5, -1, 1014.5, 1259, 0), new Location(Bukkit.getWorld("minigames2"), 498.5, -2, 1515.5, 1620, 1)};
    public static Location[] AVOS_MAP_LOCATIONS = {};
    public static BoundingBox[][] AVOS_STAGES = {};
    public static BoundingBox[][] AVOS_COLD_FLOORS = {};


    public static int AURA_SURVIVE = 600;
    public static final int AURA_WIN = 65;
    public static final int AVOS_FINISH = 500;
    public static final int AVOS_STAGE_COMPLETE = 300;
    public static final int AVOS_FALLOFF = 20;

}
