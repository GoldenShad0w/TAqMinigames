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

    public DoubleXP(String materialName) {
        super("A profession XP bomb has been thrown! Mining " + materialName + " will yield 3x as much XP for the next 2 minutes!", null, false);
        ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("A profession XP bomb has been thrown! Mining " + materialName + " will yield 3x as much XP for the next 2 minutes!", 50), ChatColor.YELLOW).toArray(String[]::new));
        mat = materialName;
        barUUID = BossbarWrapper.createBossbar( ChatColor.AQUA  + "Triple XP for " + materialName, BarColor.BLUE, BarStyle.SOLID, 1);
    }

    @Override
    public void tick() {
        if (tick > 120) {
            isDone = true;
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Triple XP for " + mat + " has run out!"));
            BossbarWrapper.destroyBossbar(barUUID);
        }
        super.tick();
    }
}
