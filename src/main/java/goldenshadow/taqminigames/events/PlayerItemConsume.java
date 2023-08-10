package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.ExcavationSiteE;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerItemConsume implements Listener {

    @EventHandler
    public void event(PlayerItemConsumeEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof ExcavationSiteE game) {
                if (event.getItem().getType() == Material.POTION) {
                    game.potionUsed(event.getPlayer());
                }
            }
        }
    }
}
