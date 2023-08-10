package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.AledarCartRacing;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void event(PlayerDropItemEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof AledarCartRacing game) {
                if (event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
                    event.setCancelled(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> game.abilityUsed(event.getPlayer(), event.getItemDrop().getItemStack().getType()), 1L);
                }
            }
        }
    }
}
