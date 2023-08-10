package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.minigames.AledarCartRacing;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;

public class VehicleEntityCollision implements Listener {

    @EventHandler
    public void event(VehicleEntityCollisionEvent event) {
        if (TAqMinigames.isRunning()) {
            if (TAqMinigames.minigame instanceof AledarCartRacing game) {
                game.boatCollide(event);
            }
        }
    }
}
