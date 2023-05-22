package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.event.ScoreboardWrapper;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Timer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * An abstract class outlining and implementing key mechanic that all minigames must share
 */
public abstract class Minigame {

    protected int tick = 0;
    protected int fastTick = 0;
    protected GameState gameState;
    protected Timer timer;
    protected ScoreManager scoreManager;

    /**
     * Main loop of the minigame. This should usually run on a 1hz clock
     */
    public void tick() {
        if (timer != null) {
            timer.tick();
        }
        tick++;
    }


    /**
     * A 20hz loop that can be used to things where a 1hz loop is not fast enough
     */
    public void fastTick() {
        fastTick++;
    }



    /**
     * Method that should be called when the game ends
     */
    public void end() {
        gameState = GameState.ENDING;
        timer = null;
        TAqMinigames.totalScoreManager.merge(scoreManager);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + String.valueOf(ChatColor.STRIKETHROUGH) + "==================================================");
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "Game Rankings:");
            for (String s : scoreManager.getSortedDisplayList(ChatColor.AQUA, ChatColor.GREEN)) {
                Bukkit.broadcastMessage(s);
            }
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + String.valueOf(ChatColor.STRIKETHROUGH) + "==================================================");

        }, 60L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + String.valueOf(ChatColor.STRIKETHROUGH) + "==================================================");
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "Global Rankings:");
            for (String s : TAqMinigames.totalScoreManager.getSortedDisplayList(ChatColor.AQUA, ChatColor.GREEN)) {
                Bukkit.broadcastMessage(s);
            }
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatColor.DARK_AQUA + String.valueOf(ChatColor.STRIKETHROUGH) + "==================================================");

        }, 100L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Teleporting back to lobby in 5 seconds!")), 120L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Teleporting back to lobby in 3 seconds!")), 200L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Teleporting back to lobby in 2 seconds!")), 240L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Teleporting back to lobby in 1 second!")), 280L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Teleporting..."));
            ParticipantManager.teleportAllPlayers(Constants.LOBBY);
            for (Player p : ParticipantManager.getParticipants()) {
                p.getInventory().clear();
                p.setGameMode(GameMode.ADVENTURE);
                p.setHealth(20);
                updateToLobbyBoard(p);
            }
            TAqMinigames.minigame = null;
        }, 320L);

    }

    /**
     * Used to insert a player into a running game
     * @param player The player who should be inserted
     */
    protected void insertPlayer(Player player) {
        ParticipantManager.addParticipant(player, true);
        ScoreManager.calculateScores(ParticipantManager.getParticipants().size());
    }

    /**
     * Method that should be called when a player dies or an equal event occurs
     * @param player The players who died
     */
    public abstract void onDeath(Player player);

    /*

    Game: Aura & Volley
    Starting in: 00:30

    Your Emeralds: 0

    1. Someone 193 Emeralds

    4. Someone 160 Emeralds
    5. You 150 Emeralds
    6. Someone 3 Emeralds
     */

    /**
     * Used to update the scoreboard for a specific player
     * @param player The player whose scoreboard that should be updated
     */
    protected void updateScoreboard(Player player) {
        String[] data = scoreManager.getScoreboardLines(player, ChatColor.AQUA, ChatColor.GREEN);
        ScoreboardWrapper.queueData(player, " ",
                ChatColor.DARK_AQUA + "Game: " + ChatColor.AQUA + (TAqMinigames.minigame != null ? TAqMinigames.minigame.getGame().getLabel() : "---"), (gameState != GameState.ENDING) ? ChatColor.DARK_AQUA + gameState.getDescriptor() + ChatColor.AQUA + timer.toString() : ChatColor.DARK_AQUA + gameState.getDescriptor(),
                " ",
                ChatColor.DARK_AQUA + "Your " + scoreManager.getDescriptor() + ": " + ChatColor.GREEN + scoreManager.getScore(player.getUniqueId()),
                " ",
                data[0],
                " ",
                data[1],
                data[2],
                data[3],
                " "
                );
    }

    /**
     * Used to re-insert a player who reconnected
     * @param player The player who reconnected
     */
    public abstract void playerReconnect(Player player);

    /**
     * Used to update the players scoreboard to how it should be displayed while in the lobby
     * @param player The player
     */
    protected static void updateToLobbyBoard(Player player) {
        String[] data = TAqMinigames.totalScoreManager.getScoreboardLines(player, ChatColor.AQUA, ChatColor.GREEN);
        ScoreboardWrapper.queueData(player, " ",
                ChatColor.AQUA + "The next game will start soon!",
                " ",
                ChatColor.DARK_AQUA + "Your " + TAqMinigames.totalScoreManager.getDescriptor() + ": " + ChatColor.GREEN + TAqMinigames.totalScoreManager.getScore(player.getUniqueId()),
                " ",
                data[0],
                " ",
                data[1],
                data[2],
                data[3],
                " "
        );
    }

    /**
     * Getter for the current game
     * @return The current game
     */
    public abstract Game getGame();

}
