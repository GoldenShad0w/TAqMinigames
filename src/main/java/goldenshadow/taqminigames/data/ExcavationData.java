package goldenshadow.taqminigames.data;

import goldenshadow.taqminigames.minigames.excavation.DartTrapData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.UUID;

public class ExcavationData {

    public enum Map {
        JUNGLE,
        DESERT;
    }

    public final Map MAP;
    public final Location START_LOCATION;
    public final Location TUTORIAL_LOCATION;
    public final Location[] ENTRANCE_WALL;
    public final Location[][] SPIKE_BOXES;
    public final Location[][] FLAMETHROWER_BOXES;
    public final DartTrapData[] DART_TRAPS;
    @Nullable
    public final Location SHOCKWAVE_LOCATION;
    public final Location[] DOOR_RED;
    public final Location[] DOOR_BLUE;
    public final Location[] DOOR_GREEN;
    public final Location[] DOOR_YELLOW;
    public final Location END_LOCATION;
    public final Location[] GLITTER_LOCATIONS;
    public final UUID KEY_RED_UUID;
    public final UUID KEY_BLUE_UUID;
    public final UUID KEY_GREEN_UUID;
    public final UUID KEY_YELLOW_UUID;
    @Nullable
    public final UUID HERB_UUID;
    public final String BETTER_SWORD_NAME;
    @Nullable
    public final Location[] FALLING_CEILING_BOX;
    public int CRYSTAL_POINTS = 100;
    public int EMERALDS_SMALL_POINTS = 10;
    public int EMERALDS_MEDIUM_POINTS = 20;
    public int EMERALDS_LARGE_POINTS = 40;
    public int FIRST_FOUND_POINTS = 10;


    public ExcavationData(Map map) {
        MAP = map;
        switch (map) {
            case JUNGLE -> {
                START_LOCATION = new Location(Bukkit.getWorld("minigames2"), 10044.5, 7.0, 9896.5, 441, 0);
                TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), 9975.5, 7.0, 9889.5, 419, 8);
                ENTRANCE_WALL = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10028.5, 4.0, 9901.5, 35, 88),new Location(Bukkit.getWorld("minigames2"), 10028.5, 8.0, 9897.5, 530, 89)};
                SPIKE_BOXES = new Location[][]{{new Location(Bukkit.getWorld("minigames2") ,10023.0, -11.5, 9861.0), new Location(Bukkit.getWorld("minigames2"), 10020.0, -10.0, 9881.0)}, {new Location(Bukkit.getWorld("minigames2"), 10005.0, -10.0, 9907.0), new Location(Bukkit.getWorld("minigames2"), 9998.0, -11.5, 9913.0)}, {new Location(Bukkit.getWorld("minigames2"), 9958.0, -5.0, 9910.0), new Location(Bukkit.getWorld("minigames2"), 9951.0, -6.5, 9916.0)}, {new Location(Bukkit.getWorld("minigames2"), 9974.0, -5.0, 9984.0), new Location(Bukkit.getWorld("minigames2"), 9947.0, -6.5, 9981.0)}, {new Location(Bukkit.getWorld("minigames2"), 9987.0, 5.0, 9839.0), new Location(Bukkit.getWorld("minigames2"), 9978.0, 3.5, 9836.0)}};

                FLAMETHROWER_BOXES = new Location[][]{{new Location(Bukkit.getWorld("minigames2"), 10029.0, 8.0, 9851.0), new Location(Bukkit.getWorld("minigames2"), 10032.0, 9.0, 9852.0)},
                        {new Location(Bukkit.getWorld("minigames2"), 9971.0, -4.0, 9984.0), new Location(Bukkit.getWorld("minigames2"), 9968.0, -5.0, 9981.0)}, {new Location(Bukkit.getWorld("minigames2"), 9963.0, -4.0, 9984.0), new Location(Bukkit.getWorld("minigames2"), 9960.0, -5.0, 9981.0)}, {new Location(Bukkit.getWorld("minigames2"), 9955.0, -5.0, 9984.0), new Location(Bukkit.getWorld("minigames2"), 9952.0, -4.0, 9981.0)}, {new Location(Bukkit.getWorld("minigames2"), 10035.0, -6.0, 10022.0), new Location(Bukkit.getWorld("minigames2"), 10030.0, -5.0, 10019.0)}, {new Location(Bukkit.getWorld("minigames2"), 10031.0, -4.0, 10020.0), new Location(Bukkit.getWorld("minigames2"), 10034.0, -5.0, 10023.0)}, {new Location(Bukkit.getWorld("minigames2"), 10034.0, -4.0, 10024.0), new Location(Bukkit.getWorld("minigames2"), 10031.0, -3.0, 10021.0)}, {new Location(Bukkit.getWorld("minigames2"), 10040.0, -6.0, 10024.0), new Location(Bukkit.getWorld("minigames2"), 10037.0, -5.0, 10029.0)}, {new Location(Bukkit.getWorld("minigames2"), 10039.0, -4.0, 10028.0), new Location(Bukkit.getWorld("minigames2"), 10036.0, -5.0, 10025.0)}, {new Location(Bukkit.getWorld("minigames2"), 10035.0, -4.0, 10025.0), new Location(Bukkit.getWorld("minigames2"), 10038.0, -3.0, 10028.0)}, {new Location(Bukkit.getWorld("minigames2"), 10030.0, -6.0, 10031.0), new Location(Bukkit.getWorld("minigames2"), 10035.0, -5.0, 10034.0)}, {new Location(Bukkit.getWorld("minigames2"), 10034.0, -4.0, 10033.0), new Location(Bukkit.getWorld("minigames2"), 10031.0, -5.0, 10030.0)}, {new Location(Bukkit.getWorld("minigames2"), 10031.0, -4.0, 10029.0), new Location(Bukkit.getWorld("minigames2"), 10034.0, -3.0, 10032.0)}, {new Location(Bukkit.getWorld("minigames2"), 10028.0, -6.0, 10029.0), new Location(Bukkit.getWorld("minigames2"), 10025.0, -5.0, 10024.0)}, {new Location(Bukkit.getWorld("minigames2"), 10026.0, -4.0, 10025.0), new Location(Bukkit.getWorld("minigames2"), 10029.0, -5.0, 10028.0)}, {new Location(Bukkit.getWorld("minigames2"), 10030.0, -4.0, 10028.0), new Location(Bukkit.getWorld("minigames2"), 10027.0, -3.0, 10025.0)}, {new Location(Bukkit.getWorld("minigames2"), 9976.0, 5.0, 9938.0), new Location(Bukkit.getWorld("minigames2"), 9973.0, 6.0, 9935.0)}, {new Location(Bukkit.getWorld("minigames2"), 9977.0, 6.0, 9936.0), new Location(Bukkit.getWorld("minigames2"), 9980.0, 5.0, 9938.0)}, {new Location(Bukkit.getWorld("minigames2"), 10062.0, -9.0, 9855.0), new Location(Bukkit.getWorld("minigames2"), 10063.0, -8.0, 9852.0)}, {new Location(Bukkit.getWorld("minigames2"), 10069.0, -8.0, 9854.0), new Location(Bukkit.getWorld("minigames2"), 10066.0, -7.0, 9853.0)}, {new Location(Bukkit.getWorld("minigames2"), 10064.0, -7.0, 9856.0), new Location(Bukkit.getWorld("minigames2"), 10067.0, -6.0, 9857.0)}, {new Location(Bukkit.getWorld("minigames2"), 10066.0, -6.0, 9857.0), new Location(Bukkit.getWorld("minigames2"), 10067.0, -5.0, 9858.0)}, {new Location(Bukkit.getWorld("minigames2"), 10062.0, -4.0, 9858.0), new Location(Bukkit.getWorld("minigames2"), 10063.0, -3.0, 9860.0)}, {new Location(Bukkit.getWorld("minigames2"), 9982.0, -5.0, 9918.0), new Location(Bukkit.getWorld("minigames2"), 9979.0, -4.0, 9915.0)}, {new Location(Bukkit.getWorld("minigames2"), 9965.0, -5.0, 9908.0), new Location(Bukkit.getWorld("minigames2"), 9968.0, -4.0, 9905.0)}, {new Location(Bukkit.getWorld("minigames2"), 10020.0, -8.0, 9844.0), new Location(Bukkit.getWorld("minigames2"), 10021.0, -7.0, 9847.0)}, {new Location(Bukkit.getWorld("minigames2"), 10024.0, -8.0, 9844.0), new Location(Bukkit.getWorld("minigames2"), 10023.0, -7.0, 9847.0)}};

                DART_TRAPS = new DartTrapData[]{new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 9962.5, 7.5, 10010.5, 1172, 45), new Vector(-2,0,0)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 9938.5, 7.5, 10003.5, 1351, 80), new Vector(2,0,0)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 9990.5, 10.0, 10045.5, 87, 90), new Vector(-2,0,0)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 9986.5, 11.0, 10036.5, 373, 90), new Vector(0,0,2)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10004.5, 5.0, 9959.5, -4, 88), new Vector(0,0,-2)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10002.5, 5.0, 9961.5, 19, 89), new Vector(-2,0,0)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10004.5, 5.0, 9963.5, 13, 89), new Vector(0,0,2)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10006.5, 5.0, 9961.5, -89, 2), new Vector(2,0,0))};
                SHOCKWAVE_LOCATION = new Location(Bukkit.getWorld("minigames2"), 10047.5, -11.0, 9879.5, 4326, 90);
                DOOR_RED = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10047.5, -11.0, 9843.5, 562, 0), new Location(Bukkit.getWorld("minigames2"), 10047.5, -11.0, 9849.5, 0, 0)};
                DOOR_BLUE = new Location[]{new Location(Bukkit.getWorld("minigames2"), 9990.5, -11.0, 9893.5, 809, 0), new Location(Bukkit.getWorld("minigames2"), 9996.5, -11.0, 9893.5, -90, 0)};
                DOOR_GREEN = new Location[]{new Location(Bukkit.getWorld("minigames2"), 9965.5, 4.0, 10023.5, 813, 0), new Location(Bukkit.getWorld("minigames2"), 9971.5, 4.0, 10023.5, -90, 0)};
                DOOR_YELLOW = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10004.5, 4.0, 10093.5, 1080, 0), new Location(Bukkit.getWorld("minigames2"), 10004.5, 4.0, 10087.5, 180, 0)};
                END_LOCATION = new Location(Bukkit.getWorld("minigames2"), 10068.5, 4.0, 9895.5, 1396, -1);
                GLITTER_LOCATIONS = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10016.5, 4.5, 10029.5, -249, 51), new Location(Bukkit.getWorld("minigames2"), 10032.5, -1.95, 10059.5, -363, -4), new Location(Bukkit.getWorld("minigames2"), 9938.5, -5.0, 9982.5, -271, 89), new Location(Bukkit.getWorld("minigames2"), 10020.5, 5.0, 10001.5, 265, 89), new Location(Bukkit.getWorld("minigames2"), 10004.5, 5.0, 10120.5, 437, 90), new Location(Bukkit.getWorld("minigames2"), 9936.5, 5.0, 10023.5, 457, 90), new Location(Bukkit.getWorld("minigames2"), 9967.5, 4.75, 9872.5, 547, 21), new Location(Bukkit.getWorld("minigames2"), 9955.5, 6.0, 9837.5, 463, 89), new Location(Bukkit.getWorld("minigames2"), 10079.5, -8.0, 9879.5, 628, 2), new Location(Bukkit.getWorld("minigames2"), 9970.5, -10.0, 9893.5, 811, 90), new Location(Bukkit.getWorld("minigames2"), 9966.5, -5.0, 9940.5, 745, 90)};
                KEY_RED_UUID = UUID.fromString("f5defac0-2606-4a21-b4fb-0ca7d1d7865b");
                KEY_BLUE_UUID = UUID.fromString("7ea79b75-59c4-4c0b-9811-d52df51b958c");
                KEY_GREEN_UUID = UUID.fromString("1ac623e6-a273-435b-bf48-832f6984eaac");
                KEY_YELLOW_UUID = UUID.fromString("fcf75b67-e80e-4113-9cb1-58836f3cba34");
                HERB_UUID = UUID.fromString("9fc35431-f7b8-4986-b66c-391ea26cd558");
                BETTER_SWORD_NAME = "m_exca_better_sword";
                FALLING_CEILING_BOX = null;
            }
            case DESERT -> {
                START_LOCATION = new Location(Bukkit.getWorld("minigames2"), 10225.5, 8.0, 9054.5, 90, 0);
                TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), 10207.5, 16.0, 9085.5, 314, 35);
                ENTRANCE_WALL = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10216.5, 8.0, 9056.5, 454, 88), new Location(Bukkit.getWorld("minigames2"), 10216.5, 12.0, 9052.5, 683, 89)};
                SPIKE_BOXES = new Location[][]{{new Location(Bukkit.getWorld("minigames2"), 10236.0, 12.0, 9080, 811, 90), new Location(Bukkit.getWorld("minigames2"), 10230.0, 10.5, 9091.0, 742, 90)}, {new Location(Bukkit.getWorld("minigames2"), 10240.0, 10.5, 9071.0, 1384, 89), new Location(Bukkit.getWorld("minigames2"), 10256.0, 12.0, 9092.0, 1588, 90)}, {new Location(Bukkit.getWorld("minigames2"), 10142.0, 9.0, 8997.0, 1917, 88), new Location(Bukkit.getWorld("minigames2"), 10145.0, 7.5, 9008.0, 2129, 90)}, {new Location(Bukkit.getWorld("minigames2"), 10236.0, 9.0, 9020.0, 4116, 90), new Location(Bukkit.getWorld("minigames2"), 10229.0, 7.5, 9021.0, 4037, 89)}};
                FLAMETHROWER_BOXES = new Location[][]{{new Location(Bukkit.getWorld("minigames2"), 10235.0, 8.0, 9021.0), new Location(Bukkit.getWorld("minigames2"), 10236.0, 10.0, 9022.0)}, {new Location(Bukkit.getWorld("minigames2"), 10235.0, 8.0, 9019.0), new Location(Bukkit.getWorld("minigames2"), 10236.0, 10.0, 9020.0)}, {new Location(Bukkit.getWorld("minigames2"), 10232.0, 8.0, 9019.0), new Location(Bukkit.getWorld("minigames2"), 10233.0, 10.0, 9020.0)}, {new Location(Bukkit.getWorld("minigames2"), 10229.0, 8.0, 9019.0), new Location(Bukkit.getWorld("minigames2"), 10230.0, 10.0, 9020.0)}, {new Location(Bukkit.getWorld("minigames2"), 10229.0, 8.0, 9021.0), new Location(Bukkit.getWorld("minigames2"), 10230.0, 10.0, 9022.0)}, {new Location(Bukkit.getWorld("minigames2"), 10232.0, 8.0, 9021.0), new Location(Bukkit.getWorld("minigames2"), 10233.0, 10.0, 9022.0)}, {new Location(Bukkit.getWorld("minigames2"), 10178.0, 13.5, 8925.0), new Location(Bukkit.getWorld("minigames2"), 10179.0, 15.5, 8926.0)}, {new Location(Bukkit.getWorld("minigames2"), 10180.0, 13.5, 8918.0), new Location(Bukkit.getWorld("minigames2"), 10181.0, 15.5, 8919.0)}, {new Location(Bukkit.getWorld("minigames2"), 10178.0, 13.5, 8914.0), new Location(Bukkit.getWorld("minigames2"), 10179.0, 15.5, 8915.0)}, {new Location(Bukkit.getWorld("minigames2"), 10152.0, 21.0, 9054.0), new Location(Bukkit.getWorld("minigames2"), 10154.0, 22.0, 9055.0)}, {new Location(Bukkit.getWorld("minigames2"), 10229.0, 14.8, 9133.0), new Location(Bukkit.getWorld("minigames2"), 10228.0, 15.8, 9139.0)}, {new Location(Bukkit.getWorld("minigames2"), 10225.0, 14.8, 9133.0), new Location(Bukkit.getWorld("minigames2"), 10224.0, 15.8, 9139.0)}, {new Location(Bukkit.getWorld("minigames2"), 10221.0, 14.8, 9133.0), new Location(Bukkit.getWorld("minigames2"), 10220.0, 15.8, 9139.0)}, {new Location(Bukkit.getWorld("minigames2"), 10217.0, 14.8, 9133.0), new Location(Bukkit.getWorld("minigames2"), 10216.0, 15.8, 9139.0)}, {new Location(Bukkit.getWorld("minigames2"), 10183.0, 16.0, 9140.0), new Location(Bukkit.getWorld("minigames2"), 10184.0, 17.0, 9136.0)}};
                DART_TRAPS = new DartTrapData[]{new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10237.5, 9.0, 8960.5, 6566, 9), new Vector(-2,0,0)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10237.5, 9.0, 8962.5, 6559, 23), new Vector(-2,0,0)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10237.5, 9.0, 8964.5, 6569, 19), new Vector(-2,0,0)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10237.5, 9.0, 8966.5, 6566, 32), new Vector(-2,0,0)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10237.5, 9.0, 8968.5, 6575, 10), new Vector(-2,0,0)), new DartTrapData(new Location(Bukkit.getWorld("minigames2"), 10237.5, 9.0, 8970.5, 6572, 5), new Vector(-2,0,0))};
                SHOCKWAVE_LOCATION = null;
                DOOR_RED = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10166.5, 8.0, 9092.5, 6929, 0), new Location(Bukkit.getWorld("minigames2"), 10171.5, 8.0, 9092.5, 7110, -1)};
                DOOR_BLUE = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10199.5, 14.0, 9147.5, 7200, 1), new Location(Bukkit.getWorld("minigames2"), 10199.5, 14.0, 9142.5, 7380, 0)};
                DOOR_GREEN = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10151.5, 8.0, 8937.5, 7289, -1), new Location(Bukkit.getWorld("minigames2"), 10156.5, 8.0, 8937.5, 7470, 0)};
                DOOR_YELLOW = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10179.5, 13.0, 8904.5, 7379, 0), new Location(Bukkit.getWorld("minigames2"), 10179.5, 13.0, 8909.5, 7560, 0)};
                END_LOCATION = new Location(Bukkit.getWorld("minigames2"), 10239.5, 24.0, 9020.5, 8622, 2);
                GLITTER_LOCATIONS = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10250.5, 13.0, 9101.5, 8526, 90), new Location(Bukkit.getWorld("minigames2"), 10147.5, 27.6, 9078.5, 9091, -3), new Location(Bukkit.getWorld("minigames2"), 10128.5, -4.0, 8953.5, 9177, 85), new Location(Bukkit.getWorld("minigames2"), 10135.5, 11.0, 8966.5, 9255, 90), new Location(Bukkit.getWorld("minigames2"), 10189.5, 16.0, 8918.5, 9459, 90), new Location(Bukkit.getWorld("minigames2"), 10275.5, 16.0, 9005.5, 10064, 90)};
                KEY_RED_UUID = UUID.fromString("9dfd8b1a-2834-4359-ac36-7f3e40c6b13d");
                KEY_BLUE_UUID = UUID.fromString("bb081d3c-410e-4731-a25a-d1f282b81e24");
                KEY_YELLOW_UUID = UUID.fromString("c9a07027-20df-4c76-82c9-2481ae37f3ff");
                KEY_GREEN_UUID = UUID.fromString("50e34b40-62f5-4013-9a41-69b42f50bbc8");
                HERB_UUID = null;
                BETTER_SWORD_NAME = "m_exca_better_sword_desert";
                FALLING_CEILING_BOX = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10223.0, 12.0, 9079.0, 9954, 90), new Location(Bukkit.getWorld("minigames2"), 10228.0, 15.2, 9067.0, 10084, 80)};

            }
            default -> throw new IllegalArgumentException("The selected map was null or doesn't exist!");
        }
    }
}
