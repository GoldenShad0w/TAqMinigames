package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.minigames.ProffersPit;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.concurrent.ThreadLocalRandom;

public abstract class ProfEvent {

    protected int tick = 0;
    protected final EventLocation location;
    protected boolean isDone = false;

    public ProfEvent(String formattedString, EventLocation location) {
        this.location = location;
        if (location != null) {
            ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString(formattedString.formatted(location.area().getName()), 50), ChatColor.YELLOW).toArray(String[]::new));
        }

    }

    public void tick() {
        tick++;
    }



    public boolean isDone() {
        return isDone;
    }
}
