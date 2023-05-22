package goldenshadow.taqminigames;

import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.util.Constants;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GameSelection {

    private final List<Game> possibleGames;
    private int tick = 0;
    private final int taskID;
    private final List<TextDisplay> textDisplays = new ArrayList<>();

    public GameSelection(List<Game> possibleGames) {
        this.possibleGames = possibleGames;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(TAqMinigames.getPlugin(), this::tick, 0, 1L);
    }

    private void tick() {
        if (tick > 20000) Bukkit.getScheduler().cancelTask(taskID);
        switch (tick) {
            case 1 -> {
                for (Player p : ParticipantManager.getParticipants()) {
                    p.sendTitle(" ", ChatColor.GOLD + "The next minigame is about to be picked", 10, 20, 10);
                    p.teleport(Constants.LOBBY_GAME_SELECTION);
                }
            }
        }
        tick++;
    }


    private void spawnTextDisplay(Location location, String text) {
        TextDisplay textDisplay = (TextDisplay) location.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY, false);
        textDisplay.setText(text);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.getTransformation().getScale().set(0,0,0);
        textDisplay.setInterpolationDelay(0);
        textDisplay.setInterpolationDuration(10);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> textDisplay.getTransformation().getScale().set(1,1,1), 1L);
    }

    private void spawnFireWork(Location location, Color color) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREBALL, false);
        FireworkMeta fwm = firework.getFireworkMeta();
        fwm.addEffect(FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.BALL).withTrail().build());
        firework.setFireworkMeta(fwm);
        firework.addScoreboardTag("m_firework");
        firework.detonate();
    }

}
