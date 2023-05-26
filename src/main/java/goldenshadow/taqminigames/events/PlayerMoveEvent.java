package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.util.Trigger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEvent implements Listener {

    @EventHandler
    public void playerMoveEvent(org.bukkit.event.player.PlayerMoveEvent event) {
        if (!Trigger.isRegisterEmpty()) {
            Trigger.checkTriggers(event.getPlayer());
        }
    }
}
