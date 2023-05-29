package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.util.ChatMessageFactory;
import org.bukkit.Bukkit;

public class DoubleXP extends ProfEvent {

    private final String mat;

    public DoubleXP(String materialName) {
        super("A profession XP bomb has been thrown! Mining " + materialName + " will yield 3x as much XP for the next 2 minutes!");
        mat = materialName;
    }

    @Override
    public void tick() {
        if (tick > 120) {
            isDone = true;
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Triple XP for " + mat + " has run out!"));
        }
        super.tick();
    }
}
