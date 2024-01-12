package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.event.SoundtrackManager;
import goldenshadow.taqminigames.util.Trigger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerMoveEvent implements Listener {

    private final static List<UUID> connectedAwaitingMovement = new ArrayList<>();

    @EventHandler
    public void playerMoveEvent(org.bukkit.event.player.PlayerMoveEvent event) {
        if (!Trigger.isRegisterEmpty()) {
            Trigger.checkTriggers(event.getPlayer());
        }
        if (connectedAwaitingMovement.contains(event.getPlayer().getUniqueId())) {
            connectedAwaitingMovement.remove(event.getPlayer().getUniqueId());
            if (SoundtrackManager.isCurrentTackLooped()) {
                SoundtrackManager.play(event.getPlayer());
            }
        }
    }

    /**
     * This method is used to tell the plugin to wait for a connecting player to move before it tries to play the current soundtrack as otherwise, the player might not be done loading the resource pack yet
     * @param uuid The uuid of the connecting player
     */
    public static void registerConnectedAwaitingMovement(UUID uuid) {
        connectedAwaitingMovement.add(uuid);
    }
}
