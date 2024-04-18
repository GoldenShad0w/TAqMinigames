package goldenshadow.taqminigames.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class GenericData {

    public final String WORLD_NAME = "minigames2";
    public final World WORLD = Bukkit.getWorld(WORLD_NAME);
    public final UUID LOBBY_LEADERBOARD_UUID = UUID.fromString("f3793505-cb8b-45f4-b0e4-b551ff4e2af2");
    public final Location LOBBY = new Location(Bukkit.getWorld("minigames2"), 132.5, 102.0, 170.5, 136, -4);
    public final Location[] LOBBY_JUMP_PADS = {new Location(Bukkit.getWorld("minigames2"), 112.5, 103.0, 201.5, 817, 88), new Location(Bukkit.getWorld("minigames2"), 96.5, 104.0, 200.5, 824, 90), new Location(Bukkit.getWorld("minigames2"), 78.5, 105.0, 200.5, 826, 89), new Location(Bukkit.getWorld("minigames2"), 67.5, 105.0, 185.5, 951, 88), new Location(Bukkit.getWorld("minigames2"), 78.5, 106.0, 173.5, 973, 86), new Location(Bukkit.getWorld("minigames2"), 94.5, 105.0, 184.5, 1094, 89), new Location(Bukkit.getWorld("minigames2"), 58.5, 108.0, 162.5, 132, -1), new Location(Bukkit.getWorld("minigames2"), 44.5, 111.0, 148.5, 168, 34)};
    public final Location LOBBY_GAME_SELECTION = new Location(Bukkit.getWorld("minigames2"), 44.5, 118.0, 78.5, 180, 0);
    public final Location LOBBY_POSSIBLE_GAME_RED = new Location(Bukkit.getWorld("minigames2"), 35.5, 120.0, 68.5, 2113, 4);
    public final Location LOBBY_POSSIBLE_GAME_LIME = new Location(Bukkit.getWorld("minigames2"), 44.5, 120.0, 64.5, 2159, -2);
    public final Location LOBBY_POSSIBLE_GAME_BLUE = new Location(Bukkit.getWorld("minigames2"), 53.5, 120.0, 68.5, 2207, -5);
    public final Location[] LOBBY_SELECTION_AREA = {new Location(Bukkit.getWorld("minigames2"), 56.5, 117.0, 72.5, 2255, 4), new Location(Bukkit.getWorld("minigames2"), 32.5, 117.0, 62.5, 2430, 25)};
    public final Location LOBBY_WINNER_LOCATION = new Location(Bukkit.getWorld("minigames2"), 44.5, 120.0, 78.5, 719, 0);
    public final Location LOBBY_WINNER_REST_LOCATION = new Location(Bukkit.getWorld("minigames2"), 44.5, 118.0, 92.5, 540, -3);
    public final Location[] LOBBY_PODIUM_TEMPLATE = {new Location(Bukkit.getWorld("minigames2"), 61.5, 78.0, 105.5, 1588, 47), new Location(Bukkit.getWorld("minigames2"), 47.5, 90.0, 95.5, 1403, 37)};
    public final Location[] LOBBY_PODIUM_LOCATION = {new Location(Bukkit.getWorld("minigames2"), 37.5, 118.0, 73.5, 971, 14), new Location(Bukkit.getWorld("minigames2"), 51.5, 131.7370889756015, 83.5, 1199, 19)};
    public final Location[] LOBBY_WINNER_FIREWORK_LOCATIONS = {new Location(Bukkit.getWorld("minigames2"), 46.5, 120.0, 83.5, 1795, 90), new Location(Bukkit.getWorld("minigames2"), 42.5, 120.0, 83.5, 1804, 89), new Location(Bukkit.getWorld("minigames2"), 40.5, 120.0, 82.5, 1890, 89), new Location(Bukkit.getWorld("minigames2"), 38.5, 120.0, 81.5, 1896, 90), new Location(Bukkit.getWorld("minigames2"), 37.5, 120.0, 80.5, 2026, 90), new Location(Bukkit.getWorld("minigames2"), 37.5, 120.0, 76.5, 1984, 89), new Location(Bukkit.getWorld("minigames2"), 38.5, 120.0, 75.5, 2063, 90), new Location(Bukkit.getWorld("minigames2"), 40.5, 120.0, 74.5, 1979, 90), new Location(Bukkit.getWorld("minigames2"), 42.5, 120.0, 73.5, 2070, 89), new Location(Bukkit.getWorld("minigames2"), 46.5, 120.0, 73.5, 2070, 90), new Location(Bukkit.getWorld("minigames2"), 48.5, 120.0, 74.5, 2224, 90), new Location(Bukkit.getWorld("minigames2"), 50.5, 120.0, 75.5, 2067, 90), new Location(Bukkit.getWorld("minigames2"), 51.5, 120.0, 76.5, 2076, 90), new Location(Bukkit.getWorld("minigames2"), 51.5, 120.0, 80.5, 2173, 89), new Location(Bukkit.getWorld("minigames2"), 50.5, 120.0, 81.5, 2206, 90), new Location(Bukkit.getWorld("minigames2"), 48.5, 120.0, 82.5, 2225, 90)};

    public final int GENERIC_MAX_POINTS = 2000;
    public final int GENERIC_FALLOFF = 200;
}
