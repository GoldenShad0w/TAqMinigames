package goldenshadow.taqminigames.events;


import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.event.SoundtrackManager;
import goldenshadow.taqminigames.minigames.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (TAqMinigames.isRunning()) {
            SoundtrackManager.stopAll(event.getEntity());
            Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> SoundtrackManager.play(event.getEntity()), 10L);
            if (TAqMinigames.minigame instanceof NesaakFight nesaakFight) {
                nesaakFight.onDeath(event.getEntity());
            }
            if (TAqMinigames.minigame instanceof ProffersPit pit) {
                pit.onDeath(event.getEntity());
            }
            if (TAqMinigames.minigame instanceof ExcavationSiteE e) {
                e.onDeath(event.getEntity());
            }
            if (TAqMinigames.minigame instanceof NetherPvP pvp) {
                pvp.onDeath(event.getEntity());
                if (event.getEntity().getKiller() != null) {
                    pvp.killAchieved(event.getEntity().getKiller(), event.getEntity());
                }
            }
            if (TAqMinigames.minigame instanceof SkyIslandLootrun lr) {
                lr.onDeath(event.getEntity());
            }
        }
    }

}
