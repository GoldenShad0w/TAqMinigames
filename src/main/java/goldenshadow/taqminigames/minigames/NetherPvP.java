package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Timer;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class NetherPvP extends Minigame {

    private final HashMap<UUID, Integer> stageMap = new HashMap<>();
    private final HashMap<UUID, Integer> highestStageReachedMap = new HashMap<>();
    private final HashMap<UUID, Long> lastKillMap = new HashMap<>();
    private final String[] deathMessages = {"%s was murdered by %s!", "%s didn't stand a chance against %s!", "%s was annihilated by %s!", "%s was killed by %s!", "%s lost to %s!", "%s should stay away from %s in the future!"};

    public NetherPvP() {
        gameState = GameState.STARTING;
        scoreManager = new ScoreManager("Emeralds", true);

        timer = new Timer(0, 29, () -> timer = new Timer(9,59, this::end));
        for (Player player : ParticipantManager.getParticipants()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, true, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 10, false, false, false));
            player.setLevel(105);
            stageMap.put(player.getUniqueId(), 0);
            highestStageReachedMap.put(player.getUniqueId(), 0);
        }

        ParticipantManager.teleportAllPlayers(Constants.NETHER_PVP_TUTORIAL_LOCATION);
    }

    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Nether PvP", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to kill other players and not die yourself", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Every time you kill a player, you get a new weapon with a special ability", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Dying will downgrade your weapon by one kill", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 16 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The game ends when a player has achieved a kill with every weapon", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 20 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Reaching a new weapon for the first time will give you " + ((int) (Constants.NETHER_PVP_STAGE_COMPLETE * ScoreManager.getScoreMultiplier())) + " emeralds", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Being the one who gets a kill with every weapon will earn you " + ((int) (Constants.NETHER_PVP_COMPLETE * ScoreManager.getScoreMultiplier())) + " emeralds", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 25 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 5 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                }
            }
            case 27 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 3 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                }
            }
            case 28 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 2 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                }
            }
            case 29 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 1 second!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                }
            }
            case 30 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Good Luck!"));
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    Utilities.giveAurumItem(player, Constants.NETHER_PVP_ITEM_NAMES[0]);
                    player.teleport(getRandomSpawn());
                    player.setBedSpawnLocation(getRandomSpawn(), true);
                    togglePvP(true);
                }
                gameState = GameState.RUNNING;
            }
        }
        super.tick();
    }

    @Override
    public void end() {
        togglePvP(false);
        for (Player p : ParticipantManager.getParticipants()) {
            p.setGameMode(GameMode.SPECTATOR);
            p.getInventory().clear();
            p.setLevel(0);
        }
        super.end();
    }

    @Override
    public void onDeath(Player player) {
        int i = Math.max(stageMap.getOrDefault(player.getUniqueId(), 0) - 1, 0);
        stageMap.put(player.getUniqueId(), i);
        player.sendMessage(ChatMessageFactory.singleLineInfo("You died! You are now at weapon " + (i+1) + "/8"));
        player.getInventory().clear();
        Utilities.giveAurumItem(player, Constants.NETHER_PVP_ITEM_NAMES[i]);
        player.setBedSpawnLocation(getRandomSpawn(), true);
    }

    public void killAchieved(Player killer, Player victim) {

        if (lastKillMap.getOrDefault(killer.getUniqueId(), System.currentTimeMillis()) > System.currentTimeMillis()) return;

        int i = stageMap.getOrDefault(killer.getUniqueId(), 0) + 1;

        lastKillMap.put(killer.getUniqueId(), System.currentTimeMillis() + 100);
        stageMap.put(killer.getUniqueId(), i);
        Bukkit.broadcastMessage(ChatColor.GOLD + String.format(deathMessages[ThreadLocalRandom.current().nextInt(0, deathMessages.length)], victim.getName(), killer.getName()));

        if (i < 8) {
            if (i > highestStageReachedMap.getOrDefault(killer.getUniqueId(), 0)) {
                scoreManager.increaseScore(killer, Constants.NETHER_PVP_STAGE_COMPLETE, "You reached weapon " + i + " for the first time!", true);
                highestStageReachedMap.put(killer.getUniqueId(), i);
            }
            killer.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 100));
            killer.playSound(killer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            killer.sendMessage(ChatMessageFactory.singleLineInfo("You killed " + victim.getName() + "! You are now at weapon " + (i + 1) + "/8"));
            killer.getInventory().clear();
            Utilities.giveAurumItem(killer, Constants.NETHER_PVP_ITEM_NAMES[i]);
            if (i == 7) {
                ChatMessageFactory.sendInfoBlockToAll(ChatColor.YELLOW + killer.getName() + " has received the last weapon!");
                Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo(killer.getName() + " has received the last weapon!"));
                killer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, PotionEffect.INFINITE_DURATION, 0, false, false, false));
            }
        } else {
            playerFinish(killer);
        }

    }

    private void playerFinish(Player player) {
        scoreManager.increaseScore(player, Constants.NETHER_PVP_STAGE_COMPLETE, "You reached weapon 8 for the first time!", true);
        scoreManager.increaseScore(player, Constants.NETHER_PVP_COMPLETE, "You finished the game!", true);
        Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo(player.getName() + " achieved a kill with every weapon!"));
        timer.runTaskEarly();
    }

    @Override
    public void playerReconnect(Player player) {
        if (gameState != GameState.RUNNING) {
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @Override
    public void playerDisconnect(Player player) {
    }

    @Override
    public Game getGame() {
        return Game.NETHER_PVP;
    }

    private Location getRandomSpawn() {
        return Constants.NETHER_PVP_SPAWN_LOCATIONS[ThreadLocalRandom.current().nextInt(0, Constants.NETHER_PVP_SPAWN_LOCATIONS.length)];
    }

    private void togglePvP(boolean b) {
        if (b) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "aurum api enable_pvp");
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "aurum api disable_pvp");
        }
    }
}
