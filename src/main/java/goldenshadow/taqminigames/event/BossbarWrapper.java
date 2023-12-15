package goldenshadow.taqminigames.event;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class BossbarWrapper {

    private static final HashMap<UUID, BossBar> barList = new HashMap<>();

    /**
     * Used to create a new bossbar
     * @param title The text it should display
     * @param color The color the bar should have
     * @param style The style it should have
     * @param initialProgress The initial percentage that should be filled out
     * @param barFlags Any additional flags
     * @return The UUID of the bar. Useful for being able to edit and delete this specific bossbar later on
     */
    public static UUID createBossbar(String title, BarColor color, BarStyle style, double initialProgress, BarFlag... barFlags) {
        BossBar bar = Bukkit.createBossBar(title, color, style, barFlags);
        bar.setProgress(initialProgress);
        for (Player player : ParticipantManager.getAll()) {
            bar.addPlayer(player);
        }
        UUID uuid = UUID.randomUUID();
        barList.put(uuid, bar);
        return uuid;
    }

    /**
     * Used to delete a bossbar
     * @param uuid The UUID of the bossbar
     */
    public static void destroyBossbar(UUID uuid) {
        if (barList.containsKey(uuid)) {
            barList.get(uuid).removeAll();
            barList.remove(uuid);
        }
    }

    /**
     * Used to update the title of a bossbar
     * @param uuid The UUID of the bossbar
     * @param string The new title
     */
    public static void updateTitle(UUID uuid, String string) {
        if (barList.containsKey(uuid)) {
            barList.get(uuid).setTitle(string);
        }
    }

    /**
     * Used to update the progress on a bossbar
     * @param uuid The UUID of the bossbar
     * @param percentage The new percentage (value between 0 and 1)
     */
    public static void updateProgress(UUID uuid, double percentage) {
        if (barList.containsKey(uuid)) {
            barList.get(uuid).setProgress(percentage);
        }
    }

    /**
     * Makes a player see all bossbars
     * @param player The player
     */
    public static void addPlayer(Player player) {
        barList.values().forEach(b -> b.addPlayer(player));
    }

    /**
     * Used to delete all bossbars at once
     */
    public static void destroyAll() {
        barList.values().forEach(BossBar::removeAll);
        barList.clear();
    }


}
