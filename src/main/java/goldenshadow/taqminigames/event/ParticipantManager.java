package goldenshadow.taqminigames.event;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ParticipantManager {

    private static final List<Participant> participantList = new ArrayList<>();

    /**
     * Used to teleport all register players to a given location
     * @param location The location that should be teleported to
     */
    public static void teleportAllPlayers(Location location) {
        participantList.forEach(x -> x.getPlayer().teleport(location));
    }

    /**
     * Used to register a new player
     * @param player The player who should be registered
     * @param isPlaying Whether they are playing or spectating
     */
    public static void addParticipant(Player player, boolean isPlaying) {
        participantList.add(new Participant(player, isPlaying));
    }

    /**
     * A getter for a list of participants
     * @return A list containing all the registered players who are playing
     */
    public static List<Player> getParticipants() {
        List<Player> players = new ArrayList<>();
        for (Participant p : participantList) {
            if (p.isPlaying()) players.add(p.getPlayer());
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
            players.add(p.getPlayer());
        }
        return players;
    }

    /**
     * Used to unregister all players
     */
    public static void resetAll() {
        participantList.clear();
    }

}
