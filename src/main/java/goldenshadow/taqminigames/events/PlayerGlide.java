package goldenshadow.taqminigames.events;


import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.AvosRace;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

public class PlayerGlide implements Listener {

    @EventHandler
    public void glideEvent(EntityToggleGlideEvent event) {
        if (TAqMinigames.minigame != null) {
            if (TAqMinigames.minigame instanceof AvosRace a) {
                if (event.getEntity() instanceof Player player) {
                    if (Bukkit.getOnlinePlayers().contains(player)) {
                        if (!event.isGliding()) {
                            if (a.isHotFloor(player)) {
                                a.onDeath(player);
                            }
                        }
                    }
                }
            }
        }
    }
}
