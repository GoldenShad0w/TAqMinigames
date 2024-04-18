package goldenshadow.taqminigames.minigames;

import com.google.errorprone.annotations.DoNotCall;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.event.SoundtrackManager;
import goldenshadow.taqminigames.util.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AledarCartRacing extends Minigame {


    private final List<UUID> finishedPlayers = new ArrayList<>();
    private final HashMap<UUID, Integer> antiCheatState = new HashMap<>();
    private final HashMap<UUID, Integer> currentLap = new HashMap<>();
    private final List<UUID> gameObjectUUIDs = new ArrayList<>();
    private final List<Location> crystalLocations = new ArrayList<>();

    /**
     * Used to initiate the game
     */
    public AledarCartRacing() {
        gameState = GameState.STARTING;
        scoreManager = new ScoreManager("Emeralds", true);
        SoundtrackManager.stopAllForAll();

        timer = new Timer(0, 29, () -> timer = new Timer(9,59, this::end));
        for (Player player : ParticipantManager.getParticipants()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 500, 0, true, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 10, false, false, false));
        }

        Trigger.register(new Trigger(new BoundingBox(TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_1[0].getX(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_1[0].getY(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_1[0].getZ(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_1[1].getX(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_1[1].getY(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_1[1].getZ()), TAqMinigames.getEventConfig().getGenericData().WORLD, p -> {
            if (p.getGameMode() != GameMode.ADVENTURE) return false;
            return antiCheatState.getOrDefault(p.getUniqueId(), 0) == 0;
        }, p -> antiCheatState.put(p.getUniqueId(), 1), 0, false, false));

        Trigger.register(new Trigger(new BoundingBox(TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_2[0].getX(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_2[0].getY(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_2[0].getZ(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_2[1].getX(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_2[1].getY(), TAqMinigames.getEventConfig().getAledarCartData().ANTI_CHEAT_LINE_2[1].getZ()), TAqMinigames.getEventConfig().getGenericData().WORLD, p -> {
            if (p.getGameMode() != GameMode.ADVENTURE) return false;
            return antiCheatState.getOrDefault(p.getUniqueId(), 0) == 1;
        }, p -> antiCheatState.put(p.getUniqueId(), 2), 0, false, false));

        Trigger.register(new Trigger(new BoundingBox(TAqMinigames.getEventConfig().getAledarCartData().LAP_LINE[0].getX(), TAqMinigames.getEventConfig().getAledarCartData().LAP_LINE[0].getY(), TAqMinigames.getEventConfig().getAledarCartData().LAP_LINE[0].getZ(), TAqMinigames.getEventConfig().getAledarCartData().LAP_LINE[1].getX(), TAqMinigames.getEventConfig().getAledarCartData().LAP_LINE[1].getY(), TAqMinigames.getEventConfig().getAledarCartData().LAP_LINE[1].getZ()), TAqMinigames.getEventConfig().getGenericData().WORLD, p -> {
            if (p.getGameMode() != GameMode.ADVENTURE) return false;
            return antiCheatState.getOrDefault(p.getUniqueId(), 0) == 2;
        }, p -> {
            antiCheatState.put(p.getUniqueId(), 0);
            lapCompleted(p);
        }, 0, false, false));

        ParticipantManager.teleportAllPlayers(TAqMinigames.getEventConfig().getAledarCartData().TUTORIAL_LOCATION);
    }

    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Aledar Cart Racing", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to escort Aledar through the silent expanse", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("You must complete 5 laps to finish the track", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Along the way, you can collect powerups. These are the powerups that exist:", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 14 -> ChatMessageFactory.sendInfoBlockToAll(ChatColor.YELLOW + "Emerald Crate", " ", ChatColor.YELLOW + "Gain " + ((int) (TAqMinigames.getEventConfig().getAledarCartData().CRATE_POINTS * ScoreManager.getScoreMultiplier())) + " emeralds!");
            case 16 -> ChatMessageFactory.sendInfoBlockToAll(ChatColor.YELLOW + "Toxic Slime", " ", ChatColor.YELLOW + "Place a slime behind you that slows down", ChatColor.YELLOW + "players who hit it!");
            case 18 -> ChatMessageFactory.sendInfoBlockToAll(ChatColor.YELLOW + "Dernic Dash", " ", ChatColor.YELLOW + "50% chance to boosts the speed of your cart for", ChatColor.YELLOW + "a short while!");
            case 20 -> ChatMessageFactory.sendInfoBlockToAll(ChatColor.YELLOW + "Eldritch Blast", " ", ChatColor.YELLOW + "Slow down all players near you in a single", ChatColor.YELLOW + "large explosion!");
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Each lap will reward you with " + ((int) (TAqMinigames.getEventConfig().getAledarCartData().LAP_POINTS * ScoreManager.getScoreMultiplier())) + " emeralds, and you also get emeralds for completing the entire track!", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 25 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 5 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING,SoundCategory.VOICE, 1, 1);
                    player.teleport(TAqMinigames.getEventConfig().getAledarCartData().START_LOCATION);
                }
                for (Player player : ParticipantManager.getParticipants()) {
                    Boat b = buildCart(TAqMinigames.getEventConfig().getAledarCartData().START_LOCATION);
                    b.addPassenger(player);
                }
                Utilities.fillAreaWithBlock(TAqMinigames.getEventConfig().getAledarCartData().START_BARRIERS[0], TAqMinigames.getEventConfig().getAledarCartData().START_BARRIERS[1], Material.BARRIER, Material.AIR);
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
                    Utilities.fillAreaWithBlock(TAqMinigames.getEventConfig().getAledarCartData().START_BARRIERS[0], TAqMinigames.getEventConfig().getAledarCartData().START_BARRIERS[1], Material.AIR, Material.BARRIER);
                }
                gameState = GameState.RUNNING;
                spawnPowerups();
            }
        }
        if (tick > 30) {
            for (Player p : ParticipantManager.getParticipants()) {
                ChatMessageFactory.sendActionbarMessage(p, net.md_5.bungee.api.ChatColor.YELLOW + "Lap [" + (currentLap.getOrDefault(p.getUniqueId(), 0)) + "/5]");
            }
        }

        if (tick > 50) {
            if (gameState == GameState.RUNNING) {
                spawnPowerups();
            }
            tick = 31;
        }
        super.tick();

    }

    @Override
    @DoNotCall
    public void onDeath(Player player) {
    }

    /**
     * Used to correctly handle a player reconnecting
     * @param player The player who reconnected
     */
    @Override
    public void playerReconnect(Player player) {
        if (gameState != GameState.RUNNING) {
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }
        if (finishedPlayers.contains(player.getUniqueId())) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            //one block above location so that the boat doesn't get stuck in the ice
            Boat b = buildCart(player.getLocation().add(0, 1,0));
            b.addPassenger(player);
        }
    }

    /**
     * Used to correctly handle to player disconnecting
     * @param player The player
     */
    @Override
    public void playerDisconnect(Player player) {
        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            for (Entity e : vehicle.getPassengers()) {
                if (!(e instanceof Player)) {
                    e.remove();
                }
            }
            vehicle.removePassenger(player);
            vehicle.remove();
        }
    }

    /**
     * Used for when a player completes a lap
     * @param player The lap
     */
    private void lapCompleted(Player player) {
        int lap = currentLap.getOrDefault(player.getUniqueId(), 0);

        player.sendTitle("", ChatColor.YELLOW + "[" + (lap+1) + "/5] Laps Completed", 5,40,5);
        Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo(player.getName() + " has completed lap " + (lap + 1) + "!"));
        scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getAledarCartData().LAP_POINTS, "You completed a lap!", true);
        if (lap == 4) {
            trackCompleted(player);
        } else {
            currentLap.put(player.getUniqueId(), lap+1);
        }
    }

    /**
     * Used for when a player finishes the race
     * @param player The player
     */
    private void trackCompleted(Player player) {
        Bukkit.broadcastMessage(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + player.getName() + " has successfully escorted Aledar through the Silent Expanse!");
        int reward = Math.max(TAqMinigames.getEventConfig().getAledarCartData().FINISH_POINTS - (TAqMinigames.getEventConfig().getGenericData().GENERIC_FALLOFF * finishedPlayers.size()), 300);
        scoreManager.increaseScore(player, reward, "You finished " + Utilities.getNumberSuffix(finishedPlayers.size()+1)+ "!", true);
        currentLap.put(player.getUniqueId(), 5);
        Entity vehicle = player.getVehicle();
        if (vehicle != null) {
            for (Entity e : vehicle.getPassengers()) {
                if (!(e instanceof Player)) {
                    e.remove();
                }
            }
            vehicle.removePassenger(player);
            vehicle.remove();
        }
        player.setGameMode(GameMode.SPECTATOR);
        finishedPlayers.add(player.getUniqueId());

    }

    /**
     * Used to spawn new powerups
     */
    private void spawnPowerups() {
        for (Location location : TAqMinigames.getEventConfig().getAledarCartData().POWERUP_LOCATIONS) {
            assert location.getWorld() != null;
            if (!crystalLocations.contains(location)) {
                crystalLocations.add(location);
                EnderCrystal crystal = (EnderCrystal) location.getWorld().spawnEntity(location.clone().add(0,.5,0), EntityType.ENDER_CRYSTAL, false);
                crystal.setShowingBottom(false);
                gameObjectUUIDs.add(crystal.getUniqueId());
                Trigger t = new Trigger(new BoundingBox(location.getX()-1, location.getY()-1, location.getZ()-1, location.getX()+1, location.getY()+1, location.getZ()+1), location.getWorld(), p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                    powerupCollected(p);
                    crystal.remove();
                    crystalLocations.remove(location);
                }, 1000, true, true);
                Trigger.register(t);

            }
        }
    }

    @Override
    public Game getGame() {
        return Game.CART_RACING;
    }

    /**
     * Used for when a player collects a powerup
     * @param player The player
     */
    private void powerupCollected(Player player) {
        player.getInventory().clear();
        switch (ThreadLocalRandom.current().nextInt(0,4)) {
            case 0 -> scoreManager.increaseScore(player, TAqMinigames.getEventConfig().getAledarCartData().CRATE_POINTS, "You found an emerald crate!", true);
            case 1 -> {
                ChatMessageFactory.sendInfoMessageBlock(player, ChatColor.GOLD + "Toxic Slime", "", ChatColor.YELLOW + "Offhand to place a slime behind you", ChatColor.YELLOW + "that slows down players who hit it!");
                player.sendTitle(" ", ChatColor.GREEN + "Toxic Slime " + ChatColor.GRAY + "[Press Offhand Key to use]", 5,40,5);
                Utilities.giveAurumItem(player, "m_racing_slime");
            }
            case 2 -> {
                ChatMessageFactory.sendInfoMessageBlock(player, ChatColor.GOLD + "Dernic Dash", "", ChatColor.YELLOW + "Offhand for chance to get a powerful boost", ChatColor.YELLOW + "to you cart!");
                player.sendTitle(" ", ChatColor.AQUA + "Dernic Dash " + ChatColor.GRAY + "[Press Offhand Key to use]", 5,40,5);
                Utilities.giveAurumItem(player, "m_racing_dash");
            }
            case 3 -> {
                ChatMessageFactory.sendInfoMessageBlock(player, ChatColor.GOLD + "Eldritch Blast", "", ChatColor.YELLOW + "Offhand to slow down all players", ChatColor.YELLOW + "near you!");
                player.sendTitle(" ", ChatColor.GOLD + "Eldritch Blast " + ChatColor.GRAY + "[Press Offhand Key to use]", 5,40,5);
                Utilities.giveAurumItem(player, "m_racing_blast");
            }
        }
    }

    /**
     * Used for when a player uses an ability
     * @param player The player
     * @param material The material of the item that was used
     */
    public void abilityUsed(Player player, Material material) {
        player.getInventory().clear();
        if (material == Material.SLIME_BALL) {
            Entity v = player.getVehicle();
            if (v != null) {
                Slime slime = (Slime) player.getWorld().spawnEntity(v.getLocation().clone().add(v.getLocation().getDirection().multiply(-5)), EntityType.SLIME, false);
                slime.addScoreboardTag("m_racing_slime");
                slime.setInvulnerable(true);
                slime.setRemoveWhenFarAway(false);
                slime.setAI(false);
                slime.setSize(2);
                gameObjectUUIDs.add(slime.getUniqueId());
                player.playSound(player, Sound.BLOCK_SLIME_BLOCK_BREAK,SoundCategory.VOICE, 1,1);
            }

        } else if (material == Material.BLAZE_POWDER) {
            Entity casterVehicle = player.getVehicle();
            if (casterVehicle != null) {
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,SoundCategory.VOICE, 1, 1);
                player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 100, 6, 2, 6, 0);
                for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), 10, 10, 10)) {
                    if (e instanceof Player p) {
                        Entity v = p.getVehicle();
                        if (v != null && !(v.getUniqueId().equals(casterVehicle.getUniqueId()))) {
                            v.setVelocity(new Vector(0, 0, 0));
                        }
                    }
                }
            }
        } else if (material == Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE) {
            Entity e = player.getVehicle();
            if (e != null) {
                e.setVelocity(e.getLocation().getDirection().multiply(5));
                player.playSound(player, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS,SoundCategory.VOICE, 1,1);
            }
        }
    }

    /**
     * Used for when a boat collides with a slime
     * @param event The event
     */
    public void boatCollide(VehicleEntityCollisionEvent event) {
        if (event.getEntity() instanceof Slime s) {
            if (s.getScoreboardTags().contains("m_racing_slime")) {
                event.getVehicle().setVelocity(new Vector(0,0,0));
                s.getWorld().playSound(s.getLocation(), Sound.BLOCK_SLIME_BLOCK_BREAK,SoundCategory.VOICE, 1,1);
                gameObjectUUIDs.remove(s.getUniqueId());
                s.remove();
            }
        }
    }

    /**
     * Used to stop players from leaving their boat
     * @param event The event
     */
    public void boatExit(VehicleExitEvent event) {
        if (event.getExited() instanceof Player p) {
            if (currentLap.getOrDefault(p.getUniqueId(), 0) < 5) {
                if (p.getGameMode() == GameMode.ADVENTURE) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Used to spawn in the racing cart
     * @param location The location of where it should spawn
     * @return The boat
     */
    private Boat buildCart(Location location) {
        assert location.getWorld() != null;
        Boat cart = (Boat) location.getWorld().spawnEntity(location, EntityType.BOAT, false);
        cart.setBoatType(Boat.Type.SPRUCE);
        cart.setInvulnerable(true);

        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, false);
        armorStand.setArms(true);
        armorStand.setBasePlate(false);
        armorStand.setLeftArmPose(new EulerAngle(5.44, 0.0, 0.0));
        armorStand.setRightArmPose(new EulerAngle(5.32,0.0,0.0));
        armorStand.setLeftLegPose(new EulerAngle(4.66, 6.05,0.0));
        armorStand.setRightLegPose(new EulerAngle(4.72, 0.52, 0.0));
        armorStand.setHeadPose(new EulerAngle(6.03, 0.35, 0.0));
        armorStand.setInvulnerable(true);
        assert armorStand.getEquipment() != null;
        armorStand.getEquipment().setHelmet(getHead());
        armorStand.getEquipment().setChestplate(getArmor(Material.LEATHER_CHESTPLATE));
        armorStand.getEquipment().setLeggings(getArmor(Material.LEATHER_LEGGINGS));
        armorStand.getEquipment().setBoots(getArmor(Material.LEATHER_BOOTS));
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING_OR_CHANGING);

        gameObjectUUIDs.add(cart.getUniqueId());
        gameObjectUUIDs.add(armorStand.getUniqueId());

        cart.addPassenger(armorStand);
        return cart;
    }

    /**
     * Used to get the player head of Aledar
     * @return The head
     */
    @SuppressWarnings("CallToPrintStackTrace")
    private ItemStack getHead() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        assert meta != null;
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
        gameProfile.getProperties().put("textures", new Property("textures", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ZkNjRmZDAxZWFlNGIxMjZmOWZhZTU4NjgwNDE5MmM0MmRhOGE3ZTNjYmEwOGExOGVhMmM0MGQwYTNkNDk3ZSJ9fX0="));
        Field profileField;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Used to create the armor for Aledar
     * @param material The type of armor
     * @return The armor
     */
    private ItemStack getArmor(Material material) {
        ItemStack itemStack = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromARGB(1,32, 72, 60));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @Override
    public void end() {
        Trigger.unregisterAll();
        Utilities.registerLobbyTrigger();

        for (Player player : ParticipantManager.getParticipants()) {
            Entity vehicle = player.getVehicle();
            if (vehicle != null) {
                for (Entity e : vehicle.getPassengers()) {
                    if (!(e instanceof Player)) {
                        e.remove();
                    }
                }
                vehicle.removePassenger(player);
                vehicle.remove();
            }
            player.setGameMode(GameMode.SPECTATOR);
        }

        for (UUID uuid : gameObjectUUIDs) {
            Entity e = Bukkit.getEntity(uuid);
            if (e != null) e.remove();
        }

        super.end();
    }
}
