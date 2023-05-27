package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.NesaakFight;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof NesaakFight game) {
                if (event.hasBlock()) {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        if (event.getClickedBlock() != null) {
                            if (event.getClickedBlock().getType() == Material.SNOW_BLOCK) {
                                if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.NETHERITE_SHOVEL) {
                                    game.snowClicked(event.getPlayer(), event.getClickedBlock());
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
