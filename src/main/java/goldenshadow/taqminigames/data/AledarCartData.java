package goldenshadow.taqminigames.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class AledarCartData {

    public enum Map {
        CLASSIC;
    }

    public final Map MAP;
    public final Location TUTORIAL_LOCATION;
    public final Location START_LOCATION;
    public final Location[] START_BARRIERS;
    public final Location[] POWERUP_LOCATIONS;
    public final Location[] ANTI_CHEAT_LINE_1;
    public final Location[] ANTI_CHEAT_LINE_2;
    public final Location[] LAP_LINE;
    public final int CRATE_POINTS = 30;
    public final int LAP_POINTS = 100;
    public final int FINISH_POINTS = 1500;

    public AledarCartData(Map map) {
        this.MAP = map;
        switch (map) {
            case CLASSIC -> {
                TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), 10147.5, -35.0, -9875.5, 3354, 6);
                START_LOCATION = new Location(Bukkit.getWorld("minigames2"), 10288.5, -60.0, -9766.5, 3329, 0);
                START_BARRIERS = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10280.5, -60.0, -9742.5, 2341, 4), new Location(Bukkit.getWorld("minigames2"), 10280.5, -50.38186452388239, -9786.5, 2518, 38)};
                POWERUP_LOCATIONS = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10159.5, -60.0, -9814.5, 3708, -1), new Location(Bukkit.getWorld("minigames2"), 10162.5, -60.0, -9820.5, 3706, 0), new Location(Bukkit.getWorld("minigames2"), 10165.5, -60.0, -9840.5, 3696, -4), new Location(Bukkit.getWorld("minigames2"), 10167.5, -60.0, -9845.5, 3694, 0), new Location(Bukkit.getWorld("minigames2"), 10170.5, -60.0, -9849.5, 3692, 6), new Location(Bukkit.getWorld("minigames2"), 10079.5, -60.0, -9857.5, 3731, -4), new Location(Bukkit.getWorld("minigames2"), 10073.5, -60.0, -9854.5, 3665, -3), new Location(Bukkit.getWorld("minigames2"), 10067.5, -60.0, -9851.5, 3665, 82), new Location(Bukkit.getWorld("minigames2"), 10060.5, -60.0, -9846.5, 3664, 90), new Location(Bukkit.getWorld("minigames2"), 10054.5, -60.0, -9840.5, 3656, 89), new Location(Bukkit.getWorld("minigames2"), 10053.5, -60.0, -9932.5, 3782, 90), new Location(Bukkit.getWorld("minigames2"), 10047.5, -60.0, -9932.5, 3710, 90), new Location(Bukkit.getWorld("minigames2"), 10041.5, -60.0, -9932.5, 3690, 90), new Location(Bukkit.getWorld("minigames2"), 10035.5, -60.0, -9932.5, 3692, 90), new Location(Bukkit.getWorld("minigames2"), 10029.5, -60.0, -9932.5, 3697, 90), new Location(Bukkit.getWorld("minigames2"), 10023.5, -60.0, -9932.5, 3649, 89), new Location(Bukkit.getWorld("minigames2"), 10104.5, -60.0, -10023.5, 3880, 88), new Location(Bukkit.getWorld("minigames2"), 10108.5, -60.0, -10027.5, 3844, 90), new Location(Bukkit.getWorld("minigames2"), 10113.5, -60.0, -10032.5, 3825, 90), new Location(Bukkit.getWorld("minigames2"), 10117.5, -60.0, -10037.5, 3825, 90), new Location(Bukkit.getWorld("minigames2"), 10122.5, -60.0, -10041.5, 3827, 89), new Location(Bukkit.getWorld("minigames2"), 10184.5, -60.0, -9930.5, 3955, 90), new Location(Bukkit.getWorld("minigames2"), 10180.5, -60.0, -9925.5, 4006, 90), new Location(Bukkit.getWorld("minigames2"), 10177.5, -60.0, -9920.5, 3998, 89), new Location(Bukkit.getWorld("minigames2"), 10173.5, -60.0, -9914.5, 4137, 88), new Location(Bukkit.getWorld("minigames2"), 10260.5, -60.0, -9872.5, 4347, 89), new Location(Bukkit.getWorld("minigames2"), 10258.5, -60.0, -9866.5, 4337, 90), new Location(Bukkit.getWorld("minigames2"), 10256.5, -60.0, -9860.5, 4340, 90), new Location(Bukkit.getWorld("minigames2"), 10254.5, -60.0, -9854.5, 4334, 89), new Location(Bukkit.getWorld("minigames2"), 10252.5, -60.0, -9848.5, 4333, 90), new Location(Bukkit.getWorld("minigames2"), 10250.5, -60.0, -9843.5, 4343, 87), new Location(Bukkit.getWorld("minigames2"), 10374.5, -60.0, -9872.5, 4246, 89), new Location(Bukkit.getWorld("minigames2"), 10377.5, -60.0, -9876.5, 4184, 90), new Location(Bukkit.getWorld("minigames2"), 10380.5, -60.0, -9879.5, 4207, 90), new Location(Bukkit.getWorld("minigames2"), 10388.5, -60.0, -9888.5, 4116, 88), new Location(Bukkit.getWorld("minigames2"), 10392.5, -60.0, -9893.5, 4172, 90), new Location(Bukkit.getWorld("minigames2"), 10351.5, -60.0, -9808.5, 4360, 90), new Location(Bukkit.getWorld("minigames2"), 10354.5, -60.0, -9803.5, 4315, 90), new Location(Bukkit.getWorld("minigames2"), 10358.5, -60.0, -9797.5, 4290, 90), new Location(Bukkit.getWorld("minigames2"), 10360.5, -60.0, -9791.5, 4302, 90), new Location(Bukkit.getWorld("minigames2"), 10362.5, -60.0, -9785.5, 4299, 90)};
                ANTI_CHEAT_LINE_1 = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10056.5, -61.0, -9912.5, 2610, 9), new Location(Bukkit.getWorld("minigames2"), 10016.5, -40.0, -9915.5, 2705, 89)};
                ANTI_CHEAT_LINE_2 = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10191.5, -61.0, -9956.5, 2969, 6), new Location(Bukkit.getWorld("minigames2"), 10154.5, -40, -9960.5, 2975, 87)};
                LAP_LINE = new Location[]{new Location(Bukkit.getWorld("minigames2"), 10279.5, -61.0, -9742.5, 182, 90), new Location(Bukkit.getWorld("minigames2"), 10273.5, -53.006862994049804, -9785.5, 352, 25)};
            }
            default -> throw new IllegalArgumentException("The selected map was null or doesn't exist!");
        }
    }
}
