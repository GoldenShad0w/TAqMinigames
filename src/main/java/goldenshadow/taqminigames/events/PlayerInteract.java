package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.minigames.NesaakFight;
import goldenshadow.taqminigames.minigames.ProffersPit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

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
                                    if (event.getHand() == EquipmentSlot.HAND) {
                                        game.snowClicked(event.getPlayer(), event.getClickedBlock());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (TAqMinigames.minigame instanceof ProffersPit game) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.FIRE_CHARGE) {
                        if (event.getHand() == EquipmentSlot.HAND) {
                            if (ParticipantManager.getParticipants().contains(event.getPlayer())) {
                                game.huntedToggled(event.getPlayer());
                            }
                        }
                    }
                }
            }
        }
    }
}
