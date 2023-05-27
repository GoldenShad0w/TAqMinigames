package goldenshadow.taqminigames;

import goldenshadow.taqminigames.commands.Command;
import goldenshadow.taqminigames.commands.TabComplete;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.event.GameSelection;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.event.ScoreboardWrapper;
import goldenshadow.taqminigames.events.*;
import goldenshadow.taqminigames.minigames.Minigame;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class TAqMinigames extends JavaPlugin {


    private static TAqMinigames plugin;
    private static boolean isRunning = false;
    public static int gameIndex = 0;
    private static GameSelection gameSelection = null;
    public static List<Game> possibleGames = new ArrayList<>(Arrays.asList(Game.values()));
    public static ScoreManager totalScoreManager;
    public static Minigame minigame = null;

    @Override
    public void onEnable() {
        plugin = this;
        
        registerEvents();
        getConfig().options().copyDefaults(true);
        saveConfig();
        initLoops();
        Objects.requireNonNull(this.getCommand("minigames")).setExecutor(new Command());
        Objects.requireNonNull(this.getCommand("minigames")).setTabCompleter(new TabComplete());

        Bukkit.broadcastMessage(Constants.AURA_TOWER_CENTERS[0].clone().toString());
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Utilities::registerLobbyTrigger, 1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void start() {
        if (!isRunning) {
            isRunning = true;
            gameSelection = null;
            totalScoreManager = new ScoreManager("Emeralds", true);
            for (Player p : Bukkit.getOnlinePlayers()) {
                ParticipantManager.addParticipant(p, p.getGameMode() == GameMode.ADVENTURE);
                p.sendTitle(ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + "Welcome", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "To TAq Minigames", 20, 100,20);
                //TODO: enable scoreboard for player
            }
            ScoreManager.calculateScores(ParticipantManager.getParticipants().size());
        }
    }

    public static void stop() {
        isRunning = false;
        ParticipantManager.teleportAllPlayers(Constants.LOBBY);
        gameIndex = 0;
        gameSelection = null;
        Bukkit.getOnlinePlayers().forEach(ScoreboardWrapper::removeScoreboard);
        ParticipantManager.resetAll();
        possibleGames = new ArrayList<>(Arrays.asList(Game.values()));

    }

    public static void nextMinigame(boolean weighted) {

        if (gameIndex > 0) {
            ScoreManager.increaseMultiplier();
            for (Player p : ParticipantManager.getAll()) {
                ChatMessageFactory.sendInfoMessageBlock(p, " ", ChatColor.YELLOW + "Emerald multiplier increased to " + ScoreManager.getScoreMultiplier() + "!", " ");
            }
        }
        gameSelection = new GameSelection(possibleGames, weighted);
        gameIndex++;
    }


    private static void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerDamage(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerLeave(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerConnect(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), plugin);
        Bukkit.getPluginManager().registerEvents(new ProjectileEvents(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerGlide(), plugin);
    }

    public static TAqMinigames getPlugin() {
        return plugin;
    }

    public static void initLoops() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRunning) {
                    ScoreboardWrapper.updateBoards();
                    if (gameSelection != null) {
                        if (gameSelection.isInProgress()) {
                            gameSelection.tick();
                        } else {
                            gameSelection = null;
                        }
                    }

                    if (minigame != null) {
                        minigame.tick();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isRunning) {
                    if (minigame != null) {
                        minigame.fastTick();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
    }

    public static boolean isRunning() {
        return isRunning;
    }
}
