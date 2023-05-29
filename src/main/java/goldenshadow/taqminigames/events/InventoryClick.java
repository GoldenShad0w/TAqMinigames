package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.ProffersPit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClick implements Listener {

    @EventHandler
    public void invClick(InventoryClickEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof ProffersPit game) {
                game.shopEvent(event);
            }
        }
    }
}
