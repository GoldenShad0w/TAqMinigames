package goldenshadow.taqminigames.data;

import goldenshadow.taqminigames.minigames.sky_island.CheckpointData;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SkyIslandData {

    public enum Map {
        SKY;
    }

    public final Map MAP;
    public final Location TUTORIAL_LOCATION;
    public final Location START_LOCATION;
    public final CheckpointData[] CHECKPOINTS;
    public final Location[] BOOST_PADS;
    public final int COMPLETE_POINTS = 200;
    public final int T1_POINTS = 20;
    public final int T2_POINTS = 30;
    public final int T3_POINTS = 50;
    public final int T4_POINTS = 100;
    public final int MYTHIC_POINTS = 200;
    public final int INGREDIENTS_POINTS = 20;

    public SkyIslandData(Map map) {
        MAP = map;
        switch (map) {
            case SKY -> {
                TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), -10186.5, 67.0, 9790.5, 12469, 17);
                START_LOCATION = new Location(Bukkit.getWorld("minigames2"), -10136.5, 19.0, 10070.5, 12783, -1);
                CHECKPOINTS = new CheckpointData[]{new CheckpointData(new Location(Bukkit.getWorld("minigames2"), -10135.5, 36.0, 9980.5, 3780, 1), new Location(Bukkit.getWorld("minigames2"), -10125.5, 31.41893705920733, 9986.5, 4053, 3), new Location(Bukkit.getWorld("minigames2"), -10143.5, 40.04567581423288, 9979.5, 4234, 32)), new CheckpointData(new Location(Bukkit.getWorld("minigames2"), -10136.5, 41.0, 9893.5, 4138, 0), new Location(Bukkit.getWorld("minigames2"), -10108.5, 38.65140772299044, 9907.5, 4407, 26), new Location(Bukkit.getWorld("minigames2"), -10154.5, 49.89577214314146, 9897.5, 4233, 33)), new CheckpointData(new Location(Bukkit.getWorld("minigames2"), -10147.5, 52.0, 9788.5, 4141, 6), new Location(Bukkit.getWorld("minigames2"), -10119.5, 48.55066454447191, 9806.5, 4407, 7), new Location(Bukkit.getWorld("minigames2"), -10160.5, 57.92604283641781, 9800.5, 4591, 29)), new CheckpointData(new Location(Bukkit.getWorld("minigames2"), -10154.5, 58.0, 9682.5, 4482, 13), new Location(Bukkit.getWorld("minigames2"), -10160.5, 61.86045477989018, 9674.5, 4600, 41), new Location(Bukkit.getWorld("minigames2"), -10151.5, 49.87289469357167, 9682.5, 4432, 27)), new CheckpointData(new Location(Bukkit.getWorld("minigames2"), -10140.5, -4.0, 9662.5, 4550, 2), new Location(Bukkit.getWorld("minigames2"), -10134.5, 0.6592816667641487, 9662.5, 4734, 29), new Location(Bukkit.getWorld("minigames2"), -10149.5, -9.0, 9676.5, 4559, 12)), new CheckpointData(new Location(Bukkit.getWorld("minigames2"), -10087.5, 4.0, 9564.5, 4863, 14), new Location(Bukkit.getWorld("minigames2"), -10089.5, 4.0, 9563.5, 4898, 17), new Location(Bukkit.getWorld("minigames2"), -10085.5, 6.532928275344887, 9559.5, 4913, 20)), new CheckpointData(new Location(Bukkit.getWorld("minigames2"), -10077.5, 81.0, 9529.5, 5224, 4), new Location(Bukkit.getWorld("minigames2"), -10082.5, 81.0, 9534.5, 5257, 12), new Location(Bukkit.getWorld("minigames2"), -10074.5, 83.86859856141189, 9526.5, 5257, 12)), new CheckpointData(new Location(Bukkit.getWorld("minigames2"), -10077.5, 83.0, 9471.5, 181, 1), new Location(Bukkit.getWorld("minigames2"), -10071.5, 79.01239458081051, 9475.5, 136, 18), new Location(Bukkit.getWorld("minigames2"), -10081.5, 86.8764998829287, 9466.5, 134, 20))};

                BOOST_PADS = new Location[]{new Location(Bukkit.getWorld("minigames2"), -10118.5, 7.0, 9573.5, 2360, 88), new Location(Bukkit.getWorld("minigames2"), -10102.5, 7.0, 9584.5, 2449, 90), new Location(Bukkit.getWorld("minigames2"), -10074.5, 3.0, 9588.5, 2430, 90), new Location(Bukkit.getWorld("minigames2"), -10087.5, 4.0, 9561.5, 2383, 90), new Location(Bukkit.getWorld("minigames2"), -10077.5, 22.0, 9552.5, 2466, 90), new Location(Bukkit.getWorld("minigames2"), -10072.5, 58.0, 9545.5, 2373, 90)};
            }
            default -> throw new IllegalArgumentException("The selected map was null or doesn't exist!");
        }
    }
}
