package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.minigames.ProffersPit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBreakBlock implements Listener {

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof ProffersPit game) {
                if (ParticipantManager.getParticipants().contains(event.getPlayer())) {
                    game.blockMined(event.getPlayer(), event.getBlock());
                }
            }
        }
    }
}
