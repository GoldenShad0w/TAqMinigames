package goldenshadow.taqminigames.event;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.SoundFile;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SoundtrackManager {

    private static SoundFile soundFile = null;
    private static boolean loop = false;

    private static final HashMap<UUID, Integer> taskMap = new HashMap<>();

    /**
     * Used to change the current soundtrack
     * @param soundFile The sound file
     * @param loop Whether the soundtrack should loop
     */
    public static void setCurrent(SoundFile soundFile, boolean loop) {
        SoundtrackManager.soundFile = soundFile;
        SoundtrackManager.loop = loop;

        for (Player p : Bukkit.getOnlinePlayers()) {
            stopAll(p);
            play(p);
        }
    }

    /**
     * Used to stop any currently playing soundtracks for a player
     * @param p The player
     */
    public static void stopAll(Player p) {
        p.stopSound(SoundCategory.RECORDS);
        if (taskMap.containsKey(p.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(taskMap.get(p.getUniqueId()));
            taskMap.remove(p.getUniqueId());
        }
    }

    /**
     * Convenience method used to stop all soundtracks of all players
     */
    public static void stopAllForAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            stopAll(p);
        }
    }

    /**
     * Used to start playing a soundtrack to a player and initiate the looping if needed
     * @param p The player
     */
    public static void play(Player p) {
        if (soundFile == null) return;
        stopAll(p);
        p.playSound(p, soundFile.name(), SoundCategory.RECORDS, 1,1);
        if (loop) {
            final String oldName = soundFile.name();
            int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                if (oldName.equals(SoundtrackManager.soundFile.name())) {
                    play(p);
                }
            }, soundFile.length() / 50);
            taskMap.put(p.getUniqueId(), taskId);
        }
    }

    /**
     * Used to check if the current track loops. If not, it is recommended not to start it again when a player dies or reconnects
     * @return True if it does, false otherwise
     */
    public static boolean isCurrentTackLooped() {
        return loop;
    }

    /**
     * Used to get the current sound file being played
     * @return The sound file name
     */
    public static SoundFile getSoundFile() {
        return soundFile;
    }
}
