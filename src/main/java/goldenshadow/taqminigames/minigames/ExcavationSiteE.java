package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.EntityHider;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.event.SoundtrackManager;
import goldenshadow.taqminigames.minigames.excavation.DartTrapData;
import goldenshadow.taqminigames.minigames.excavation.Shockwave;
import goldenshadow.taqminigames.util.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ExcavationSiteE extends Minigame {

    private final HashMap<UUID, List<UUID>> collectedCrystalMap = new HashMap<>();
    private final HashMap<UUID, List<UUID>> collectedEmeraldMap = new HashMap<>();
    private final HashMap<UUID, List<UUID>> collectedMisc = new HashMap<>();
    private final List<UUID> foundFeature = new ArrayList<>();
    private final List<UUID> completedPlayers = new ArrayList<>();

    private Shockwave currentShockwave = null;
    private final List<UUID> exitConfirmStage = new ArrayList<>();
    private boolean areFlamethrowersActive = false;
    private static final String[] deathMessages = {" fell to the ancient spirit's wrath!"," was unable to outrun the curse!"," was caught by the curse!", " couldn't escape their doom!", " skill issued!", " had a serious case of skill issue!", " got scared to death!", " overestimated themselves!", " didn't make it!", " got too greedy!", " needs to re-watch Indiana Jones!"};

    /**
     * Used to initialise the game
     */
    public ExcavationSiteE() {
        gameState = GameState.STARTING;
        SoundtrackManager.stopAllForAll();
        scoreManager = new ScoreManager("Emeralds", true);

        assert TAqMinigames.getEventConfig().getGenericData().WORLD != null;
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.FALL_DAMAGE, true), 5L);
        TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.FIRE_DAMAGE, true);
        TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.FREEZE_DAMAGE, true);
        TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.DROWNING_DAMAGE, true);



        timer = new Timer(0, 29, () -> timer = new Timer(44,59, this::end));
        for (Player player : ParticipantManager.getParticipants()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 500, 0, true, false, false));
            player.setBedSpawnLocation(TAqMinigames.getEventConfig().getExcavationData().START_LOCATION, true);
            player.setLevel(105);
            player.setExp(0);
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 10, false, false, false));

        }
        registerSpikes();
        registerFallingCeiling();

        ParticipantManager.teleportAllPlayers(TAqMinigames.getEventConfig().getExcavationData().TUTORIAL_LOCATION);
    }


    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Excavation Site E", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to explore the temple and find as many crystal shards as possible", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Dying will cause you to loose all your crystals and force you to start again", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("You can also find emeralds, but these can only be picked up once during the game.", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 14 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The temple is full of traps and other dangers so be careful! You can get health potions from excavators that roam the corridors.", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 18 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Once you have gathered as many crystals as you think you can, return the the entrance and you will be able to leave the temple", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 22 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("This will end the minigame for you so only do this toward the end! If you don't leave and the timer ends, you won't get any emeralds for the crystals you collected", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Each crystal will reward you with " + ((int) (TAqMinigames.getEventConfig().getExcavationData().CRYSTAL_POINTS * ScoreManager.getScoreMultiplier())) + " emeralds", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 25 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 5 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING,SoundCategory.VOICE, 1, 1);
                    player.teleport(TAqMinigames.getEventConfig().getExcavationData().START_LOCATION);
                }
                Utilities.fillAreaWithBlock(TAqMinigames.getEventConfig().getExcavationData().ENTRANCE_WALL[0], TAqMinigames.getEventConfig().getExcavationData().ENTRANCE_WALL[1], Material.BARRIER, Material.AIR);
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
                    Utilities.fillAreaWithBlock(TAqMinigames.getEventConfig().getExcavationData().ENTRANCE_WALL[0], TAqMinigames.getEventConfig().getExcavationData().ENTRANCE_WALL[1], Material.AIR, Material.BARRIER);
                    if (ParticipantManager.getParticipants().contains(player)) {
                        Utilities.giveAurumGroup(player, "m_exca_gear");
                    }
                }
                gameState = GameState.RUNNING;
            }
        }
        if (30 < tick && tick < 320) {

            if (tick % 3 == 0) {
                //Dart trap
                for (DartTrapData d : TAqMinigames.getEventConfig().getExcavationData().DART_TRAPS) {
                    assert d.location().getWorld() != null;
                    Arrow arrow = d.location().getWorld().spawnArrow(d.location(), d.vector(), 2f, 8);
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
                    arrow.setCritical(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), arrow::remove, 100L);
                }
                for (Location loc : TAqMinigames.getEventConfig().getExcavationData().GLITTER_LOCATIONS) {
                    assert loc.getWorld() != null;
                    loc.getWorld().spawnParticle(Particle.WAX_OFF, loc, 10, 0.3,0.3,0.3,0);
                }
            }
            if ((tick % 6 == 0)) {
                //Flamethrower loop
                for (Location[] l : TAqMinigames.getEventConfig().getExcavationData().FLAMETHROWER_BOXES) {
                    BoundingBox b = new BoundingBox(l[0].getX(), l[0].getY(), l[0].getZ(), l[1].getX(), l[1].getY(), l[1].getZ());
                    assert TAqMinigames.getEventConfig().getGenericData().WORLD != null;

                    TAqMinigames.getEventConfig().getGenericData().WORLD.spawnParticle(Particle.FLAME, b.getCenter().toLocation(TAqMinigames.getEventConfig().getGenericData().WORLD), (int) (10* b.getVolume()), b.getWidthX()/4 , b.getHeight()/4, b.getWidthZ()/4, 0);

                    Trigger trigger = new Trigger(b, TAqMinigames.getEventConfig().getGenericData().WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> p.setFireTicks(120), Utilities.secondsToMillis(0.5), false, false);
                    Trigger.register(trigger);

                    areFlamethrowersActive = true;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                        Trigger.unregister(trigger);
                        areFlamethrowersActive = false;
                    }, 30L);
                }
            }
            if (tick % 6 == 0) {
                if (TAqMinigames.getEventConfig().getExcavationData().SHOCKWAVE_LOCATION != null) {
                    //Shockwave trap
                    if (currentShockwave == null)
                        currentShockwave = new Shockwave(TAqMinigames.getEventConfig().getExcavationData().SHOCKWAVE_LOCATION);
                }
            }
            if (tick % 30 == 0) {
                for (Player p : ParticipantManager.getParticipants()) {
                    if (ThreadLocalRandom.current().nextInt(0,3) == 0) {
                        switch (ThreadLocalRandom.current().nextInt(0, 6)) {
                            case 0,1,2 -> p.playSound(p, Sound.AMBIENT_CAVE,SoundCategory.VOICE, 1,1);
                            case 3 -> p.playSound(p, Sound.ENTITY_WARDEN_SNIFF,SoundCategory.VOICE, 1,1);
                            case 4 -> p.playSound(p, Sound.ENTITY_WARDEN_TENDRIL_CLICKS,SoundCategory.VOICE, 1,1);
                            case 5 -> p.playSound(p, Sound.ENTITY_SPIDER_AMBIENT,SoundCategory.VOICE, 1,1);
                        }
                    }
                }
            }
        }
        if (tick > 320) {
            tick = 31;
        }
        super.tick();
    }

    @Override
    public void fastTick() {
        super.fastTick();
        if (gameState == GameState.RUNNING) {
            if (currentShockwave != null) {
                currentShockwave.tick();
                if (currentShockwave.isDone) currentShockwave = null;
            }
            if (fastTick % 4 == 0) {
                if (areFlamethrowersActive) {
                    for (Location[] l : TAqMinigames.getEventConfig().getExcavationData().FLAMETHROWER_BOXES) {
                        BoundingBox b = new BoundingBox(l[0].getX(), l[0].getY(), l[0].getZ(), l[1].getX(), l[1].getY(), l[1].getZ());
                        assert TAqMinigames.getEventConfig().getGenericData().WORLD != null;

                        TAqMinigames.getEventConfig().getGenericData().WORLD.spawnParticle(Particle.FLAME, b.getCenter().toLocation(TAqMinigames.getEventConfig().getGenericData().WORLD), (int) (10 * b.getVolume()), b.getWidthX() / 4, b.getHeight() / 4, b.getWidthZ() / 4, 0);
                    }
                }
            }
        }
    }

    @Override
    public void onDeath(Player player) {
        player.getInventory().clear();
        player.sendMessage(ChatMessageFactory.singleLineInfo("You died... All crystals have been lost!"));
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + deathMessages[ThreadLocalRandom.current().nextInt(0, deathMessages.length)]);
        Utilities.giveAurumGroup(player, "m_exca_gear");
        collectedCrystalMap.remove(player.getUniqueId());
        collectedMisc.remove(player.getUniqueId());
    }

    @Override
    public void playerReconnect(Player player) {
        if (gameState != GameState.RUNNING) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        player.setBedSpawnLocation(TAqMinigames.getEventConfig().getExcavationData().START_LOCATION, true);
        if (completedPlayers.contains(player.getUniqueId())) {
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @Override
    public void playerDisconnect(Player player) {

    }

    @Override
    public Game getGame() {
        return Game.EXCAVATION;
    }

    @Override
    public void end() {
        assert TAqMinigames.getEventConfig().getGenericData().WORLD != null;
        TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.FALL_DAMAGE, false);
        TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.FIRE_DAMAGE, false);
        TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.FREEZE_DAMAGE, false);
        TAqMinigames.getEventConfig().getGenericData().WORLD.setGameRule(GameRule.DROWNING_DAMAGE, false);

        Trigger.unregisterAll();
        Utilities.registerLobbyTrigger();


        ParticipantManager.teleportAllPlayers(TAqMinigames.getEventConfig().getExcavationData().END_LOCATION);
        ParticipantManager.getParticipants().forEach(x -> {
            x.setGameMode(GameMode.ADVENTURE);
            x.getInventory().clear();
            x.getActivePotionEffects().clear();
            x.setLevel(0);
            x.setExp(0);
        });
        super.end();
    }

    /**
     * Used for when a player interacts with an interaction entity
     * @param player The player
     * @param interaction The interaction entity
     */
    public void interact(Player player, Interaction interaction) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(ChatMessageFactory.adminErrorMessage("You can't interact with game objects while in creative mode!"));
            return;
        }
        if (!ParticipantManager.getParticipants().contains(player)) return;
        if (player.getLocation().distance(interaction.getLocation()) > 2) return;
        if (player.getGameMode() != GameMode.ADVENTURE) return;

        if (interaction.getScoreboardTags().contains("m_exca_crystal")) {
            List<UUID> list = new ArrayList<>();
            if (collectedCrystalMap.containsKey(player.getUniqueId())) {
                list = collectedCrystalMap.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedCrystalMap.put(player.getUniqueId(), list);
            Utilities.giveAurumItem(player, "m_exca_crystal");
            player.playSound(player, Sound.BLOCK_AMETHYST_CLUSTER_STEP,SoundCategory.VOICE, 5,1);
            if (!foundFeature.contains(interaction.getUniqueId())) {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().FIRST_FOUND_POINTS, "You were first to find this crystal!", true);
                foundFeature.add(interaction.getUniqueId());
            }
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_emeralds_small")) {
            List<UUID> list = new ArrayList<>();
            if (collectedEmeraldMap.containsKey(player.getUniqueId())) {
                list = collectedEmeraldMap.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedEmeraldMap.put(player.getUniqueId(), list);
            scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().EMERALDS_SMALL_POINTS, "You found a small amount of emeralds!", true);
            if (!foundFeature.contains(interaction.getUniqueId())) {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().FIRST_FOUND_POINTS, "You were first to find these emeralds!", true);
                foundFeature.add(interaction.getUniqueId());
            }
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_CHANGED, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_emeralds_medium")) {
            List<UUID> list = new ArrayList<>();
            if (collectedEmeraldMap.containsKey(player.getUniqueId())) {
                list = collectedEmeraldMap.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedEmeraldMap.put(player.getUniqueId(), list);
            scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().EMERALDS_MEDIUM_POINTS, "You found a sizable amount of emeralds!", true);
            if (!foundFeature.contains(interaction.getUniqueId())) {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().FIRST_FOUND_POINTS, "You were first to find these emeralds!", true);
                foundFeature.add(interaction.getUniqueId());
            }
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_CHANGED, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_emeralds_large")) {
            List<UUID> list = new ArrayList<>();
            if (collectedEmeraldMap.containsKey(player.getUniqueId())) {
                list = collectedEmeraldMap.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedEmeraldMap.put(player.getUniqueId(), list);
            scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().EMERALDS_LARGE_POINTS, "You found a large amount of emeralds!", true);
            if (!foundFeature.contains(interaction.getUniqueId())) {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().FIRST_FOUND_POINTS, "You were first to find these emeralds!", true);
                foundFeature.add(interaction.getUniqueId());
            }
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_CHANGED, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_sword")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            player.getInventory().remove(Material.IRON_SWORD);
            Utilities.giveAurumItem(player, TAqMinigames.getEventConfig().getExcavationData().BETTER_SWORD_NAME);
            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_NETHERITE,SoundCategory.VOICE, 1, 1);
            if (!foundFeature.contains(interaction.getUniqueId())) {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().FIRST_FOUND_POINTS, "You were first to find this sword!", true);
                foundFeature.add(interaction.getUniqueId());
            }
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_chestplate")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            Utilities.giveAurumItem(player, "m_exca_chestplate");
            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_NETHERITE,SoundCategory.VOICE, 1, 1);
            if (!foundFeature.contains(interaction.getUniqueId())) {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().FIRST_FOUND_POINTS, "You were first to find this chestplate!", true);
                foundFeature.add(interaction.getUniqueId());
            }
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_boots")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            Utilities.giveAurumItem(player, "m_exca_boots");
            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_NETHERITE,SoundCategory.VOICE, 1, 1);
            if (!foundFeature.contains(interaction.getUniqueId())) {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().FIRST_FOUND_POINTS, "You were first to find these boots!", true);
                foundFeature.add(interaction.getUniqueId());
            }
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_helmet")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            Utilities.giveAurumItem(player, "m_exca_helmet");
            player.playSound(player, Sound.ITEM_ARMOR_EQUIP_NETHERITE,SoundCategory.VOICE, 1, 1);
            if (!foundFeature.contains(interaction.getUniqueId())) {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().FIRST_FOUND_POINTS, "You were first to find this helmet!", true);
                foundFeature.add(interaction.getUniqueId());
            }
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_totem")) {

            if (player.getInventory().contains(Material.TOTEM_OF_UNDYING)) {
                return;

            }
            Utilities.giveAurumItem(player, "m_exca_totem");
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP,SoundCategory.VOICE, 1, 1);
            if (!foundFeature.contains(interaction.getUniqueId())) {
                scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().FIRST_FOUND_POINTS, "You were first to find this item!", true);
                foundFeature.add(interaction.getUniqueId());
            }
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_red_key")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            Utilities.giveAurumItem(player, "m_exca_red_key");
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP,SoundCategory.VOICE, 1, 1);
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_blue_key")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            Utilities.giveAurumItem(player, "m_exca_blue_key");
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP,SoundCategory.VOICE, 1, 1);
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_green_key")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            Utilities.giveAurumItem(player, "m_exca_green_key");
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP,SoundCategory.VOICE, 1, 1);
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_yellow_key")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            Utilities.giveAurumItem(player, "m_exca_yellow_key");
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP,SoundCategory.VOICE, 1, 1);
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_herb")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            Utilities.giveAurumItem(player, "m_exca_herb");
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP,SoundCategory.VOICE, 1, 1);
            hideEntitiesInBox(interaction.getBoundingBox(), EntityHider.HideType.UNTIL_DEATH, player);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_cauldron")) {

            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == Material.POTION) {

                        ItemMeta meta = i.getItemMeta();
                        assert meta != null;
                        if (meta.getDisplayName().contains("Speed Potion")) {
                            ChatMessageFactory.sendActionbarMessage(player, ChatColor.YELLOW + "You can only carry one speed potion!");
                            player.playSound(player, Sound.ENTITY_CAT_HISS,SoundCategory.VOICE, 1,1);
                            return;
                        }
                    }
                }
            }

            if (player.getInventory().contains(Material.LILY_OF_THE_VALLEY)) {

                List<UUID> list = collectedMisc.get(player.getUniqueId());
                list.remove(TAqMinigames.getEventConfig().getExcavationData().HERB_UUID);
                collectedMisc.put(player.getUniqueId(), list);

                assert TAqMinigames.getEventConfig().getExcavationData().HERB_UUID != null;
                Entity entity = Bukkit.getEntity(TAqMinigames.getEventConfig().getExcavationData().HERB_UUID);
                if (entity != null) {
                    for (Entity inBox : entity.getWorld().getNearbyEntities(entity.getBoundingBox())) {
                        if (inBox instanceof Display) {
                            EntityHider.revealEntity(player, inBox.getUniqueId());
                        }
                    }
                }

                Utilities.giveAurumItem(player, "m_exca_speed_potion");
                player.getInventory().remove(Material.LILY_OF_THE_VALLEY);
                player.playSound(player, Sound.BLOCK_BREWING_STAND_BREW,SoundCategory.VOICE, 1, 1);
            } else {
                player.sendMessage(ChatMessageFactory.singleLineInfo("You don't have the ingredient needed to use this..."));
                player.playSound(player, Sound.ENTITY_CAT_HISS,SoundCategory.VOICE, 1,1);
            }
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_shrine")) {
            List<UUID> list = new ArrayList<>();
            if (collectedMisc.containsKey(player.getUniqueId())) {
                list = collectedMisc.get(player.getUniqueId());
                if (list.contains(interaction.getUniqueId())) {
                    ChatMessageFactory.sendActionbarMessage(player, ChatColor.YELLOW + "You already received a blessing!");
                    player.playSound(player, Sound.ENTITY_CAT_HISS,SoundCategory.VOICE, 1,1);
                    return;
                }
            }
            list.add(interaction.getUniqueId());
            collectedMisc.put(player.getUniqueId(), list);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, PotionEffect.INFINITE_DURATION, 0, true, false, true));
            player.sendMessage(ChatMessageFactory.singleLineInfo("You have received a blessing..."));
            player.playSound(player, Sound.BLOCK_BEACON_POWER_SELECT,SoundCategory.VOICE, 1, 1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_desert_jump_potion")) {

            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == Material.POTION) {

                        ItemMeta meta = i.getItemMeta();
                        assert meta != null;
                        if (meta.getDisplayName().contains("Potion of Leaping")) {
                            player.sendMessage(" ");
                            player.sendMessage(ChatColor.YELLOW + "I already gave you a potion! Go use it first before I give you another...");
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, SoundCategory.VOICE, 1,1);
                            player.sendMessage(" ");
                            return;
                        }
                    }
                }
            }

            Utilities.giveAurumItem(player, "m_exca_jump_potion");
            player.sendMessage(" ");
            player.sendMessage(ChatColor.YELLOW + "I found this " + ChatColor.AQUA + ChatColor.BOLD + "jump boost potion " + ChatColor.RESET + ChatColor.YELLOW + "in one of the crates behind me. Maybe you can find a use for it!");
            player.sendMessage(" ");
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, SoundCategory.VOICE, 1,1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_door_red_in")) {
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == Material.STICK) {
                        ItemMeta meta = i.getItemMeta();
                        assert meta != null;
                        if (meta.getCustomModelData() == 100014) {

                            List<UUID> list = collectedMisc.get(player.getUniqueId());
                            list.remove(TAqMinigames.getEventConfig().getExcavationData().KEY_RED_UUID);
                            collectedMisc.put(player.getUniqueId(), list);

                            Entity entity = Bukkit.getEntity(TAqMinigames.getEventConfig().getExcavationData().KEY_RED_UUID);
                            if (entity != null) {
                                for (Entity inBox : entity.getWorld().getNearbyEntities(entity.getBoundingBox())) {
                                    if (inBox instanceof Display) {
                                        EntityHider.revealEntity(player, inBox.getUniqueId());
                                    }
                                    if (inBox instanceof ArmorStand a) {
                                        if (a.getScoreboardTags().contains("m_exca_item")) {
                                            EntityHider.revealEntity(player, a.getUniqueId());
                                        }
                                    }
                                }
                            }

                            player.teleport(TAqMinigames.getEventConfig().getExcavationData().DOOR_RED[0]);
                            player.playSound(player, Sound.BLOCK_IRON_DOOR_OPEN,SoundCategory.VOICE, 1,1);
                            player.getInventory().remove(i);
                            return;
                        }
                    }
                }
            }
            player.playSound(player, Sound.BLOCK_IRON_DOOR_CLOSE,SoundCategory.VOICE, 1,1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_door_blue_in")) {
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == Material.STICK) {
                        ItemMeta meta = i.getItemMeta();
                        assert meta != null;
                        if (meta.getCustomModelData() == 100011) {

                            List<UUID> list = collectedMisc.get(player.getUniqueId());
                            list.remove(TAqMinigames.getEventConfig().getExcavationData().KEY_BLUE_UUID);
                            collectedMisc.put(player.getUniqueId(), list);

                            Entity entity = Bukkit.getEntity(TAqMinigames.getEventConfig().getExcavationData().KEY_BLUE_UUID);
                            if (entity != null) {
                                for (Entity inBox : entity.getWorld().getNearbyEntities(entity.getBoundingBox())) {
                                    if (inBox instanceof Display) {
                                        EntityHider.revealEntity(player, inBox.getUniqueId());
                                    }
                                    if (inBox instanceof ArmorStand a) {
                                        if (a.getScoreboardTags().contains("m_exca_item")) {
                                            EntityHider.revealEntity(player, a.getUniqueId());
                                        }
                                    }
                                }
                            }

                            player.teleport(TAqMinigames.getEventConfig().getExcavationData().DOOR_BLUE[0]);
                            player.playSound(player, Sound.BLOCK_IRON_DOOR_OPEN,SoundCategory.VOICE, 1,1);
                            player.getInventory().remove(i);
                            return;
                        }
                    }
                }
            }
            player.playSound(player, Sound.BLOCK_IRON_DOOR_CLOSE,SoundCategory.VOICE, 1,1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_door_green_in")) {
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == Material.STICK) {
                        ItemMeta meta = i.getItemMeta();
                        assert meta != null;
                        if (meta.getCustomModelData() == 100012) {

                            List<UUID> list = collectedMisc.get(player.getUniqueId());
                            list.remove(TAqMinigames.getEventConfig().getExcavationData().KEY_GREEN_UUID);
                            collectedMisc.put(player.getUniqueId(), list);

                            Entity entity = Bukkit.getEntity(TAqMinigames.getEventConfig().getExcavationData().KEY_GREEN_UUID);
                            if (entity != null) {
                                for (Entity inBox : entity.getWorld().getNearbyEntities(entity.getBoundingBox())) {
                                    if (inBox instanceof Display) {
                                        EntityHider.revealEntity(player, inBox.getUniqueId());
                                    }
                                    if (inBox instanceof ArmorStand a) {
                                        if (a.getScoreboardTags().contains("m_exca_item")) {
                                            EntityHider.revealEntity(player, a.getUniqueId());
                                        }
                                    }
                                }
                            }

                            player.teleport(TAqMinigames.getEventConfig().getExcavationData().DOOR_GREEN[0]);
                            player.playSound(player, Sound.BLOCK_IRON_DOOR_OPEN,SoundCategory.VOICE, 1,1);
                            player.getInventory().remove(i);
                            return;
                        }
                    }
                }
            }
            player.playSound(player, Sound.BLOCK_IRON_DOOR_CLOSE,SoundCategory.VOICE, 1,1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_door_yellow_in")) {
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == Material.STICK) {
                        ItemMeta meta = i.getItemMeta();
                        assert meta != null;
                        if (meta.getCustomModelData() == 100013) {

                            List<UUID> list = collectedMisc.get(player.getUniqueId());
                            list.remove(TAqMinigames.getEventConfig().getExcavationData().KEY_YELLOW_UUID);
                            collectedMisc.put(player.getUniqueId(), list);

                            Entity entity = Bukkit.getEntity(TAqMinigames.getEventConfig().getExcavationData().KEY_YELLOW_UUID);
                            if (entity != null) {
                                for (Entity inBox : entity.getWorld().getNearbyEntities(entity.getBoundingBox())) {
                                    if (inBox instanceof Display) {
                                        EntityHider.revealEntity(player, inBox.getUniqueId());
                                    }
                                    if (inBox instanceof ArmorStand a) {
                                        if (a.getScoreboardTags().contains("m_exca_item")) {
                                            EntityHider.revealEntity(player, a.getUniqueId());
                                        }
                                    }
                                }
                            }

                            player.teleport(TAqMinigames.getEventConfig().getExcavationData().DOOR_YELLOW[0]);
                            player.playSound(player, Sound.BLOCK_IRON_DOOR_OPEN,SoundCategory.VOICE, 1,1);
                            player.getInventory().remove(i);
                            return;
                        }
                    }
                }
            }
            player.playSound(player, Sound.BLOCK_IRON_DOOR_CLOSE,SoundCategory.VOICE, 1,1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_door_red_out")) {
            player.teleport(TAqMinigames.getEventConfig().getExcavationData().DOOR_RED[1]);
            player.playSound(player, Sound.BLOCK_IRON_DOOR_OPEN,SoundCategory.VOICE, 1,1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_door_blue_out")) {
            player.teleport(TAqMinigames.getEventConfig().getExcavationData().DOOR_BLUE[1]);
            player.playSound(player, Sound.BLOCK_IRON_DOOR_OPEN,SoundCategory.VOICE, 1,1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_door_green_out")) {
            player.teleport(TAqMinigames.getEventConfig().getExcavationData().DOOR_GREEN[1]);
            player.playSound(player, Sound.BLOCK_IRON_DOOR_OPEN,SoundCategory.VOICE, 1,1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_door_yellow_out")) {
            player.teleport(TAqMinigames.getEventConfig().getExcavationData().DOOR_YELLOW[1]);
            player.playSound(player, Sound.BLOCK_IRON_DOOR_OPEN,SoundCategory.VOICE, 1,1);
            return;
        }
        if (interaction.getScoreboardTags().contains("m_exca_exit")) {
            if (timer.getMinutesLeft() > 30) {
                player.sendMessage(" ");
                player.sendMessage(ChatColor.YELLOW + "You haven't spent a lot of time exploring yet, " + ChatColor.AQUA + ChatColor.BOLD + "come back later " + ChatColor.YELLOW + "to talk to me again and then I'll let you exit");
                player.sendMessage(" ");
            } else {
                if (exitConfirmStage.contains(player.getUniqueId())) {
                    playerExit(player);
                } else {
                    ChatMessageFactory.sendInfoMessageBlock(player, ChatColor.YELLOW + "Are you sure you want to exit?", ChatColor.YELLOW + "This will end the game for you!", " ", ChatColor.GOLD + "Click again to confirm");
                    exitConfirmStage.add(player.getUniqueId());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> exitConfirmStage.remove(player.getUniqueId()), 200L);
                }
            }
        }
    }

    /**
     * Used for when a player breaks a breakable block
     * @param block The block
     */
    public void blockBroken(Block block) {
        if (block.getType() == Material.GRAVEL || block.getType() == Material.SUSPICIOUS_SAND) {
            Material type = block.getType();
            Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> block.setType(type), 300L);
        }
    }

    /**
     * Used for when a player exits the temple
     * @param player The player
     */
    private void playerExit(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        completedPlayers.add(player.getUniqueId());
        int count = 0;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i != null && i.getType() == Material.AMETHYST_SHARD) {
                count += i.getAmount();
            }
        }
        player.getInventory().clear();
        Bukkit.broadcastMessage(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + player.getName() + " has exited the temple with " + count + " crystals!");
        scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getExcavationData().CRYSTAL_POINTS * count, "You collected " + count + " crystals!", true);
    }

    /**
     * Used to remove the empty bottle from a players inventory after a potion has been drunk
     * @param player The player
     */
    public void potionUsed(Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.getInventory().remove(Material.GLASS_BOTTLE), 1L);
    }

    /**
     * Used to register all the spike traps
     */
    private void registerSpikes() {
        for (Location[] l : TAqMinigames.getEventConfig().getExcavationData().SPIKE_BOXES) {

            BoundingBox b = new BoundingBox(l[0].getX(), l[0].getY(), l[0].getZ(), l[1].getX(), l[1].getY(), l[1].getZ());
            Trigger.register(new Trigger(b, TAqMinigames.getEventConfig().getGenericData().WORLD, p -> p.getLocation().getBlock().getType() == Material.TRIPWIRE && p.isOnGround() && p.getGameMode() == GameMode.ADVENTURE, p -> {
                p.damage(4);
                assert TAqMinigames.getEventConfig().getGenericData().WORLD != null;
                for (Entity e : TAqMinigames.getEventConfig().getGenericData().WORLD.getNearbyEntities(b)) {
                    if (e instanceof ItemDisplay itemDisplay) {
                        if (itemDisplay.getScoreboardTags().contains("m_exca_spike")) {
                            itemDisplay.getWorld().playSound(itemDisplay.getLocation(), Sound.BLOCK_PISTON_EXTEND, SoundCategory.VOICE, 1, 1);
                            ItemStack item = itemDisplay.getItemStack();
                            if (item != null) {
                                assert item.getItemMeta() != null;
                                ItemMeta meta = item.getItemMeta();
                                meta.setCustomModelData(100042);
                                item.setItemMeta(meta);
                                itemDisplay.setItemStack(item);
                            }
                            Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                                itemDisplay.getWorld().playSound(itemDisplay.getLocation(), Sound.BLOCK_PISTON_CONTRACT, SoundCategory.VOICE, 1, 1);
                                    assert item != null;
                                    assert item.getItemMeta() != null;
                                    ItemMeta meta = item.getItemMeta();
                                    meta.setCustomModelData(100091);
                                    item.setItemMeta(meta);
                                    itemDisplay.setItemStack(item);
                            }, 15L);
                        }
                    }
                }
            }, Utilities.secondsToMillis(1), true, false));
        }
    }

    private void registerFallingCeiling() {
        if (TAqMinigames.getEventConfig().getExcavationData().FALLING_CEILING_BOX != null) {
            Location[] l = TAqMinigames.getEventConfig().getExcavationData().FALLING_CEILING_BOX;
            BoundingBox b = new BoundingBox(l[0].getX(), l[0].getY(), l[0].getZ(), l[1].getX(), l[1].getY(), l[1].getZ());
            Trigger.register(new Trigger(b, TAqMinigames.getEventConfig().getGenericData().WORLD, p -> {
                if (p.getGameMode() == GameMode.ADVENTURE) {
                    if (p.getEquipment() != null && p.getEquipment().getHelmet() != null) {
                        return p.getEquipment().getHelmet().getType() != Material.LEATHER_HELMET;
                    }
                    return true;
                }
                return false;
            }, p -> {
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 10, false, false, false));
                Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                    p.damage(40);
                    p.playSound(p, Sound.BLOCK_ANVIL_PLACE, SoundCategory.VOICE, 1,1);
                }, 20L);
            }, Utilities.secondsToMillis(5), false, false));
        }
    }

    /**
     * Used to hide display entities once the thing they are representing has been collected
     * @param box The interactions bounding box
     * @param hideType The hide type
     * @param player The player
     */
    private void hideEntitiesInBox(BoundingBox box, EntityHider.HideType hideType, Player player) {
        World world = TAqMinigames.getEventConfig().getGenericData().WORLD;
        assert world != null;
        for (Entity entity : world.getNearbyEntities(box)) {
            if (entity instanceof Display) {
                EntityHider.hideEntity(player, entity, hideType);
            }
            if (entity instanceof ArmorStand armorStand) {
                if (armorStand.getEquipment() != null) {
                    if (armorStand.getScoreboardTags().contains("m_exca_item")) {
                        EntityHider.hideEntity(player, entity, hideType);
                    }
                }
            }
        }
    }
}
