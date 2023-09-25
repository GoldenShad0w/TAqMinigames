package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.minigames.NesaakFight;
import goldenshadow.taqminigames.minigames.ProffersPit;
import goldenshadow.taqminigames.minigames.SkyIslandLootrun;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
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
                            if (event.getClickedBlock().getType() == Material.SNOW_BLOCK || event.getClickedBlock().getType() == Material.SNOW) {
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
                                for (Entity e : event.getPlayer().getWorld().getNearbyEntities(event.getPlayer().getLocation(), 3,2,3)) {
                                    if (e.getScoreboardTags().contains("m_prof_shop")) {
                                        game.huntedToggled(event.getPlayer());
                                        return;
                                    }
                                }
                                event.getPlayer().sendMessage(ChatMessageFactory.singleLineInfo("You need to be closer to a tool merchant to toggle hunted mode!"));
                            }
                        }
                    }
                }
            }
            if (TAqMinigames.minigame instanceof SkyIslandLootrun game) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getHand() == EquipmentSlot.HAND) {
                        game.itemUsed(event.getPlayer(), event.getMaterial());
                    }
                }
            }
        }
    }
}
