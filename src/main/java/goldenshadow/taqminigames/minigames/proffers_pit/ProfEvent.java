package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.ChatColor;


public abstract class ProfEvent {

    protected int tick = 0;
    protected final EventLocation location;
    protected boolean isDone = false;

    /**
     * Used to create a new prof event
     * @param formattedString The string end to all players to announce the event
     * @param location The location of where it occurs
     * @param formatted Whether the string is using formatting
     */
    public ProfEvent(String formattedString, EventLocation location, boolean formatted) {
        this.location = location;

        if (formatted) {
            if (location != null) {
                ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString(formattedString.formatted(location.area().getName()), 50), ChatColor.YELLOW).toArray(String[]::new));
            }
        } else {
            ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString(formattedString, 50), ChatColor.YELLOW).toArray(String[]::new));
        }
    }


    public void tick() {
        tick++;
    }



    public boolean isDone() {
        return isDone;
    }
}
