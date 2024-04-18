package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.event.SoundtrackManager;
import goldenshadow.taqminigames.minigames.sky_island.CheckpointData;
import goldenshadow.taqminigames.util.*;
import org.bukkit.*;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

// ✫ <- loot chest star
// &7Loot Chest [&f✫&8✫✫✫&7]
// &eLoot Chest [&6✫✫&8✫✫&e]
// &5Loot Chest [&d✫✫✫&8✫&5]
// &3Loot Chest [&b✫✫✫✫&3]

public class SkyIslandLootrun extends Minigame{

    private final HashMap<UUID, List<UUID>> openedChests = new HashMap<>();
    private final HashMap<UUID, List<UUID>> queuedOpenedChests = new HashMap<>();
    private final HashMap<UUID, Integer> queuedPoints = new HashMap<>();
    private final HashMap<UUID, List<UUID>> nonBonusItemChests = new HashMap<>();

    private final Team team;

    /**
     * Used to initialise the game
     */
    public SkyIslandLootrun() {
        SoundtrackManager.stopAllForAll();
        gameState = GameState.STARTING;
        scoreManager = new ScoreManager("Emeralds", true);

        assert Bukkit.getScoreboardManager() != null;
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("m_sky") == null) {
            team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("m_sky");
        } else team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("m_sky");
        assert team != null;
        team.setCanSeeFriendlyInvisibles(true);

        timer = new Timer(0, 29, () -> timer = new Timer(19,59, this::end));
        for (Player player : ParticipantManager.getParticipants()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 0, true, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 10, false, false, false));
            player.setLevel(105);
        }

        ParticipantManager.teleportAllPlayers(TAqMinigames.getEventConfig().getSkyIslandData().TUTORIAL_LOCATION);
    }

    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Sky Island Lootrun", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to explore the sky islands and open as many chests as possible", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The path will sometimes split into paths of different difficulty. Harder paths will have more chests, but beware, when we say hard, we mean hard!", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("If you fall, you can use your teleport scroll to return to a checkpoint. All large islands have checkpoints on them", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 14 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("When opening a chest, there is also a chance you will find a bonus item in it", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 18 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("These bonus items can be anything from a speed potion to a mythic", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 22 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Completing a the entire lootrun will give you " + ((int) (TAqMinigames.getEventConfig().getSkyIslandData().COMPLETE_POINTS * ScoreManager.getScoreMultiplier())) + " emeralds", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Opening a chest will give you " + ((int) (TAqMinigames.getEventConfig().getSkyIslandData().T1_POINTS * ScoreManager.getScoreMultiplier())) + ", " + ((int) (TAqMinigames.getEventConfig().getSkyIslandData().T2_POINTS * ScoreManager.getScoreMultiplier())) + ", " + ((int) (TAqMinigames.getEventConfig().getSkyIslandData().T3_POINTS * ScoreManager.getScoreMultiplier())) + " or " + ((int) (TAqMinigames.getEventConfig().getSkyIslandData().T4_POINTS * ScoreManager.getScoreMultiplier())) + " emeralds depending on the tier of the chest", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 25 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 5 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING,SoundCategory.VOICE, 1, 1);
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
                    player.setBedSpawnLocation(TAqMinigames.getEventConfig().getSkyIslandData().START_LOCATION, true);
                    Utilities.giveAurumItem(player, "m_sky_scroll");
                    team.addEntry(player.getName());
                }
                ParticipantManager.teleportAllPlayers(TAqMinigames.getEventConfig().getSkyIslandData().START_LOCATION);
                registerTriggers();
                gameState = GameState.RUNNING;
            }
        }
        if (tick > 30 && gameState == GameState.RUNNING) {
            for (Player p : ParticipantManager.getParticipants()) {
                if (p.getGameMode() == GameMode.ADVENTURE) {
                    ChatMessageFactory.sendActionbarMessage(p, ChatColor.DARK_GREEN + String.valueOf(ChatColor.BOLD) + "Emerald Pouch: " + ChatColor.GREEN + queuedPoints.getOrDefault(p.getUniqueId(), 0));
                }
            }
        }
        super.tick();
    }

    /**
     * Used to register all the triggers
     */
    private void registerTriggers() {

        for (CheckpointData checkpointData : TAqMinigames.getEventConfig().getSkyIslandData().CHECKPOINTS) {
            Trigger.register(new Trigger(checkpointData.getBoundingBox(), TAqMinigames.getEventConfig().getGenericData().WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                p.setBedSpawnLocation(checkpointData.respawnPoint(), true);
                sendCheckpointTitle(p);
                convertQueuedChests(p);
            }, Utilities.secondsToMillis(10000), false, false));
        }
        for (int i = 0; i < TAqMinigames.getEventConfig().getSkyIslandData().BOOST_PADS.length; i++) {
            Location loc = TAqMinigames.getEventConfig().getSkyIslandData().BOOST_PADS[i];
            BoundingBox box = new BoundingBox(loc.getX()+1, loc.getY()+1, loc.getZ()+1, loc.getX()-1, loc.getY(), loc.getZ()-1);
            if (i == 0) {
                Trigger.register(new Trigger(box, TAqMinigames.getEventConfig().getGenericData().WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                    p.setVelocity(new Vector(30, 1, -1.5));
                    p.playSound(p, Sound.ITEM_TRIDENT_HIT_GROUND,SoundCategory.VOICE, 1,1);
                }, Utilities.secondsToMillis(2), false, false));
            } else if (i == 1) {
                Trigger.register(new Trigger(box, TAqMinigames.getEventConfig().getGenericData().WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                    p.setVelocity(new Vector(2, 1, -3));
                    p.playSound(p, Sound.ITEM_TRIDENT_HIT_GROUND,SoundCategory.VOICE, 1,1);
                }, Utilities.secondsToMillis(2), false, false));
            } else if (i == 2) {
                Trigger.register(new Trigger(box, TAqMinigames.getEventConfig().getGenericData().WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                    p.setVelocity(new Vector(-2, 1, -20));
                    p.playSound(p, Sound.ITEM_TRIDENT_HIT_GROUND,SoundCategory.VOICE, 1,1);
                }, Utilities.secondsToMillis(2), false, false));
            } else {
                Trigger.register(new Trigger(box, TAqMinigames.getEventConfig().getGenericData().WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                    p.setVelocity(new Vector(0, 3, 0));
                    p.playSound(p, Sound.ITEM_TRIDENT_HIT_GROUND,SoundCategory.VOICE, 1,1);
                }, Utilities.secondsToMillis(2), false, false));
            }
        }
    }

    /**
     * Used to handle an item being used
     * @param player The player
     * @param material The material of the item used
     */
    public void itemUsed(Player player, Material material) {
        if (material == Material.PAPER && !player.hasCooldown(Material.PAPER)) {
            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT,SoundCategory.VOICE, 1,1);
            onDeath(player);
            Location location = player.getBedSpawnLocation() != null ? player.getBedSpawnLocation() : TAqMinigames.getEventConfig().getSkyIslandData().START_LOCATION;
            player.teleport(location);
            player.setCooldown(Material.PAPER, 20);
        }
    }

    /**
     * Used to add all emeralds saved in the "emerald pouch" to the actual scoreboard
     * @param player The player
     */
    private void convertQueuedChests(Player player) {
        int i = queuedPoints.getOrDefault(player.getUniqueId(), 0);
        List<UUID> list = queuedOpenedChests.get(player.getUniqueId());
        if (list == null) list = new ArrayList<>();
        queuedPoints.put(player.getUniqueId(), 0);
        queuedOpenedChests.put(player.getUniqueId(), new ArrayList<>());
        scoreManager.increaseScore(player, i, "Your emerald pouch has been saved to your bank!", true);
        List<UUID> opened = openedChests.getOrDefault(player.getUniqueId(), new ArrayList<>());
        opened.addAll(list);
        openedChests.put(player.getUniqueId(), opened);
    }

    /**
     * Used to check if a player can open a chest
     * @param player The player
     * @param interaction The interaction entity
     * @return True if they can, false otherwise
     */
    private boolean isOpenable(Player player, Interaction interaction) {
        if (openedChests.getOrDefault(player.getUniqueId(), List.of()).contains(interaction.getUniqueId())) return false;
        return !queuedOpenedChests.getOrDefault(player.getUniqueId(), List.of()).contains(interaction.getUniqueId());
    }

    /**
     * Used for when a player opens a chest
     * @param player The player
     * @param interaction The interaction entity
     */
    public void chestOpened(Player player, Interaction interaction) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(ChatMessageFactory.adminErrorMessage("You can't interact with game objects while in creative mode!"));
            return;
        }
        if (!ParticipantManager.getParticipants().contains(player)) return;
        if (player.getLocation().distance(interaction.getLocation()) > 4) return;
        if (player.getGameMode() != GameMode.ADVENTURE) return;

        if (interaction.getScoreboardTags().contains("m_sky_chest_t1") || interaction.getScoreboardTags().contains("m_sky_chest_t2") || interaction.getScoreboardTags().contains("m_sky_chest_t3") || interaction.getScoreboardTags().contains("m_sky_chest_t4")) {

            if (!isOpenable(player, interaction)) {
                player.sendMessage(ChatMessageFactory.singleLineInfo("You already opened this chest!"));
                player.playSound(player, Sound.ENTITY_CAT_HISS,SoundCategory.VOICE, 1,1);
                return;
            }

            if (interaction.getScoreboardTags().contains("m_sky_instant")) {
                List<UUID> list = openedChests.get(player.getUniqueId());
                if (list == null) list = new ArrayList<>();

                list.add(interaction.getUniqueId());
                openedChests.put(player.getUniqueId(), list);
            } else {
                List<UUID> list = queuedOpenedChests.get(player.getUniqueId());
                if (list == null) list = new ArrayList<>();

                list.add(interaction.getUniqueId());
                queuedOpenedChests.put(player.getUniqueId(), list);
            }

            player.playSound(player, Sound.BLOCK_CHEST_OPEN,SoundCategory.VOICE, 1,1);

            if (interaction.getScoreboardTags().contains("m_sky_chest_t1")) {

                rollBonusItem(player, 1, interaction.getUniqueId());
                if (interaction.getScoreboardTags().contains("m_sky_instant")) {
                    scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getSkyIslandData().T1_POINTS, "You opened a tier 1 chest!", true);
                } else {
                    player.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "+" + (int) (TAqMinigames.getEventConfig().getSkyIslandData().T1_POINTS * ScoreManager.getScoreMultiplier()) + ChatColor.DARK_AQUA + " Emeralds to pouch] " + ChatColor.AQUA + "You opened a tier 1 chest!");
                    queuedPoints.put(player.getUniqueId(), queuedPoints.getOrDefault(player.getUniqueId(), 0) + (int) (TAqMinigames.getEventConfig().getSkyIslandData().T1_POINTS * ScoreManager.getScoreMultiplier()));
                }

            } else if (interaction.getScoreboardTags().contains("m_sky_chest_t2")) {

                rollBonusItem(player, 2, interaction.getUniqueId());
                if (interaction.getScoreboardTags().contains("m_sky_instant")) {
                    scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getSkyIslandData().T2_POINTS, "You opened a tier 2 chest!", true);
                } else {
                    player.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "+" + (int) (TAqMinigames.getEventConfig().getSkyIslandData().T2_POINTS * ScoreManager.getScoreMultiplier()) + ChatColor.DARK_AQUA + " Emeralds to pouch] " + ChatColor.AQUA + "You opened a tier 2 chest!");
                    queuedPoints.put(player.getUniqueId(), queuedPoints.getOrDefault(player.getUniqueId(), 0) + (int) (TAqMinigames.getEventConfig().getSkyIslandData().T2_POINTS * ScoreManager.getScoreMultiplier()));
                }

            } else if (interaction.getScoreboardTags().contains("m_sky_chest_t3")) {

                rollBonusItem(player, 3, interaction.getUniqueId());
                if (interaction.getScoreboardTags().contains("m_sky_instant")) {
                    scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getSkyIslandData().T3_POINTS, "You opened a tier 3 chest!", true);
                } else {
                    player.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "+" + (int) (TAqMinigames.getEventConfig().getSkyIslandData().T3_POINTS * ScoreManager.getScoreMultiplier()) + ChatColor.DARK_AQUA + " Emeralds to pouch] " + ChatColor.AQUA + "You opened a tier 3 chest!");
                    queuedPoints.put(player.getUniqueId(), queuedPoints.getOrDefault(player.getUniqueId(), 0) + (int) (TAqMinigames.getEventConfig().getSkyIslandData().T3_POINTS * ScoreManager.getScoreMultiplier()));
                }

            } else if (interaction.getScoreboardTags().contains("m_sky_chest_t4")) {

                rollBonusItem(player, 4, interaction.getUniqueId());
                if (interaction.getScoreboardTags().contains("m_sky_instant")) {
                    scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getSkyIslandData().T4_POINTS, "You opened a tier 4 chest!", true);
                } else {
                    player.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "+" + (int) (TAqMinigames.getEventConfig().getSkyIslandData().T4_POINTS * ScoreManager.getScoreMultiplier()) + ChatColor.DARK_AQUA + " Emeralds to pouch] " + ChatColor.AQUA + "You opened a tier 4 chest!");
                    queuedPoints.put(player.getUniqueId(), queuedPoints.getOrDefault(player.getUniqueId(), 0) + (int) (TAqMinigames.getEventConfig().getSkyIslandData().T4_POINTS * ScoreManager.getScoreMultiplier()));
                }

            }
        }
        if (interaction.getScoreboardTags().contains("m_sky_end")) {
            scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getSkyIslandData().COMPLETE_POINTS, "You finished the lootrun!", true);
            Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo(player.getName() + " has completed the lootrun!"));
            player.getActivePotionEffects().clear();
            player.getInventory().clear();
            Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.setGameMode(GameMode.SPECTATOR), 1L);

        }
    }

    /**
     * Used to determine what bonus item the player should get
     * @param player The player
     * @param chestTier The level of the chest that was opened
     */
    private void rollBonusItem(Player player, int chestTier, UUID uuid) {
        List<UUID> list = nonBonusItemChests.getOrDefault(player.getUniqueId(), new ArrayList<>());
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
            list.add(uuid);
            nonBonusItemChests.put(player.getUniqueId(), list);
        }, 1L);

        switch (chestTier) {
            case 1 -> {
                double d = ThreadLocalRandom.current().nextDouble(0,1);
                if (d < 0.05) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.DOUBLE_FABLED);
                } else if (d < 0.2) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.INGREDIENT);
                } else if (d < 0.22) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.MYTHIC);
                } else if (d < 0.37) {
                    giveBonusItem(player, BonusItem.SPEED);
                } else if (d < 0.47) {
                    giveBonusItem(player, BonusItem.JUMP);
                }
            }
            case 2 -> {
                double d = ThreadLocalRandom.current().nextDouble(0,1);
                if (d < 0.07) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.DOUBLE_FABLED);
                } else if (d < 0.22) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.INGREDIENT);
                } else if (d < 0.25) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.MYTHIC);
                } else if (d < 0.45) {
                    giveBonusItem(player, BonusItem.SPEED);
                } else if (d < 0.6) {
                    giveBonusItem(player, BonusItem.JUMP);
                }
            }
            case 3 -> {
                double d = ThreadLocalRandom.current().nextDouble(0,1);
                if (d < 0.1) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.DOUBLE_FABLED);
                } else if (d < 0.3) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.INGREDIENT);
                } else if (d < 0.35) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.MYTHIC);
                } else if (d < 0.55) {
                    giveBonusItem(player, BonusItem.SPEED);
                } else if (d < 0.65) {
                    giveBonusItem(player, BonusItem.JUMP);
                }
            }
            case 4 -> {
                double d = ThreadLocalRandom.current().nextDouble(0,1);
                if (d < 0.1) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.DOUBLE_FABLED);
                } else if (d < 0.3) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.INGREDIENT);
                } else if (d < 0.37) {
                    if (list.contains(uuid)) {
                        giveBonusItem(player, BonusItem.SPEED);
                        return;
                    }
                    giveBonusItem(player, BonusItem.MYTHIC);
                } else if (d < 0.62) {
                    giveBonusItem(player, BonusItem.SPEED);
                } else if (d < 0.77) {
                    giveBonusItem(player, BonusItem.JUMP);
                }
            }
        }
    }

    /**
     * Used to give a player a bonus item from a chest
     * @param player The player
     * @param bonusItem The bonus item that should be received
     */
    private void giveBonusItem(Player player, BonusItem bonusItem) {
        switch (bonusItem) {
            case DOUBLE_FABLED -> {
                scoreManager.increaseScore(player, 1, "You found a double fabled!", false);
                player.playSound(player, Sound.ENCHANT_THORNS_HIT,SoundCategory.VOICE, 1,1);
            }
            case INGREDIENT -> {
                player.playSound(player, Sound.ENTITY_VILLAGER_CELEBRATE,SoundCategory.VOICE, 1,1);
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getSkyIslandData().INGREDIENTS_POINTS, "You found an expensive ingredient!", true);
            }
            case MYTHIC -> {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getSkyIslandData().MYTHIC_POINTS, "You found a mythic!", true);
                Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "!" + ChatColor.DARK_PURPLE + "] " + ChatColor.LIGHT_PURPLE + player.getName() + " found a " + ChatColor.DARK_PURPLE + "mythic!");
                player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE,SoundCategory.VOICE, 1,1);
                player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 20, 0.3,0.3,0.3,0.1);
                ItemStack old = player.getInventory().getItemInMainHand();
                ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                ItemMeta meta = totem.getItemMeta();
                assert meta != null;
                meta.setCustomModelData(2);
                totem.setItemMeta(meta);
                player.getInventory().setItemInMainHand(totem);
                player.damage(30);
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.removePotionEffect(PotionEffectType.ABSORPTION);
                player.setHealth(20);
                player.getInventory().setItemInMainHand(old);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.stopSound(Sound.ITEM_TOTEM_USE);
                }
            }
            case SPEED -> {
                player.sendMessage(ChatMessageFactory.singleLineInfo("You found a speed potion!"));
                Utilities.giveAurumItem(player, "m_sky_speed");
                player.playSound(player, Sound.ENTITY_WITCH_DRINK,SoundCategory.VOICE, 1,1);
            }
            case JUMP -> {
                player.sendMessage(ChatMessageFactory.singleLineInfo("You found a jump height consumable!"));
                Utilities.giveAurumItem(player, "m_sky_jump");
                player.playSound(player, Sound.ENTITY_WITCH_DRINK,SoundCategory.VOICE, 1,1);
            }
        }
    }

    @Override
    public void end() {
        Trigger.unregisterAll();
        Utilities.registerLobbyTrigger();
        for (Player p : ParticipantManager.getParticipants()) {
            team.removeEntry(p.getName());
            p.getActivePotionEffects().clear();
            p.getInventory().clear();
            p.setLevel(0);
            Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> p.setGameMode(GameMode.SPECTATOR), 2L);
        }
        super.end();
    }

    @Override
    public void onDeath(Player player) {
        player.sendMessage(ChatMessageFactory.singleLineInfo("You have been teleported to your last checkpoint! All emeralds in your pouch have been lost..."));
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, PotionEffect.INFINITE_DURATION, 0, false, false, false)), 5L);
        queuedPoints.put(player.getUniqueId(), 0);
        queuedOpenedChests.put(player.getUniqueId(), new ArrayList<>());
    }

    @Override
    public void playerReconnect(Player player) {
        if (gameState != GameState.RUNNING) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            team.addEntry(player.getName());
        }
    }

    @Override
    public void playerDisconnect(Player player) {
        team.removeEntry(player.getName());
    }

    @Override
    public Game getGame() {
        return Game.SKY_ISLAND_LOOTRUN;
    }

    /**
     * Bonus item enum
     */
    private enum BonusItem {
        DOUBLE_FABLED,
        MYTHIC,
        SPEED,
        JUMP,
        INGREDIENT
    }

    /**
     * Used to send the animated checkpoint title
     * @param player The player
     */
    private void sendCheckpointTitle(Player player) {
        for (int i = 1; i < 20; i++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.playSound(player, Sound.ITEM_FLINTANDSTEEL_USE,SoundCategory.VOICE, 1,1), i);
        }
        player.sendTitle( ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "KP", "", 0, 20, 0);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.sendTitle(ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "CKPO", "", 0, 20, 0), 3L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.sendTitle(ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "ECKPOI", "", 0, 20, 0), 6L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.sendTitle(ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "HECKPOIN", "", 0, 20, 0), 9L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.sendTitle(ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "CHECKPOINT", "", 0, 20, 0), 12L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.sendTitle(ChatColor.WHITE + String.valueOf(ChatColor.BOLD) + "CHECKPOINT", "", 0, 20, 0), 15L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.sendTitle(ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "CHECKPOINT", "", 0, 20, 0), 18L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.sendTitle(ChatColor.WHITE + String.valueOf(ChatColor.BOLD) + "CHECKPOINT", "", 0, 20, 0), 21L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.sendTitle(ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "CHECKPOINT", "", 0, 20, 10), 24L);
    }

}
