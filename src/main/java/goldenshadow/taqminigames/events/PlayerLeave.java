package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.event.ScoreboardWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        ScoreboardWrapper.removeScoreboard(event.getPlayer());
    }
}
