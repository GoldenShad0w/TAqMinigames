package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.concurrent.ThreadLocalRandom;

public abstract class ProfEvent {

    protected int tick = 0;
    protected final Location location;
    protected final String area;
    protected boolean isDone = false;

    public ProfEvent(String formattedString) {
        int i = ThreadLocalRandom.current().nextInt(0, Constants.PROF_EVENT_LOCATIONS.length);
        location = Constants.PROF_EVENT_LOCATIONS[i][ThreadLocalRandom.current().nextInt(0, Constants.PROF_EVENT_LOCATIONS[i].length)];
        area = getArea(i);
        ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString(formattedString.formatted(area),50), ChatColor.YELLOW).toArray(String[]::new));

    }

    public void tick() {
        tick++;
    }

    protected String getArea(int i) {
        switch (i) {
            case 0 -> {
                return "Copper Quarry";
            }
            case 1 -> {
                return "Iron Mines";
            }
            case 2 -> {
                return "Gold Canyon";
            }
            case 3 -> {
                return "Cobalt Hills";
            }
            case 4 -> {
                return "Diamond Caves";
            }
            case 5 -> {
                return "Molten Caves";
            }
            default -> {
                return "ERROR";
            }
        }
    }

    public boolean isDone() {
        return isDone;
    }
}
