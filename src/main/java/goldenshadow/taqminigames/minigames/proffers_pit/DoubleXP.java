package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.event.BossbarWrapper;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.util.UUID;

public class DoubleXP extends ProfEvent {

    private final String mat;
    private final UUID barUUID;
    private final String formatted;

    public DoubleXP(String materialName) {
        super("A profession XP bomb has been thrown! Mining " + materialName + " will yield 3x as much XP for the next 2 minutes!", null, false);
        mat = materialName;
        formatted = ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Triple XP for " + materialName + " " +  ChatColor.RESET + ChatColor.GRAY + "[%ds]";
        barUUID = BossbarWrapper.createBossbar(String.format(formatted, 120) , BarColor.BLUE, BarStyle.SOLID, 1);
    }

    @Override
    public void tick() {
        if (tick > 120) {
            isDone = true;
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Triple XP for " + mat + " has run out!"));
            BossbarWrapper.destroyBossbar(barUUID);
        } else {
            BossbarWrapper.updateTitle(barUUID, String.format(formatted, (120 - tick)));
            BossbarWrapper.updateProgress(barUUID, 1 - ((double) tick) / 120);
        }
        super.tick();
    }
}
