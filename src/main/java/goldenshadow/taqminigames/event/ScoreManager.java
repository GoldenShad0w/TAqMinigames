package goldenshadow.taqminigames.event;

import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * A class used to keep track of earned scores
 */
public class ScoreManager {

    private static double scoreMultiplier = 1;
    private final HashMap<UUID, Integer> scores = new HashMap<>();
    private boolean hasChangeOccurred = false;
    private List<String> sortedDisplayList = new ArrayList<>();
    private final String descriptor;


    /**
     * Used to create a new scoreboard manager
     * @param descriptor How the score should be called (e.g. "Emeralds")
     */
    public ScoreManager(String descriptor) {
        for (Player p : ParticipantManager.getParticipants()) {
            scores.put(p.getUniqueId(), 0);
        }
        this.descriptor = descriptor;
    }

    /**
     * A class method that is used to calculate how many points certain things should give depending on the amount of players
     * @param players How many player are competing
     */
    public static void calculateScores(int players) {
        if (players < 1) players = 1;
        Constants.AURA_SURVIVE /= players;
    }

    /**
     * A class method used to get the current score multiplier value
     * @return The current score multiplier
     */
    public static double getScoreMultiplier() {
        return scoreMultiplier;
    }

    /**
     * A class method used to increase the score multiplier to its next increment
     */
    public static void increaseMultiplier() {
        scoreMultiplier += 0.5;
    }

    /**
     * Used to increase the score of a player and send them a feedback message
     * @param player The player whose score should be increased
     * @param amount The amount by which it should be increased. Should be above 0
     * @param message The reason why the player earned the points. This will be displayed to the player
     * @param withMultiplier Whether the score multiplier should be applied
     */
    public void increaseScore(Player player, int amount, String message, boolean withMultiplier) {
        hasChangeOccurred = true;
        if (withMultiplier) {
            amount *= scoreMultiplier;
        }
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
        UUID uuid = player.getUniqueId();
        if (scores.containsKey(uuid)) {
            scores.put(uuid, scores.get(uuid) + amount);
        } else scores.put(uuid, amount);
        player.sendMessage(ChatMessageFactory.pointsGainedInfo(message, amount));
    }

    /**
     * Used to increase the score of a player
     * @param player The player whose score should be increased
     * @param amount The amount by which it should be increased
     * @param withMultiplier Whether the score multiplier should be applied
     */
    public void increaseScore(Player player, int amount, boolean withMultiplier) {
        hasChangeOccurred = true;
        if (withMultiplier) {
            amount *= scoreMultiplier;
        }
        UUID uuid = player.getUniqueId();
        if (scores.containsKey(uuid)) {
            scores.put(uuid, scores.get(uuid) + amount);
        } else scores.put(uuid, amount);
    }

    /**
     * Used to increase the score of a player
     * @param uuid The uuid of the player whose score should be increased
     * @param amount The amount by which it should be increased
     * @param withMultiplier Whether the score multiplier should be applied
     */
    public void increaseScore(UUID uuid, int amount, boolean withMultiplier) {
        hasChangeOccurred = true;
        if (withMultiplier) {
            amount *= scoreMultiplier;
        }
        if (scores.containsKey(uuid)) {
            scores.put(uuid, scores.get(uuid) + amount);
        } else scores.put(uuid, amount);
    }

    /**
     * Getter for the score of a certain player
     * @param uuid The players UUID
     * @return The score of that player
     */
    public int getScore(UUID uuid) {
        if (scores.containsKey(uuid)) return scores.get(uuid);
        return 0;
    }

    /**
     * Used to merge another score manager into this one
     * @param otherScoreManager The other score manager
     */
    public void merge(ScoreManager otherScoreManager) {
        hasChangeOccurred = true;
        HashMap<UUID, Integer> otherMap = otherScoreManager.getScores();
        for (UUID uuid : otherMap.keySet()) {
            increaseScore(uuid, otherMap.get(uuid), false);
        }
    }

    /**
     * Getter for the score map
     * @return The score map
     */
    public HashMap<UUID, Integer> getScores() {
        return scores;
    }

    /**
     * Used to get a sorted and formatted list that can be used to display the standings in chat or on a scoreboard
     * @param nameColor The color that the player names should have
     * @param scoreColor The color that the actual score should have
     * @return The formatted list
     */
    public List<String> getSortedDisplayList(ChatColor nameColor, ChatColor scoreColor) {
        List<UUID> sorted = new ArrayList<>(scores.keySet());
        sorted.sort((x,y) -> scores.get(y).compareTo(scores.get(x)));
        List<String> returnList = new ArrayList<>();
        for (UUID uuid : sorted) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                returnList.add((returnList.size() + 1) + ". " + nameColor + player.getName() + " " + scoreColor + scores.get(uuid) + " " + descriptor);
            }
        }
        return returnList;
    }

    /**
     * Used to get the placement of a certain player
     * @param player The player whose placement should be returned
     * @return The placement of the player
     */
    public int getPlacement(Player player) {
        List<UUID> sorted = new ArrayList<>(scores.keySet());
        sorted.sort((x,y) -> scores.get(y).compareTo(scores.get(x)));
        return sorted.indexOf(player.getUniqueId());
    }

    /**
     * Getter for the score descriptor
     * @return How the points are being called
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Used to get the specific four placement lines of the default scoreboard
     * @param player The players whose lines should be returned
     * @param nameColor The color that the player names should have
     * @param scoreColor The color that the scores should have
     * @return The four lines
     */
    public String[] getScoreboardLines(Player player, ChatColor nameColor, ChatColor scoreColor) {
        if (hasChangeOccurred || sortedDisplayList.isEmpty()) {
            sortedDisplayList = getSortedDisplayList(nameColor, scoreColor);
        }
        int placement = getPlacement(player);
        String[] array = new String[4];
        array[0] = sortedDisplayList.size() > 0 ? sortedDisplayList.get(0) : "1." + nameColor + " ---";

        if (placement == 0) {   //edge case: player is first place
            array[1] = sortedDisplayList.get(0);
            array[2] = sortedDisplayList.size() > 1 ? sortedDisplayList.get(1) : "2." + nameColor + " ---";
            array[3] = sortedDisplayList.size() > 2 ? sortedDisplayList.get(2) : "3." + nameColor + " ---";
        } else if (placement == -1) { //edge case: player is spectator
            array[1] = sortedDisplayList.size() > 0 ? sortedDisplayList.get(0) : "1." + nameColor + " ---";
            array[2] = sortedDisplayList.size() > 1 ? sortedDisplayList.get(1) : "2." + nameColor + " ---";
            array[3] = sortedDisplayList.size() > 2 ? sortedDisplayList.get(2) : "3." + nameColor + " ---";
        } else { //normal case: player is not first
            array[1] = sortedDisplayList.get(placement - 1);
            array[2] = sortedDisplayList.get(placement);
            array[3] = sortedDisplayList.size() > (placement + 1) ? sortedDisplayList.get(placement + 1) : (placement + 2) + "." + nameColor + " ---";
        }
        return array;
    }
}
