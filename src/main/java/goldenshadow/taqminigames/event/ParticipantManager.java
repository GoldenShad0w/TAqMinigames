package goldenshadow.taqminigames;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParticipantManager {

    private static final List<Participant> participantList = new ArrayList<>();

    public static void teleportAllPlayers(Location location) {
        participantList.forEach(x -> x.getPlayer().teleport(location));
    }

    public static void addParticipant(Player player, boolean isPlaying) {
        participantList.add(new Participant(player, isPlaying));
    }

    public static List<Player> getParticipantsWithoutSpectators() {
        List<Player> players = new ArrayList<>();
        for (Participant p : participantList) {
            if (p.isPlaying()) players.add(p.getPlayer());
        }
        return players;
    }

    public static List<Player> getParticipants() {
        List<Player> players = new ArrayList<>();
        for (Participant p : participantList) {
            players.add(p.getPlayer());
        }
        return players;
    }

}
