package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.util.*;
import goldenshadow.taqminigames.util.Timer;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class NesaakFight extends Minigame {

    private final String[] deathMessages = {"%s was murdered by %s!", "%s didn't stand a chance against %s!", "%s was annihilated by %s!", "%s was killed by %s!", "%s was converted to a frozen corpse by %s!"};
    private final HashMap<UUID, Integer> betterGatherers = new HashMap<>();
    private final HashMap<UUID, Integer> multishoters = new HashMap<>();
    private final List<UUID> registeredPowerups = new ArrayList<>();

    /**
     * Used to start a new nesaak snowball fight minigame
     */
    public NesaakFight() {

        gameState = GameState.STARTING;
        scoreManager = new ScoreManager("Kills", false);
        timer = new Timer(0, 29, () -> timer = new Timer(10,0, this::end));
        for (Player player : ParticipantManager.getParticipants()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, true, false, false));
            assert player.getEquipment() != null;
            player.getEquipment().setChestplate(getHealthLimiter());
        }
        ParticipantManager.teleportAllPlayers(Constants.NESAAK_TUTORIAL_LOCATION);

    }

    /**
     * 1hz loop
     */
    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Nesaak Snowball Fight", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to beat other players in a snowball fight", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("You have 3 hearts, every time you get hit you lose one", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("You can replenish your snowballs by right clicking on snow with your shovel", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 14 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Powerups will spawn around the map for you to collect", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 18 -> ChatMessageFactory.sendInfoBlockToAll(ChatColor.YELLOW + "Possible powerups are:",ChatColor.YELLOW +  "Speed for 10 seconds",ChatColor.YELLOW +  "Regain all lost health", ChatColor.YELLOW + "Multishot for 20 seconds",ChatColor.YELLOW +  "Boosted gathering for 20 seconds");
            case 22 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("After respawning, you will have invulnerability for 5 seconds or until you attack someone", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The player with the most kills wins", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 25 -> {
                for (Player player : ParticipantManager.getAll()) {
                    player.sendMessage(ChatMessageFactory.singleLineInfo("Starting in 5 seconds!"));
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    if (ParticipantManager.getParticipants().contains(player)) {
                        player.getInventory().addItem(getShovel(), new ItemStack(Material.SNOWBALL, 12));

                        player.setBedSpawnLocation(getRandomSpawn(), true);
                        player.teleport(getRandomSpawn());
                    }
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

                }
                gameState = GameState.RUNNING;
            }
            case 60 -> {
                if (gameState == GameState.RUNNING) {
                    Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Powerups have spawned!"));
                    for (int i = 0; i < 5; i++) {
                        spawnPowerup(Constants.NESAAK_POWERUP_NODES[ThreadLocalRandom.current().nextInt(0, Constants.NESAAK_POWERUP_NODES.length)]);
                    }
                }
            }
        }
        if (tick >= 80) {
            tick = 59;
        }
        super.tick();
    }

    /**
     * Used for when a player dies
     * @param player The players who died
     */
    @Override
    public void onDeath(Player player) {
        player.setCooldown(Material.NETHERITE_SHOVEL, 0);
        multishoters.remove(player.getUniqueId());
        betterGatherers.remove(player.getUniqueId());
        player.getInventory().remove(Material.SNOWBALL);
        player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 12));
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100, true, false, true)), 1L);
        player.setBedSpawnLocation(getRandomSpawn(), true);
    }

    /**
     * Used for when a player gets hit by a snowball
     * @param player The player who got hit
     * @param attacker The owner of the snowball that hit them
     */
    public void playerHit(Player player, Player attacker) {
        if (player.getHealth() <= 2) {
            if (player.getNoDamageTicks() == 0) {
                killAchieved(attacker, player.getName());
            }
        }
        player.damage(2);
    }

    /**
     * Used for when a player kills another
     * @param player The player who got the kill
     * @param victimName The name of the player who got killed
     */
    public void killAchieved(Player player, String victimName) {
        scoreManager.increaseScore(player, 1, false);
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1,100));
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1,1);
        player.sendMessage(ChatMessageFactory.pointsGainedInfo("You killed " + victimName, 1, "kill"));
        Bukkit.broadcastMessage(ChatColor.GOLD + String.format(deathMessages[ThreadLocalRandom.current().nextInt(0, deathMessages.length)], victimName, player.getName()));
    }

    /**
     * Used for when a player reconnects during the game. Empty here because nothing specific needs to be done to them
     * @param player The player who reconnected
     */
    @Override
    public void playerReconnect(Player player) {

    }

    @Override
    public void insertPlayer(Player player) {
        super.insertPlayer(player);
        player.teleport(Constants.NESAAK_SPAWN_POINTS[0]);
        player.getInventory().addItem(getShovel(), new ItemStack(Material.SNOWBALL, 12));
        assert player.getEquipment() != null;
        player.getEquipment().setChestplate(getHealthLimiter());
    }

    /**
     * Getter for the game
     * @return The game
     */
    @Override
    public Game getGame() {
        return Game.NESAAK_SNOWBALL_FIGHT;
    }

    /**
     * Utility method to get the shovel item
     * @return The shovel item
     */
    private static ItemStack getShovel() {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_SHOVEL);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + "Snow Shovel");
        meta.setLore(List.of(" ", ChatColor.AQUA + "Right click on snow blocks to", ChatColor.AQUA + "collect new snowballs!"));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Utility method to get health limiter item. The reason this is used instead of settings the players health attribute to 6 is so that players who disconnect during this game aren't permanently at 6hp
     * @return The health limiter item
     */
    private static ItemStack getHealthLimiter() {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier("max_health", -14, AttributeModifier.Operation.ADD_NUMBER));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        meta.setCustomModelData(100090);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Used to spawn a random powerup
     * @param location The location it should be spawned at
     */
    private void spawnPowerup(Location location) {
        assert location.getWorld() != null;
        for (Entity e : location.getWorld().getNearbyEntities(location, 1,1,1)) {
            if (e instanceof ArmorStand) {
                return;
            }
        }
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND, false);
        BoundingBox box = new BoundingBox(location.getX()-1, location.getY(), location.getZ() -1, location.getX() +1, location.getY()+2, location.getZ()+1);
        armorStand.setInvisible(true);
        Utilities.lockArmorStand(armorStand);
        armorStand.setCustomNameVisible(true);
        registeredPowerups.add(armorStand.getUniqueId());
        assert armorStand.getEquipment() != null;
        Random r = new Random();
        switch (r.nextInt(0,4)) {
            case 0 -> {
                armorStand.setCustomName(ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + ">> " + ChatColor.AQUA + ChatColor.BOLD + "Heal" + ChatColor.DARK_AQUA + ChatColor.BOLD + " <<");
                armorStand.getEquipment().setHelmet(new ItemStack(Material.RED_GLAZED_TERRACOTTA));
                Trigger.register(new Trigger(box, location.getWorld(), p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                    p.playSound(p, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1,1);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1,100));
                    Entity e = Bukkit.getEntity(armorStand.getUniqueId());
                    if (e != null) {
                        e.remove();
                    }
                }, 1000, true, true));
            }
            case 1 -> {
                armorStand.setCustomName(ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + ">> " + ChatColor.AQUA + ChatColor.BOLD + "Speed" + ChatColor.DARK_AQUA + ChatColor.BOLD + " <<");
                armorStand.getEquipment().setHelmet(new ItemStack(Material.ICE));
                Trigger.register(new Trigger(box, location.getWorld(), p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                    p.playSound(p, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1,1);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200,1));
                    Entity e = Bukkit.getEntity(armorStand.getUniqueId());
                    if (e != null) {
                        e.remove();
                    }
                }, 1000, true, true));
            }
            case 2 -> {
                armorStand.setCustomName(ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + ">> " + ChatColor.AQUA + ChatColor.BOLD + "Multishot" + ChatColor.DARK_AQUA + ChatColor.BOLD + " <<");
                armorStand.getEquipment().setHelmet(new ItemStack(Material.TARGET));
                Trigger.register(new Trigger(box, location.getWorld(), p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                    if (multishoters.containsKey(p.getUniqueId())) {
                        Bukkit.getScheduler().cancelTask(multishoters.get(p.getUniqueId()));
                    }
                    p.playSound(p, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1,1);
                    p.sendMessage(ChatMessageFactory.singleLineInfo("Multishot activated!"));
                    int id = Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                        p.sendMessage(ChatMessageFactory.singleLineInfo("Multishot has run out!"));
                        multishoters.remove(p.getUniqueId());
                        }, 400L);
                    multishoters.put(p.getUniqueId(), id);
                    Entity e = Bukkit.getEntity(armorStand.getUniqueId());
                    if (e != null) {
                        e.remove();
                    }
                }, 1000, true, true));
            }
            case 3 -> {
                armorStand.setCustomName(ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + ">> " + ChatColor.AQUA + ChatColor.BOLD + "Gathering Boost" + ChatColor.DARK_AQUA + ChatColor.BOLD + " <<");
                armorStand.getEquipment().setHelmet(new ItemStack(Material.BARREL));
                Trigger.register(new Trigger(box, location.getWorld(), p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                    if (betterGatherers.containsKey(p.getUniqueId())) {
                        Bukkit.getScheduler().cancelTask(betterGatherers.get(p.getUniqueId()));
                    }
                    p.playSound(p, Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1,1);
                    p.sendMessage(ChatMessageFactory.singleLineInfo("Gathering boost activated!"));
                    int id = Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                        p.sendMessage(ChatMessageFactory.singleLineInfo("Gathering boost has run out!"));
                        betterGatherers.remove(p.getUniqueId());
                    }, 400L);
                    betterGatherers.put(p.getUniqueId(), id);
                    Entity e = Bukkit.getEntity(armorStand.getUniqueId());
                    if (e != null) {
                        e.remove();
                    }
                }, 1000, true, true));
            }
        }

    }

    /**
     * Used to get a random respawn point for a player
     * @return The random respawn point
     */
    private Location getRandomSpawn() {
        return Constants.NESAAK_SPAWN_POINTS[ThreadLocalRandom.current().nextInt(0, Constants.NESAAK_SPAWN_POINTS.length)];
    }

    /**
     * Used for when a player shoots a snowball
     * @param snowball The snowball the was shot
     * @param player The player who shot it
     */
    public void snowballShoot(Snowball snowball, Player player) {
        if (player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
        if (multishoters.containsKey(player.getUniqueId())) {
            Snowball sb1 = (Snowball) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.SNOWBALL, false);
            sb1.setShooter(player);
            sb1.setVelocity(snowball.getVelocity().rotateAroundY(Math.toRadians(5)));

            Snowball sb2 = (Snowball) player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.SNOWBALL, false);
            sb2.setShooter(player);
            sb2.setVelocity(snowball.getVelocity().rotateAroundY(Math.toRadians(-5)));
        }
        player.setCooldown(Material.SNOWBALL, 5);
    }

    /**
     * Used for when a player right clicks on snow
     * @param player The player who clicked
     * @param block The block they clicked
     */
    public void snowClicked(Player player, Block block) {
        ItemStack it = new ItemStack(Material.SNOW_BLOCK);
        Location loc = block.getRelative(BlockFace.UP).getLocation();
        assert loc.getWorld() != null;
        loc.add(loc.getX() > 0 ? -.5 : .5, 0, loc.getX() > 0 ? -.5 : .5);
        if (player.hasCooldown(Material.NETHERITE_SHOVEL)) {
            ChatMessageFactory.sendActionbarMessage(player,net.md_5.bungee.api.ChatColor.YELLOW + " Your shovel is still on cooldown!");
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 100, false, false, false));
        player.setCooldown(Material.NETHERITE_SHOVEL, 100);
        loc.getWorld().spawnParticle(Particle.ITEM_CRACK, loc, 10, 0.2,0,0.2, 0.1, it);
        loc.getWorld().playSound(loc, Sound.BLOCK_SNOW_STEP, 1,1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
            loc.getWorld().spawnParticle(Particle.ITEM_CRACK, loc, 10, 0.2,0,0.2, 0.1 ,it);
            loc.getWorld().playSound(loc, Sound.BLOCK_SNOW_STEP, 1,1);
        }, 20L);
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
            loc.getWorld().spawnParticle(Particle.ITEM_CRACK, loc, 10,0.2,0,0.2,0.1, it);
            loc.getWorld().playSound(loc, Sound.BLOCK_SNOW_STEP, 1,1);

            player.getInventory().addItem(new ItemStack(Material.SNOWBALL, betterGatherers.containsKey(player.getUniqueId()) ? 16 : 8));
        }, 40L);

    }

    /**
     * Used to end the minigame
     */
    @Override
    public void end() {
        for (Player p : ParticipantManager.getParticipants()) {
            p.setGameMode(GameMode.SPECTATOR);
            p.getInventory().clear();
        }
        Trigger.unregisterAll();
        Utilities.registerLobbyTrigger();
        for (UUID uuid : registeredPowerups) {
            Entity e = Bukkit.getEntity(uuid);
            if (e != null) {
                e.remove();
            }
        }
        super.end();

    }


}
