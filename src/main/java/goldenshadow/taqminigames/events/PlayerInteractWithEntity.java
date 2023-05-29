package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.ProffersPit;
import goldenshadow.taqminigames.minigames.proffers_pit.ShopItems;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractWithEntity implements Listener {

    @EventHandler
    public void interact(PlayerInteractEntityEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof ProffersPit) {
                if (event.getRightClicked().getScoreboardTags().contains("m_prof_shop")) {
                    event.getPlayer().openInventory(ShopItems.getShopGUI());
                }
            }
        }
    }
}
