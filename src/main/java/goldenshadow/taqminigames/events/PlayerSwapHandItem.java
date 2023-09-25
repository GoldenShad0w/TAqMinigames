package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.AledarCartRacing;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItem implements Listener {

    @EventHandler
    public void event(PlayerSwapHandItemsEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof AledarCartRacing game) {
                if (event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                    event.setCancelled(true);
                    if (event.getOffHandItem() != null) {
                        game.abilityUsed(event.getPlayer(), event.getOffHandItem().getType());
                    }
                }
            }
        }
    }
}
