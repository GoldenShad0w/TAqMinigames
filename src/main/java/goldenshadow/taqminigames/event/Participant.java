package goldenshadow.taqminigames.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * A class used to represent a participating or spectating player
 */
public class Participant {

    private final UUID uuid;
    private final boolean isPlaying;


    /**
     * Creates a new Participant object
     * @param player The player who is participating
     * @param isPlaying Whether the player is playing or spectating
     */
    public Participant(Player player, boolean isPlaying) {
        uuid = player.getUniqueId();
        this.isPlaying = isPlaying;
        ScoreboardWrapper.queueData(player,
                " ",
                ChatColor.AQUA + "Starting soon!",
                ChatColor.DARK_AQUA + "Role: " + ChatColor.AQUA + (isPlaying ? "Participant" : "Spectator"),
                " ");
    }

    /**
     * A getter for the player
     * @return The player
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * A getter for whether the player is playing or spectating
     * @return True if they are playing, false otherwise
     */
    public boolean isPlaying() {
        return isPlaying;
    }



}
