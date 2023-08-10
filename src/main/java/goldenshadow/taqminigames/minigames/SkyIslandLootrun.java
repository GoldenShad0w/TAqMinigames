package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.util.*;
import org.bukkit.*;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class SkyIslandLootrun extends Minigame{

    private final HashMap<UUID, List<UUID>> openedChests = new HashMap<>();

    public SkyIslandLootrun() {
        gameState = GameState.STARTING;
        scoreManager = new ScoreManager("Emeralds", true);

        timer = new Timer(0, 29, () -> timer = new Timer(15,0, this::end));
        for (Player player : ParticipantManager.getParticipants()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 500, 0, true, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 10, false, false, false));
        }

        ParticipantManager.teleportAllPlayers(Constants.SKY_TUTORIAL_LOCATION);
    }

    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Sky Island Lootrun", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to explore the sky islands and open as many chests as possible", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("There are multiple different areas with differing difficulty", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("When opening a chest, there is also a chance you will find a bonus item in it", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 14 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("These bonus items can be anything from a speed potion to a mythic", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 18 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Completing a section will give you emeralds and so will opening chests", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 22 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Completing a section will give you " + ((int) (Constants.SKY_SECTION_COMPLETE * ScoreManager.getScoreMultiplier())) + " emeralds", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Opening a chest will give you " + ((int) (Constants.SKY_T1 * ScoreManager.getScoreMultiplier())) + ", " + ((int) (Constants.SKY_T2 * ScoreManager.getScoreMultiplier())) + ", " + ((int) (Constants.SKY_T3 * ScoreManager.getScoreMultiplier())) + " or " + ((int) (Constants.SKY_T4 * ScoreManager.getScoreMultiplier())) + " depending on the tier of the chest", 50), ChatColor.YELLOW).toArray(String[]::new));
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

                    ParticipantManager.teleportAllPlayers(Constants.SKY_START_LOCATION);
                }
                gameState = GameState.RUNNING;
            }
        }
        if (tick > 320) {
            tick = 30;
        }
        super.tick();
    }

    public void itemUsed(Player player, Material material) {
        //TODO
    }

    public void chestOpened(Player player, Interaction interaction) {
        if (interaction.getScoreboardTags().contains("m_sky_chest")) {
            List<UUID> list = new ArrayList<>();
            if (openedChests.containsKey(player.getUniqueId())) {
                list = openedChests.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("You already opened this chest!"));
                    player.playSound(player, Sound.ENTITY_CAT_HISS, 1,1);
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            openedChests.put(player.getUniqueId(), list);
            if (interaction.getScoreboardTags().contains("m_sky_chest_t1")) {
                if (rollBonusItem(player, 1)) {
                    scoreManager.increaseScore(player, Constants.SKY_T1, "You opened a tier 1 chest!", true);
                }

            } else if (interaction.getScoreboardTags().contains("m_sky_chest_t2")) {
                if (rollBonusItem(player, 2)) {
                    scoreManager.increaseScore(player, Constants.SKY_T2, "You opened a tier 2 chest!", true);
                }
            } else if (interaction.getScoreboardTags().contains("m_sky_chest_t3")) {
                if (rollBonusItem(player, 3)) {
                    scoreManager.increaseScore(player, Constants.SKY_T3, "You opened a tier 3 chest!", true);
                }
            } else if (interaction.getScoreboardTags().contains("m_sky_chest_t4")) {
                if (rollBonusItem(player, 4)) {
                    scoreManager.increaseScore(player, Constants.SKY_T4, "You opened a tier 4 chest!", true);
                }
            }
        }
    }

    /**
     * Used to determine what bonus item the player should get
     * @param player The player
     * @param chestTier The level of the chest that was opened
     * @return Shows if a looted chest was found. false if it was, true otherwise
     */
    private boolean rollBonusItem(Player player, int chestTier) {
         switch (chestTier) {
            case 1 -> {
                double d = ThreadLocalRandom.current().nextDouble(0,1);
                if (d < 0.05) {
                    return giveBonusItem(player, BonusItem.DOUBLE_FABLED);
                } else if (d < 0.2) {
                    return giveBonusItem(player, BonusItem.LOOTED);
                } else if (d < 0.22) {
                    return giveBonusItem(player, BonusItem.MYTHIC);
                } else if (d < 0.37) {
                    return giveBonusItem(player, BonusItem.SPEED);
                } else if (d < 0.47) {
                    return giveBonusItem(player, BonusItem.JUMP);
                }
            }
            case 2 -> {
                double d = ThreadLocalRandom.current().nextDouble(0,1);
                if (d < 0.07) {
                    return giveBonusItem(player, BonusItem.DOUBLE_FABLED);
                } else if (d < 0.22) {
                    return giveBonusItem(player, BonusItem.LOOTED);
                } else if (d < 0.25) {
                    return giveBonusItem(player, BonusItem.MYTHIC);
                } else if (d < 0.45) {
                    return giveBonusItem(player, BonusItem.SPEED);
                } else if (d < 0.6) {
                    return giveBonusItem(player, BonusItem.JUMP);
                }
            }
            case 3 -> {
                double d = ThreadLocalRandom.current().nextDouble(0,1);
                if (d < 0.1) {
                    return giveBonusItem(player, BonusItem.DOUBLE_FABLED);
                } else if (d < 0.3) {
                    return giveBonusItem(player, BonusItem.LOOTED);
                } else if (d < 0.35) {
                    return giveBonusItem(player, BonusItem.MYTHIC);
                } else if (d < 0.55) {
                    return giveBonusItem(player, BonusItem.SPEED);
                } else if (d < 0.65) {
                    return giveBonusItem(player, BonusItem.JUMP);
                }
            }
            case 4 -> {
                double d = ThreadLocalRandom.current().nextDouble(0,1);
                if (d < 0.1) {
                    return giveBonusItem(player, BonusItem.DOUBLE_FABLED);
                } else if (d < 0.3) {
                    return giveBonusItem(player, BonusItem.LOOTED);
                } else if (d < 0.37) {
                    return giveBonusItem(player, BonusItem.MYTHIC);
                } else if (d < 0.62) {
                    return giveBonusItem(player, BonusItem.SPEED);
                } else if (d < 0.77) {
                    return giveBonusItem(player, BonusItem.JUMP);
                }
            }
        }
        return true;
    }


    private boolean giveBonusItem(Player player, BonusItem bonusItem) {
        switch (bonusItem) {
            case DOUBLE_FABLED -> {
                scoreManager.increaseScore(player, 1, "You found a double fabled!", false);
                player.playSound(player, Sound.ENCHANT_THORNS_HIT, 1,1);
                return true;
            }
            case LOOTED -> {
                player.sendMessage(ChatMessageFactory.singleLineInfo("Looks like this chest was sniped..."));
                player.playSound(player, Sound.ENTITY_WITCH_CELEBRATE, 1,1);
                return false;
            }
            case MYTHIC -> {
                scoreManager.increaseScore(player, Constants.SKY_MYTHIC, "You found a mythic!", true);
                Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.DARK_PURPLE + "] " + ChatColor.LIGHT_PURPLE + player.getName() + " found a " + ChatColor.DARK_PURPLE + "mythic!");
                player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1,1);
                player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 20, 0.3,0.3,0.3,0.1);
                return true;
            }
            case SPEED -> {
                player.sendMessage(ChatMessageFactory.singleLineInfo("You found a speed potion!"));
                Utilities.giveAurumItem(player, "m_sky_speed");
                player.playSound(player, Sound.ENTITY_WITCH_DRINK, 1,1);
                return true;
            }
            case JUMP -> {
                player.sendMessage(ChatMessageFactory.singleLineInfo("You found a jump height consumable!"));
                Utilities.giveAurumItem(player, "m_sky_jump");
                player.playSound(player, Sound.ENTITY_WITCH_DRINK, 1,1);
                return true;
            }
        }
        return true;
    }

    @Override
    public void end() {
        super.end();
    }

    @Override
    public void onDeath(Player player) {

    }

    @Override
    public void playerReconnect(Player player) {

    }

    @Override
    public Game getGame() {
        return Game.SKY_ISLAND_LOOTRUN;
    }

    private enum BonusItem {
        DOUBLE_FABLED,
        LOOTED,
        MYTHIC,
        SPEED,
        JUMP
    }
}
