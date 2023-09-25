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

    public static void destroyBossbar(UUID uuid) {
        if (barList.containsKey(uuid)) {
            barList.get(uuid).removeAll();
            barList.remove(uuid);
        }
    }

    public static void updateTitle(UUID uuid, String string) {
        if (barList.containsKey(uuid)) {
            barList.get(uuid).setTitle(string);
        }
    }

    public static void updateProgress(UUID uuid, double percentage) {
        if (barList.containsKey(uuid)) {
            barList.get(uuid).setProgress(percentage);
        }
    }

    public static void addPlayer(Player player) {
        barList.values().forEach(b -> b.addPlayer(player));
    }

    public static void destroyAll() {
        barList.values().forEach(BossBar::removeAll);
        barList.clear();
    }


}
