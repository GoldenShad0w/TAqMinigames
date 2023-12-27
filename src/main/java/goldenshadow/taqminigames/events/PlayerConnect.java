package goldenshadow.taqminigames.events;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.event.*;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.permissions.ServerOperator;

public class PlayerConnect implements Listener {

    @EventHandler
    public void playerConnect(PlayerLoginEvent event) {
        if (TAqMinigames.isRunning()) {
            if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                    BossbarWrapper.addPlayer(event.getPlayer());
                    SoundtrackManager.play(event.getPlayer());
                    if (ParticipantManager.isRegistered(event.getPlayer())) {
                        Participant p = ParticipantManager.getParticipant(event.getPlayer());
                        assert p != null;
                        if (p.isPlaying()) {
                            //player
                            if (TAqMinigames.minigame != null) {
                                if (TAqMinigames.minigame.getGame() == p.lastGame) {
                                    TAqMinigames.minigame.playerReconnect(p.getPlayer());
                                } else {
                                    event.getPlayer().getInventory().clear();
                                    event.getPlayer().setGameMode(GameMode.SPECTATOR);
                                    event.getPlayer().sendMessage(ChatMessageFactory.singleLineInfo("A new minigame has started while you were offline! You will be able to play again after this minigame ends."));
                                }
                            } else {
                                event.getPlayer().getInventory().clear();
                                event.getPlayer().getActivePotionEffects().clear();
                                event.getPlayer().teleport(Constants.LOBBY);
                            }
                        } else {
                            event.getPlayer().setGameMode(GameMode.SPECTATOR);
                        }
                    } else {
                        //not registered
                        event.getPlayer().teleport(Constants.LOBBY);
                        Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).forEach(x -> x.sendMessage(ChatMessageFactory.adminWarnMessage(event.getPlayer().getName() + " has joined that game and is neither a participant or spectator!")));
                    }
                }, 5L);
            }
        }
        if (TAqMinigames.inPreStartPhase) {
            if (event.getResult() == PlayerLoginEvent.Result.ALLOWED) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                    ScoreboardWrapper.queueData(event.getPlayer(),
                            " ",
                            ChatColor.AQUA + "Starting soon!");
                    event.getPlayer().teleport(Constants.LOBBY);
                    event.getPlayer().getInventory().clear();
                    ScoreboardWrapper.updateBoards();
                    SoundtrackManager.play(event.getPlayer());
                }, 5L);
            }
        }

    }
}