package goldenshadow.taqminigames.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

public class Constants {

    public static final String WORLD_NAME = "minigames2";
    public static final World WORLD = Bukkit.getWorld(WORLD_NAME);
    public static final Location LOBBY = new Location(Bukkit.getWorld("minigames2"), 132.5, 102.0, 170.5, 136, -4);
    public static final Location LOBBY_GAME_SELECTION = new Location(Bukkit.getWorld("minigames2"), 44.5, 118.0, 78.5, 180, 0);
    public static final Location LOBBY_POSSIBLE_GAME_RED = new Location(Bukkit.getWorld("minigames2"), 35.5, 118.0, 68.5, 2113, 4);
    public static final Location LOBBY_POSSIBLE_GAME_LIME = new Location(Bukkit.getWorld("minigames2"), 44.5, 118.0, 64.5, 2159, -2);
    public static final Location LOBBY_POSSIBLE_GAME_BLUE = new Location(Bukkit.getWorld("minigames2"), 53.5, 118.0, 68.5, 2207, -5);
    public static final Location[] LOBBY_SELECTION_AREA = {new Location(Bukkit.getWorld("minigames2"), 56.5, 117.0, 72.5, 2255, 4), new Location(Bukkit.getWorld("minigames2"), 32.5, 117.0, 62.5, 2430, 25)};
    public static final Location[] AURA_MAP_LOCATIONS = {new Location(Bukkit.getWorld("minigames2"), 499.5, -25, 502.5, 0, 0), new Location(Bukkit.getWorld("minigames2"), 500.5, -1, 995.5, 0, 0), new Location(Bukkit.getWorld("minigames2"), 498.5, -2, 1495.5, 0, 0)};
    public static final Location AURA_TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), 504.5, -20.0, 477.5, 0, 0);
    public static final Location[] AURA_TOWER_CENTERS = {new Location(Bukkit.getWorld("minigames2"), 499.5, -25, 519.5, -179, 0), new Location(Bukkit.getWorld("minigames2"), 500.5, -1, 1014.5, 1259, 0), new Location(Bukkit.getWorld("minigames2"), 498.5, -2, 1515.5, 1620, 1)};
    public static final Location[] AVOS_MAP_LOCATIONS = {};
    public static final BoundingBox[][] AVOS_STAGES = {};
    public static final BoundingBox[][] AVOS_COLD_FLOORS = {};
    public static final BoundingBox[] AVOS_FINISH_BOXES = {};
    public static final Location NESAAK_TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), -174.5, -60, 235.5);
    public static final Location[] NESAAK_SPAWN_POINTS = {new Location(Bukkit.getWorld("minigames2"), -177.5, -60, 231.5)};
    public static final Location[] NESAAK_POWERUP_NODES = {new Location(Bukkit.getWorld("minigames2"), -180.5, -60, 235.5)};
    public static final Location PROF_TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), -117.5, -60, 266.5, 362, 18);
    public static final Location PROF_START_LOCATION = new Location(Bukkit.getWorld("minigames2"), -122.5, -60, 266.5, 353, 6);
    public static final Location[][] PROF_EVENT_LOCATIONS = {{new Location(Bukkit.getWorld("minigames2"), -135.5, -60, 268.5, 621, 20)},{new Location(Bukkit.getWorld("minigames2"), -132.5, -60, 253.5, 641, 3)},{new Location(Bukkit.getWorld("minigames2"), -119.5, -60, 251.5, 664, 46)},{new Location(Bukkit.getWorld("minigames2"), -107.5, -60, 258.5, 655, 90)},{new Location(Bukkit.getWorld("minigames2"), -102.5, -60, 271.5, 809, 89)}, {new Location(Bukkit.getWorld("minigames2"), -112.5, -60, 285.5, 833, 90)}};
    public static final int PROF_T2_PRICE = 30;
    public static final int PROF_T3_PRICE = 60;
    public static final int PROF_T4_PRICE = 150;
    public static final int PROF_T5_PRICE = 320;
    public static final int PROF_T6_PRICE = 600;
    public static final int PROF_BOOTS_PRICE = 200;
    public static final int PROF_SWORD_PRICE = 300;
    public static final int PROF_SPEED_PRICE = 100;
    public static final int PROF_HASTE_PRICE = 150;
    public static final int PROF_HEALTH_PRICE = 80;
    public static final int PROF_HUNTED_PRICE = 200;
    public static final String PROF_MOB_NAME = "m_prof_mob";


    public static int AURA_SURVIVE = 600;
    public static final int AURA_WIN = 65;
    public static final int AVOS_FINISH = 500;
    public static final int AVOS_STAGE_COMPLETE = 300;
    public static final int AVOS_FALLOFF = 20;
    public static final int GENERIC_MAX_POINTS = 2000;
    public static final int GENERIC_FALLOFF = 200;

}
