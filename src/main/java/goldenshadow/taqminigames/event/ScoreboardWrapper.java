package goldenshadow.taqminigames.event;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardWrapper {

    private static final Map<UUID, FastBoard> boards = new HashMap<>();
    private static final Map<UUID, String[]> queuedData = new HashMap<>();

    /**
     * Internal method used to create a scoreboard for a player who does not have one yet
     * @param player The player
     */
    private static void createScoreboard(Player player) {
        if (player != null) {
            FastBoard board = new FastBoard(player);
            board.updateTitle(ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + "TAQ MINIGAMES");
            boards.put(player.getUniqueId(), board);
        }
    }

    /**
     * Used to delete a scoreboard from a player
     * @param player The player whose scoreboard should be removed
     */
    public static void removeScoreboard(Player player) {
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    /**
     * Used to queue data that should be displayed on a players scoreboard next update tick
     * @param player The player whose scoreboard the data should be displayed to
     * @param entries The data that should be displayed. Each string represents a line
     */
    public static void queueData(Player player, String... entries) {
        queuedData.put(player.getUniqueId(), entries);
    }

    /**
     * Used to update all scoreboards
     */
    public static void updateBoards() {
        for (UUID uuid : queuedData.keySet()) {
            if (!boards.containsKey(uuid)) {
                createScoreboard(Bukkit.getPlayer(uuid));
            } else {
                boards.get(uuid).updateLines(queuedData.get(uuid));
            }
        }
    }


}
