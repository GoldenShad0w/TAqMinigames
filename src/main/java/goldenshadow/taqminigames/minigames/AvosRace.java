package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.event.SoundtrackManager;
import goldenshadow.taqminigames.util.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class representing the avos race minigame
 */
public class AvosRace extends Minigame {

    private int finishedPlayers = 0;
    private final int[] stageCompletions = {0,0,0,0,0};
    private final HashMap<UUID, Integer> playerStages = new HashMap<>(); // value >= 5 means finished
    private static final String[] deathMessages = {" flew into a wall!", " needs their flying licence revoked!", " thought they could fly though walls!", " had their eyes closed!", " learned that the wall was stronger than them!", " skill issued!", " has a serious case of skill issue!", " crash landed!", " overestimated themselves", " didn't make it!", " needs some flying lessons!"};
    //private final int mapIndex;

    /**
     * Used to start a new avos race minigame
     */
    public AvosRace() {
        gameState = GameState.STARTING;
        scoreManager = new ScoreManager("Emeralds", true);
        SoundtrackManager.stopAllForAll();

        //mapIndex = TAqMinigames.getPlugin().getConfig().getInt("avos-race-map") - 1;

        for (Player player : ParticipantManager.getParticipants()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, true, false, false));
        }

        assert TAqMinigames.getEventConfig().getGenericData().WORLD != null;


        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.FALL_DAMAGE, true), 5L);




        for (Player p : ParticipantManager.getParticipants()) {
            playerStages.put(p.getUniqueId(), 0);
        }

        timer = new Timer(0, 29, () -> timer = new Timer(9,59, this::end));

        ParticipantManager.teleportAllPlayers(TAqMinigames.getEventConfig().getAvosRaceData().START_LOCATION);
        Utilities.fillAreaWithBlock(TAqMinigames.getEventConfig().getAvosRaceData().START_BARRIERS[0], TAqMinigames.getEventConfig().getAvosRaceData().START_BARRIERS[1], Material.BARRIER, Material.AIR);
        Utilities.fillAreaWithBlock(TAqMinigames.getEventConfig().getAvosRaceData().START_AIR[0], TAqMinigames.getEventConfig().getAvosRaceData().START_AIR[1], Material.AIR, Material.BARRIER);

        for (BoundingBox b : TAqMinigames.getEventConfig().getAvosRaceData().STAGES) {
            Trigger.register(new Trigger(b, TAqMinigames.getEventConfig().getGenericData().WORLD, p -> (p.getGameMode() == GameMode.ADVENTURE), this::stageComplete, Utilities.secondsToMillis(1000), false, false));
        }
        Trigger.register(new Trigger(TAqMinigames.getEventConfig().getAvosRaceData().FINISH_BOX, TAqMinigames.getEventConfig().getGenericData().WORLD, p -> (p.getGameMode() == GameMode.ADVENTURE), this::finish, Utilities.secondsToMillis(1000), false, false));
    }

    /**
     * 1hz loop
     */
    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Avos Race", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to reach the end of this track as fast as possible", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Hitting a wall or landing before you reach the end will reset you to the beginning", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The track is split up into multiple stages. Completing a stage and finishing will earn you emeralds", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 14 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Completing a stage can give you up to " + (int) (TAqMinigames.getEventConfig().getAvosRaceData().STAGE_COMPLETE_POINTS * ScoreManager.getScoreMultiplier()) + " emeralds, with the reward decreasing by " + (int) (TAqMinigames.getEventConfig().getAvosRaceData().FALLOFF_POINTS * ScoreManager.getScoreMultiplier()) + " emeralds every time someone else completes the stage before you", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 18 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Finishing will earn you " + (int) (TAqMinigames.getEventConfig().getAvosRaceData().FINISH_POINTS * ScoreManager.getScoreMultiplier()) + " emeralds, with the same decrease as the stage completion reward", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 22 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Remember that you have you to jump mid-air to open your elytra", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Once you are gliding, there only way to regain momentum is to glide downhill", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 25 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 5 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING,SoundCategory.VOICE, 1, 1);
                    if (ParticipantManager.getParticipants().contains(player)) {
                        assert player.getEquipment() != null;
                        player.getEquipment().setChestplate(getElytraItem());
                    }
                }
            }
            case 27 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 3 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING,SoundCategory.VOICE, 1, 1);
                }
            }
            case 28 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 2 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING,SoundCategory.VOICE, 1, 1);
                }
            }
            case 29 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 1 second!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING,SoundCategory.VOICE, 1, 1);
                }
            }
            case 30 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Good Luck!"));
                    player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP,SoundCategory.VOICE, 1, 1);
                    Utilities.fillAreaWithBlock(TAqMinigames.getEventConfig().getAvosRaceData().START_BARRIERS[0], TAqMinigames.getEventConfig().getAvosRaceData().START_BARRIERS[1], Material.AIR, Material.BARRIER);

                }
                gameState = GameState.RUNNING;
            }
        }
        super.tick();
    }

    /**
     * Used for when a player crashes or lands on a hot floor
     * @param player The players who died
     */
    @Override
    public void onDeath(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH,SoundCategory.VOICE, 1,1);
        player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, player.getLocation(), 1, 0, 0,0,0);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,SoundCategory.VOICE, 1,1);
        player.sendMessage(ChatMessageFactory.singleLineInfo("You died..."));
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + deathMessages[ThreadLocalRandom.current().nextInt(0, deathMessages.length)]);
        player.teleport(TAqMinigames.getEventConfig().getAvosRaceData().START_LOCATION);
    }

    /**
     * Used to re-insert a player who reconnected back into the game
     * @param player The player who reconnected
     */
    @Override
    public void playerReconnect(Player player) {
        if (gameState != GameState.RUNNING) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            if (playerStages.containsKey(player.getUniqueId())) {
                if (playerStages.get(player.getUniqueId()) < 5) {
                    onDeath(player);
                    return;
                }
            }
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    /**
     * Used to correctly handle a player disconnecting. There is no need to do anything for this game
     * @param player The player
     */
    @Override
    public void playerDisconnect(Player player) {}

    /**
     * Getter for the current game
     * @return The current game
     */
    @Override
    public Game getGame() {
        return Game.AVOS_RACE;
    }

    /**
     * Used to check if a floor can be landed on or if the death event should be called
     * @param player The player whose floor should be checked
     * @return True if it is a hot floor, otherwise false
     */
    public boolean isHotFloor(Player player) {
        for (BoundingBox b : TAqMinigames.getEventConfig().getAvosRaceData().COLD_FLOORS) {
            if (b.overlaps(player.getBoundingBox())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Utility method used to get the elytra item
     * @return The elytra item
     */
    public static ItemStack getElytraItem() {
        ItemStack itemStack = new ItemStack(Material.ELYTRA);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + "Avos Wings");
        meta.addEnchant(Enchantment.BINDING_CURSE, 1,true);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Used for when a player completes the track
     * @param player The player who completed it
     */
    private void finish(Player player) {
        playerStages.put(player.getUniqueId(), 5);
        finishedPlayers++;
        scoreManager.increaseScore(player, Math.max(TAqMinigames.getEventConfig().getAvosRaceData().FINISH_POINTS - ((finishedPlayers-1) * TAqMinigames.getEventConfig().getAvosRaceData().FALLOFF_POINTS), 300), Utilities.getNumberSuffix(finishedPlayers) + " to finish the track!", true);
        Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo(player.getName() + " was " + Utilities.getNumberSuffix(finishedPlayers) + " to finish the track!"));
        player.setGameMode(GameMode.SPECTATOR);
        if (Bukkit.getOnlinePlayers().stream().filter(x -> x.getGameMode() == GameMode.ADVENTURE).toArray().length == 0) {
            timer.runTaskEarly();
        }
        
    }

    /**
     * Used for when a player completes a stage
     * @param player The player who completed the stage
     */
    private void stageComplete(Player player) {
        int oldStage = playerStages.get(player.getUniqueId());
        int placement = stageCompletions[oldStage] + 1;
        scoreManager.increaseScore(player, Math.max(TAqMinigames.getEventConfig().getAvosRaceData().STAGE_COMPLETE_POINTS - ((placement-1) * TAqMinigames.getEventConfig().getAvosRaceData().FALLOFF_POINTS), (int) (100 * ScoreManager.getScoreMultiplier())), Utilities.getNumberSuffix(placement) + " to complete this stage!", true);
        stageCompletions[oldStage] = placement;
        playerStages.put(player.getUniqueId(), oldStage+1);
    }

    /**
     * Used to end the minigame
     */
    @Override
    public void end() {
        assert TAqMinigames.getEventConfig().getGenericData().WORLD != null;
        TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.FALL_DAMAGE, false);
        for (Player p : ParticipantManager.getParticipants()) {
            p.setGameMode(GameMode.SPECTATOR);
        }
        super.end();
    }
}
