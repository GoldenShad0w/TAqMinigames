package goldenshadow.taqminigames.data;

import goldenshadow.taqminigames.minigames.proffers_pit.EventLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ProffersPitData {

    public enum Map {
        FLATLAND;
    }

    public final Map MAP;
    public final Location TUTORIAL_LOCATION;
    public final Location START_LOCATION;
    public final EventLocation[] EVENT_LOCATIONS;
    public final int PROF_T2_PRICE = 30;
    public final int PROF_T3_PRICE = 60;
    public final int PROF_T4_PRICE = 150;
    public final int PROF_T5_PRICE = 320;
    public final int PROF_T6_PRICE = 600;
    public final int PROF_BOOTS_PRICE = 200;
    public final int PROF_SWORD_PRICE = 300;
    public final int PROF_SPEED_PRICE = 80;
    public final int PROF_HASTE_PRICE = 80;
    public final int PROF_HEALTH_PRICE = 80;
    public final int PROF_HUNTED_PRICE = 400;
    public final String PROF_MOB_NAME = "m_prof_mob";


    public ProffersPitData(Map map) {
        MAP = map;
        switch (map) {
            case FLATLAND -> {
                TUTORIAL_LOCATION = new Location(Bukkit.getWorld("minigames2"), 1982.5, 103.0, 2135.5, 888, 16);
                START_LOCATION = new Location(Bukkit.getWorld("minigames2"), 2007.5, 101.0, 1989.5, 362, 0);
                EVENT_LOCATIONS = new EventLocation[]{new EventLocation(new Location(Bukkit.getWorld("minigames2"), 1993.5, 109.0, 2013.5, 940, -1), EventLocation.EventArea.COPPER, EventLocation.EventType.TOTEM), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2006.5, 101.0, 1985.5, 1051, -2), EventLocation.EventArea.COPPER, EventLocation.EventType.TOTEM), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2035.5, 115.0, 1995.5, 1529, -5), EventLocation.EventArea.COPPER, EventLocation.EventType.EARTHQUAKE), new EventLocation(null, EventLocation.EventArea.COPPER, EventLocation.EventType.XP), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 1999.5, 88.0, 2065.5, 1052, 1), EventLocation.EventArea.IRON, EventLocation.EventType.GAS), new EventLocation(null, EventLocation.EventArea.IRON, EventLocation.EventType.XP), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2021.5, 90.0, 2061.5, 178, 1), EventLocation.EventArea.IRON, EventLocation.EventType.TOTEM), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 1988.5, 95.0, 2132.5, 126, -3), EventLocation.EventArea.GOLD, EventLocation.EventType.EARTHQUAKE), new EventLocation(null, EventLocation.EventArea.GOLD, EventLocation.EventType.XP), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2022.5, 98.0, 2153.5, 154, -5), EventLocation.EventArea.GOLD, EventLocation.EventType.EARTHQUAKE), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2019.5, 100.0, 2109.5, 75, 0), EventLocation.EventArea.GOLD, EventLocation.EventType.TOTEM), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2083.5, 115.0, 1993.5, 138, -3), EventLocation.EventArea.COBALT, EventLocation.EventType.TOTEM), new EventLocation(null, EventLocation.EventArea.COBALT, EventLocation.EventType.XP), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2061.5, 89.0, 1939.5, 163, -4), EventLocation.EventArea.DIAMOND, EventLocation.EventType.GAS), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2090.5, 77.0, 1958.5, 195, -2), EventLocation.EventArea.DIAMOND, EventLocation.EventType.TOTEM), new EventLocation(null, EventLocation.EventArea.DIAMOND, EventLocation.EventType.XP), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2040.5, 84.0, 1950.5, 63, 6), EventLocation.EventArea.DIAMOND, EventLocation.EventType.GAS), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2016.5, 67.0, 1977.5, 192, 0), EventLocation.EventArea.MOLTEN, EventLocation.EventType.GAS), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2061.5, 59.0, 2002.5, 15, 1), EventLocation.EventArea.MOLTEN, EventLocation.EventType.TOTEM), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2027.5, 75.0, 1956.5, -370, -1), EventLocation.EventArea.MOLTEN, EventLocation.EventType.GAS), new EventLocation(new Location(Bukkit.getWorld("minigames2"), 2060.5, 59.0, 2002.5, 2168, 1), EventLocation.EventArea.MOLTEN, EventLocation.EventType.TOTEM)};
            }
            default -> throw new IllegalArgumentException("The selected map was null or doesn't exist!");
        }
    }
}
