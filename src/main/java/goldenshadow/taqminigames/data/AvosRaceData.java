package goldenshadow.taqminigames.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

public class AvosRaceData {

    public enum Map {
        CORKUS;
    }

    public final Map MAP;
    public final Location START_LOCATION;
    public final BoundingBox[] STAGES;
    public final BoundingBox[] COLD_FLOORS;
    public final BoundingBox FINISH_BOX;
    public final Location[] START_BARRIERS;
    public final Location[] START_AIR;
    public final int FINISH_POINTS = 500;
    public final int STAGE_COMPLETE_POINTS = 300;
    public final int FALLOFF_POINTS = 20;

    public AvosRaceData(Map map) {
        MAP = map;
        switch (map) {
            case CORKUS -> {
                START_LOCATION = new Location(Bukkit.getWorld("minigames2"), -914.5, 288.0, 894.5, 995, 3);
                STAGES = new BoundingBox[]{new BoundingBox(-835, 215, 912, -855, 182, 868), new BoundingBox(-875, 134, 833, -933, 78, 845), new BoundingBox(-823, 91, 910, -890, 22, 916), new BoundingBox(-946, 49, 933, -921, 4, 993), new BoundingBox(-933, -43, 944, -996, 5, 915)};
                COLD_FLOORS = new BoundingBox[]{new BoundingBox(-915, -48, 899, -905, -43, 909), new BoundingBox(-908, 282, 903, -921, 297, 879)};
                FINISH_BOX = new BoundingBox(-915, -48, 899, -905, -43, 909);
                START_BARRIERS = new Location[]{new Location(Bukkit.getWorld("minigames2"), -911.5, 288.0, 895.5, 2306, 13), new Location(Bukkit.getWorld("minigames2"), -915.5, 293.0, 889.5, 2414, 72)};
                START_AIR = new Location[]{new Location(Bukkit.getWorld("minigames2"), -912.5, 288.0, 894.5, 2317, 36), new Location(Bukkit.getWorld("minigames2"), -914.5, 292.0, 890.5, 2392, 86)};
            }
            default -> throw new IllegalArgumentException("The selected map was null or doesn't exist!");
        }
    }
}
