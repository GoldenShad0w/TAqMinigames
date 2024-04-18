package goldenshadow.taqminigames.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import oshi.util.tuples.Triplet;


import java.util.ArrayList;
import java.util.List;

public class AuraAndVolleyData {

    public enum Map {
        OCEAN, SWAMP, CRYSTAL;
    }

    public final Triplet<Map, Map, Map> MAP;
    private final List<Location> START_LOCATION;
    private Location TUTORIAL_LOCATION = null;
    private final List<Location> TOWER_CENTERS;
    public int SURVIVE_POINTS = 600;
    public final int WIN_POINTS = 65;

    public AuraAndVolleyData(Map map1, Map map2, Map map3) {
        MAP = new Triplet<>(map1, map2, map3);
        START_LOCATION = new ArrayList<>(3);
        TOWER_CENTERS = new ArrayList<>(3);
        applyValues(map1);
        applyValues(map2);
        applyValues(map3);
    }

    private void applyValues(Map map) {
        switch (map) {
            case OCEAN -> {
                START_LOCATION.add(new Location(Bukkit.getWorld("minigames2"), 499.5, -25, 502.5, 0, 0));
                if (TUTORIAL_LOCATION == null) TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), 504.5, -20.0, 477.5, 0, 0);
                TOWER_CENTERS.add(new Location(Bukkit.getWorld("minigames2"), 499.5, -25, 519.5, -179, 0));
            }
            case SWAMP -> {
                START_LOCATION.add(new Location(Bukkit.getWorld("minigames2"), 500.5, -1, 995.5, 0, 0));
                if (TUTORIAL_LOCATION == null) TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), 496.5, 2.0, 977.5, 711, 6);
                TOWER_CENTERS.add(new Location(Bukkit.getWorld("minigames2"), 500.5, -1, 1014.5, 1259, 0));
            }
            case CRYSTAL -> {
                START_LOCATION.add(new Location(Bukkit.getWorld("minigames2"), 498.5, -2, 1495.5, 0, 0));
                if (TUTORIAL_LOCATION == null) TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), 499.5, 5.0, 1478.5, 363, 6);
                TOWER_CENTERS.add(new Location(Bukkit.getWorld("minigames2"), 498.5, -2, 1515.5, 1620, 1));
            }
            default -> throw new IllegalArgumentException("The selected map was null or doesn't exist!");
        }
    }

    public Location getStartLocation(int stage) {
        return START_LOCATION.get(stage);
    }

    public Location getTutorialLocation() {
        return TUTORIAL_LOCATION;
    }

    public Location getTowerCenter(int stage) {
        return TOWER_CENTERS.get(stage);
    }
}
