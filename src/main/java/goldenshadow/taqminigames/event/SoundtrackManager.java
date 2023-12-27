package goldenshadow.taqminigames.event;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.SoundFile;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class SoundtrackManager {

    private static SoundFile soundFile = null;
    private static boolean loop = false;


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

        p.playSound(p, soundFile.name(), SoundCategory.RECORDS, 1,1);
        if (loop) {
            final String oldName = soundFile.name();
            Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                if (oldName.equals(SoundtrackManager.soundFile.name())) {
                    play(p);
                }
            }, soundFile.length() / 50);
        }
    }


}
