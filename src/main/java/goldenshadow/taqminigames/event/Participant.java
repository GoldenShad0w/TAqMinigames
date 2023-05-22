package goldenshadow.taqminigames;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * A class used to represent a participating or spectating player
 */
public class Participant {

    private final UUID uuid;
    private final boolean isPlaying;
    private int globalScore = 0;


    /**
     * Creates a new Participant object
     * @param player The player who is participating
     * @param isPlaying Whether the player is playing or spectating
     */
    public Participant(Player player, boolean isPlaying) {
        uuid = player.getUniqueId();
        this.isPlaying = isPlaying;
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

    /**
     * A getter for the players global score
     * @return The players global score
     */
    public int getGlobalScore() {
        return globalScore;
    }

    /**
     * Used to add or remove points from the players score. The score can never drop below zero
     * @param amount The amount that should be added. Negative amounts will result in the score being lowered
     */
    public void addToScore(int amount) {
        globalScore = Math.max(0, globalScore + amount);
    }


}
