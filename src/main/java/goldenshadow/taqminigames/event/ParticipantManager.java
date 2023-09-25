package goldenshadow.taqminigames.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParticipantManager {

    private static final List<Participant> participantList = new ArrayList<>();

    /**
     * Used to teleport all register players to a given location
     * @param location The location that should be teleported to
     */
    public static void teleportAllPlayers(Location location) {
        getAll().forEach(x -> x.teleport(location));
    }

    /**
     * Used to register a new player
     * @param player The player who should be registered
     * @param isPlaying Whether they are playing or spectating
     */
    public static void addParticipant(Player player, boolean isPlaying) {
        participantList.add(new Participant(player, isPlaying));
    }

    public static boolean isRegistered(Player player) {
        for (Participant p : participantList) {
            if (p.getPlayerUUID().equals(player.getUniqueId())) return true;
        }
        return false;
    }

    /**
     * A getter for a list of participants
     * @return A list containing all the registered players who are playing
     */
    public static List<Player> getParticipants() {
        List<Player> players = new ArrayList<>();
        for (Participant p : participantList) {
            if (isOnline(p)) {
                if (p.isPlaying()) players.add(p.getPlayer());
            }
        }
        return players;
    }

    /**
     * Getter for a list of all registered players
     * @return A list containing all the registered players
     */
    public static List<Player> getAll() {
        List<Player> players = new ArrayList<>();
        for (Participant p : participantList) {
            if (isOnline(p)) {
                players.add(p.getPlayer());
            }
        }
        return players;
    }

    /**
     * Used to get a players participant object.
     * @param player The player
     * @return The participant or null if none exists
     */
    public static Participant getParticipant(Player player) {
        if (isOnline(player.getUniqueId())) {
            for (Participant p : participantList) {
                if (p.getPlayerUUID().equals(player.getUniqueId())) {
                    return p;
                }
            }
        }
        return null;
    }



    /**
     * Used to unregister all players
     */
    public static void resetAll() {
        participantList.clear();
    }

    /**
     * Used to check if a player is online
     * @param uuid The uuid of the player to be checked
     * @return True if they are, otherwise false
     */
    public static boolean isOnline(UUID uuid) {
        OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
        return p.isOnline();
    }

    /**
     * Used to check if a participant is online
     * @param p The participant
     * @return Tue if they are, otherwise false
     */
    public static boolean isOnline(Participant p) {
        return isOnline(p.getPlayerUUID());
    }

}
