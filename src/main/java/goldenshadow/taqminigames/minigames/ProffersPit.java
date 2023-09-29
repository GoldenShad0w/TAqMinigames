package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.minigames.proffers_pit.*;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Timer;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ProffersPit extends Minigame {

    private final List<UUID> huntedPlayers = new ArrayList<>();
    private final List<Material> xpBombMaterials = new ArrayList<>();
    private final HashMap<UUID, Integer> highestMined = new HashMap<>();
    private final List<ProfEvent> currentEvents = new ArrayList<>();
    private final Team huntedColorTeam;

    public ProffersPit() {
        gameState = GameState.STARTING;
        scoreManager = new ScoreManager("Prof XP", false);
        assert Bukkit.getScoreboardManager() != null;
        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("m_prof") == null) {
            huntedColorTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("m_prof");
        } else huntedColorTeam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("m_prof");
        assert huntedColorTeam != null;
        huntedColorTeam.setColor(ChatColor.RED);
        huntedColorTeam.setAllowFriendlyFire(true);

        assert Constants.WORLD != null;
        Constants.WORLD.setGameRule(GameRule.FALL_DAMAGE, true);
        Constants.WORLD.setGameRule(GameRule.FIRE_DAMAGE, true);
        Constants.WORLD.setGameRule(GameRule.FREEZE_DAMAGE, true);
        Constants.WORLD.setGameRule(GameRule.DROWNING_DAMAGE, true);

        timer = new Timer(0, 29, () -> timer = new Timer(19,59, this::end));
        for (Player player : ParticipantManager.getParticipants()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, true, false, false));
            player.setBedSpawnLocation(Constants.PROF_START_LOCATION, true);
            player.setLevel(105);
            highestMined.put(player.getUniqueId(), 0);
        }
        ParticipantManager.teleportAllPlayers(Constants.PROF_TUTORIAL_LOCATION);
    }


    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Proffer's Pit", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to get as much Prof XP as possible. Taking damage will cause you to lose some XP!", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Mine resources to gain XP and use it to purchase pickaxe upgrades", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(ChatColor.YELLOW + "This is how much XP each ore will give:", " ",ChatColor.YELLOW +  "Copper: 1 XP",ChatColor.YELLOW +  "Iron: 2 XP",ChatColor.YELLOW +  "Gold: 4 XP",ChatColor.YELLOW +  "Cobalt: 8 XP",ChatColor.YELLOW +  "Diamond: 12 XP",ChatColor.YELLOW +  "Molten: 20 XP");
            case 14 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("You can buy pickaxe upgrades at merchants all over the map. These allow you to mine higher level resources", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 18 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("They also sell other small trinkets and the ability to enter hunted mode. Players in hunted mode can attack and kill each other", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 22 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Special events will occur during the minigame, causing certain areas to become more dangerous or more profitable", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The player with the most XP wins", 50), ChatColor.YELLOW).toArray(String[]::new));
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
                    if (ParticipantManager.getParticipants().contains(player)) {
                        Utilities.giveAurumItem(player, "m_prof_t1");
                        player.teleport(Constants.PROF_START_LOCATION);
                    }
                }
                gameState = GameState.RUNNING;
            }
            case 100 -> {
                if (gameState == GameState.RUNNING) {

                    int i = getRelevantEventLocationIndex();
                    EventLocation eventLocation = Constants.PROF_EVENT_LOCATIONS[i];
                    if (eventLocation.eventType() == EventLocation.EventType.XP) {
                        if (doesXPBombExistAlready(eventLocation)) {
                            eventLocation = Constants.PROF_EVENT_LOCATIONS[i-1];
                        }
                    }
                    switch (eventLocation.eventType()) {
                        case GAS -> currentEvents.add(new GasLeak(eventLocation));
                        case TOTEM -> currentEvents.add(new MobTotem(eventLocation));
                        case EARTHQUAKE -> currentEvents.add(new Earthquake(eventLocation));
                        case XP -> {
                            switch (eventLocation.area()) {
                                case COPPER -> {
                                    currentEvents.add(new DoubleXP("Copper"));
                                    xpBombMaterials.add(Material.RAW_COPPER_BLOCK);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.RAW_COPPER_BLOCK), 2399L);
                                }
                                case IRON -> {
                                    currentEvents.add(new DoubleXP("Iron"));
                                    xpBombMaterials.add(Material.RAW_IRON_BLOCK);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.RAW_IRON_BLOCK), 2399L);
                                }
                                case GOLD -> {
                                    currentEvents.add(new DoubleXP("Gold"));
                                    xpBombMaterials.add(Material.RAW_GOLD_BLOCK);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.RAW_GOLD_BLOCK), 2399L);
                                }
                                case COBALT -> {
                                    currentEvents.add(new DoubleXP("Cobalt"));
                                    xpBombMaterials.add(Material.LAPIS_ORE);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.LAPIS_ORE), 2399L);
                                }
                                case DIAMOND -> {
                                    currentEvents.add(new DoubleXP("Diamond"));
                                    xpBombMaterials.add(Material.DIAMOND_ORE);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.DIAMOND_ORE), 2399L);
                                }
                                case MOLTEN -> {
                                    currentEvents.add(new DoubleXP("Molten"));
                                    xpBombMaterials.add(Material.DEEPSLATE_REDSTONE_ORE);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.DEEPSLATE_REDSTONE_ORE), 2399L);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (tick > 100) {
            tick = 30;
        }
        if (gameState == GameState.RUNNING) {
            Iterator<ProfEvent> it = currentEvents.iterator();
            while (it.hasNext()) {
                ProfEvent e = it.next();
                e.tick();
                if (e.isDone()) it.remove();
            }
        }

        super.tick();
    }

    @Override
    public void onDeath(Player player) {
        int amount = (int) (scoreManager.getScore(player.getUniqueId()) * 0.1);
        if (player.getKiller() != null) {
            scoreManager.increaseScore(player.getKiller(), (int) (amount * 0.1), "You killed " + player.getName(), false);
        }
        scoreManager.increaseScore(player, amount * -1,false);
        player.sendMessage(ChatColor.DARK_RED + "[-" + amount + " Prof XP] " + ChatColor.RED + "You died!");
        huntedPlayers.remove(player.getUniqueId());
        player.removePotionEffect(PotionEffectType.GLOWING);
        huntedColorTeam.removeEntry(player.getName());
        player.setWorldBorder(null);
    }

    @Override
    public void playerReconnect(Player player) {
        if (gameState != GameState.RUNNING) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        player.setWorldBorder(null);
        huntedPlayers.remove(player.getUniqueId());
        huntedColorTeam.removeEntry(player.getName());
        player.setCooldown(Material.FIRE_CHARGE, 200);
    }

    @Override
    public void playerDisconnect(Player player) {
        huntedColorTeam.removeEntry(player.getName());
    }

    @Override
    protected void insertPlayer(Player player) {
        super.insertPlayer(player);
        player.teleport(Constants.PROF_START_LOCATION);
        player.setBedSpawnLocation(Constants.PROF_START_LOCATION, true);
        Utilities.giveAurumItem(player, "m_prof_t1");
    }

    public void huntedToggled(Player player) {
        if (!player.hasCooldown(Material.FIRE_CHARGE)) {
            if (huntedPlayers.contains(player.getUniqueId())) {
                huntedPlayers.remove(player.getUniqueId());
                player.playSound(player, Sound.ENTITY_RAVAGER_STUNNED, 1, 1);
                player.removePotionEffect(PotionEffectType.GLOWING);
                huntedColorTeam.removeEntry(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 100, false, false, false));
                player.setWorldBorder(null);
                player.sendMessage(ChatMessageFactory.singleLineInfo("You have exited out of hunted mode!"));
            } else {
                huntedPlayers.add(player.getUniqueId());
                player.playSound(player, Sound.ENTITY_RAVAGER_ROAR, 1, 1);
                huntedColorTeam.addEntry(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, PotionEffect.INFINITE_DURATION, 0, false, false, false));
                player.setWorldBorder(TAqMinigames.getPlugin().getServer().createWorldBorder());
                assert player.getWorldBorder() != null;
                player.getWorldBorder().setWarningDistance(Integer.MAX_VALUE);
                player.sendMessage(ChatMessageFactory.singleLineInfo("You have entered hunted mode!"));
            }
            player.setCooldown(Material.FIRE_CHARGE, 600);
        }
    }

    public void blockMined(Player player, Block block) {
        if (player.getGameMode() != GameMode.ADVENTURE) return;
        int amount = 0;
        double multiplier = 1;

        switch (block.getType()) {
            //  1,2,4,8,12,16 ??
            case RAW_COPPER_BLOCK -> amount = 1;
            case RAW_IRON_BLOCK -> {
                amount = 2;
                if (!(highestMined.containsKey(player.getUniqueId()) && highestMined.get(player.getUniqueId()) > 1)) {
                    highestMined.put(player.getUniqueId(), 1);
                }
            }
            case RAW_GOLD_BLOCK -> {
                amount = 4;
                if (!(highestMined.containsKey(player.getUniqueId()) && highestMined.get(player.getUniqueId()) > 2)) {
                    highestMined.put(player.getUniqueId(), 2);
                }
            }
            case DEEPSLATE_LAPIS_ORE -> {
                amount = 8;
                if (!(highestMined.containsKey(player.getUniqueId()) && highestMined.get(player.getUniqueId()) > 3)) {
                    highestMined.put(player.getUniqueId(), 3);
                }
            }
            case DIAMOND_ORE -> {
                amount = 12;
                if (!(highestMined.containsKey(player.getUniqueId()) && highestMined.get(player.getUniqueId()) > 4)) {
                    highestMined.put(player.getUniqueId(), 4);
                }
            }
            case DEEPSLATE_REDSTONE_ORE -> {
                amount = 20;
                if (!(highestMined.containsKey(player.getUniqueId()) && highestMined.get(player.getUniqueId()) > 5)) {
                    highestMined.put(player.getUniqueId(), 5);
                }
            }
        }

        if (xpBombMaterials.contains(block.getType())) multiplier += 2;
        if (huntedPlayers.contains(player.getUniqueId())) multiplier += .5;

        amount = (int) (amount * multiplier);
        ChatMessageFactory.sendActionbarMessage(player, net.md_5.bungee.api.ChatColor.YELLOW + "+" + amount + " Prof XP" + (multiplier > 1 ? net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + " [" + multiplier + "x]" : ""));
        scoreManager.increaseScore(player, amount, false);
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
        Material type = block.getType();
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> block.setType(type), 200L);
    }

    public void damageTaken(EntityDamageEvent event) {
        if (gameState == GameState.RUNNING) {
            if (event.getEntity() instanceof Player p) {
                if (event instanceof EntityDamageByEntityEvent damageByEntityEvent) {
                    if (damageByEntityEvent.getDamager() instanceof Player attacker && damageByEntityEvent.getEntity() instanceof Player) {
                        if (isHunted(p) && isHunted(attacker)) {
                            if (p.getCooldown(Material.FIRE_CHARGE) < 200) {
                                p.setCooldown(Material.FIRE_CHARGE, 200);
                            }
                        } else {
                            event.setCancelled(true);
                        }
                        return;
                    }
                }
                if (ParticipantManager.getParticipants().contains(p)) {
                    int damage = (int) Math.max(1, event.getDamage());
                    int amount = (int) Math.max(scoreManager.getScore(p.getUniqueId()) * ((double) damage/100), 1);
                    scoreManager.increaseScore(p, amount * -1,false);
                    p.sendMessage(ChatColor.DARK_RED + "[-" + amount + " Prof XP] " + ChatColor.RED + "You took damage!");
                    if (!isHunted(p)) {
                        event.setDamage(0);
                    }
                }
            }
        }
    }



    public boolean isHunted(Player player) {
        return huntedPlayers.contains(player.getUniqueId());
    }

    @Override
    public Game getGame() {
        return Game.PROFFERS_PIT;
    }

    @Override
    public void end() {
        assert Constants.WORLD != null;
        for (Player p : ParticipantManager.getParticipants()) {
            p.setLevel(0);
            p.getInventory().clear();
            p.getActivePotionEffects().clear();
            p.setGameMode(GameMode.SPECTATOR);
            p.setWorldBorder(null);
            huntedColorTeam.removeEntry(p.getName());

        }
        Constants.WORLD.setGameRule(GameRule.FALL_DAMAGE, false);
        Constants.WORLD.setGameRule(GameRule.FIRE_DAMAGE, false);
        Constants.WORLD.setGameRule(GameRule.FREEZE_DAMAGE, false);
        Constants.WORLD.setGameRule(GameRule.DROWNING_DAMAGE, false);

        Iterator<ProfEvent> it = currentEvents.iterator();
        while (it.hasNext()) {
            ProfEvent e = it.next();
            if (e instanceof MobTotem t) {
                t.deleteTotem();
            }
            it.remove();
        }

        super.end();
    }

    public void shopEvent(InventoryClickEvent event) {
        if (event.getWhoClicked().getOpenInventory().getTitle().equals(ChatColor.BOLD + "Mining Supplies")) {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null) {
                if (event.getClickedInventory() != player.getInventory()) {
                    event.setCancelled(true);
                    int score = scoreManager.getScore(player.getUniqueId());
                    if (event.getCurrentItem().getType() == Material.LEATHER_BOOTS) {
                        if (score >= Constants.PROF_BOOTS_PRICE) {
                            scoreManager.increaseScore(player, -Constants.PROF_BOOTS_PRICE, false);
                            Utilities.giveAurumItem(player, "m_prof_boots");
                            player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                        } else {
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                        }
                        return;
                    }
                    if (event.getCurrentItem().getType() == Material.FIRE_CHARGE) {
                        if (score >= Constants.PROF_HUNTED_PRICE) {
                            scoreManager.increaseScore(player, -Constants.PROF_HUNTED_PRICE, false);
                            player.getInventory().addItem(ShopItems.getHuntedToken());
                            player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                        } else {
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                        }
                        return;
                    }
                    if (event.getCurrentItem().getType() == Material.IRON_SWORD) {
                        if (score >= Constants.PROF_SWORD_PRICE) {
                            scoreManager.increaseScore(player, -Constants.PROF_SWORD_PRICE, false);
                            Utilities.giveAurumItem(player, "m_prof_sword");
                            player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                        } else {
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                        }
                        return;
                    }
                    if (event.getCurrentItem().getType() == Material.POTION) {
                        if (score >= Constants.PROF_SPEED_PRICE) {
                            scoreManager.increaseScore(player, -Constants.PROF_SPEED_PRICE, false);
                            Utilities.giveAurumItem(player, "m_prof_speed");
                            player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                        } else {
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                        }
                        return;
                    }
                    if (event.getCurrentItem().getType() == Material.GLISTERING_MELON_SLICE) {
                        if (score >= Constants.PROF_HASTE_PRICE) {
                            scoreManager.increaseScore(player, -Constants.PROF_HASTE_PRICE, false);
                            Utilities.giveAurumItem(player, "m_prof_haste");
                            player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                        } else {
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                        }
                        return;
                    }
                    if (event.getCurrentItem().getType() == Material.BEETROOT_SOUP) {
                        if (score >= Constants.PROF_HEALTH_PRICE) {
                            scoreManager.increaseScore(player, -Constants.PROF_HEALTH_PRICE, false);
                            Utilities.giveAurumItem(player, "m_prof_health");
                            player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                        } else {
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                        }
                        return;
                    }
                    if (event.getCurrentItem().getType() == Material.IRON_PICKAXE) {

                        if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 2) {
                            if (score >= Constants.PROF_T2_PRICE && hasPickaxe(player, 1)) {
                                scoreManager.increaseScore(player, -Constants.PROF_T2_PRICE, false);
                                player.getInventory().remove(Material.IRON_PICKAXE);
                                Utilities.giveAurumItem(player, "m_prof_t2");
                                player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                            } else {
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                            }
                        }
                        else if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 3) {
                            if (score >= Constants.PROF_T3_PRICE && hasPickaxe(player, 2)) {
                                scoreManager.increaseScore(player, -Constants.PROF_T3_PRICE, false);
                                player.getInventory().remove(Material.IRON_PICKAXE);
                                Utilities.giveAurumItem(player, "m_prof_t3");
                                player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                            } else {
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                            }
                        }
                        else if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 4) {
                            if (score >= Constants.PROF_T4_PRICE && hasPickaxe(player, 3)) {
                                scoreManager.increaseScore(player, -Constants.PROF_T4_PRICE, false);
                                player.getInventory().remove(Material.IRON_PICKAXE);
                                Utilities.giveAurumItem(player, "m_prof_t4");
                                player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                            } else {
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                            }
                        }
                    }
                    if (event.getCurrentItem().getType() == Material.DIAMOND_PICKAXE) {
                        if (score >= Constants.PROF_T5_PRICE && hasPickaxe(player, 4)) {
                            scoreManager.increaseScore(player, -Constants.PROF_T5_PRICE, false);
                            player.getInventory().remove(Material.IRON_PICKAXE);
                            Utilities.giveAurumItem(player, "m_prof_t5");
                            player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                        } else {
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                        }
                        return;
                    }
                    if (event.getCurrentItem().getType() == Material.NETHERITE_PICKAXE) {
                        if (score >= Constants.PROF_T6_PRICE && hasPickaxe(player, 5)) {
                            scoreManager.increaseScore(player, -Constants.PROF_T6_PRICE, false);
                            player.getInventory().remove(Material.DIAMOND_PICKAXE);
                            Utilities.giveAurumItem(player, "m_prof_t6");
                            player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                        } else {
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                        }

                    }
                }
            }
        }
    }

    private boolean hasPickaxe(Player player, int tier) {

        Material mat = tier < 5 ? Material.IRON_PICKAXE : tier < 6 ? Material.DIAMOND_PICKAXE : Material.NETHERITE_PICKAXE;

        if (player.getInventory().contains(mat)) {
            for (ItemStack i : player.getInventory().getContents()) {
                if (i != null) {
                    if (i.getType() == mat) {
                        ItemMeta meta = i.getItemMeta();
                        assert meta != null;
                        if (meta.hasCustomModelData()) {
                            return meta.getCustomModelData() == tier;
                        }
                    }
                }
            }
        }
        return false;
    }

    private int getRelevantEventLocationIndex() {
        if (timer.getMinutesLeft() > 15) {
            if (ThreadLocalRandom.current().nextInt(0, 3) == 0) {
                return ThreadLocalRandom.current().nextInt(0, 11);
            } else {
                return ThreadLocalRandom.current().nextInt(0, 7);
            }
        } else if (timer.getMinutesLeft() > 10) {
            if (ThreadLocalRandom.current().nextInt(0, 3) == 0) {
                return ThreadLocalRandom.current().nextInt(3,15);
            } else {
                return ThreadLocalRandom.current().nextInt(7,17);
            }
        } else if (timer.getMinutesLeft() > 5) {
            if (ThreadLocalRandom.current().nextInt(0, 3) == 0) {
                return ThreadLocalRandom.current().nextInt(10, 18);
            } else {
                return ThreadLocalRandom.current().nextInt(12, 20);
            }
        } else {
            return ThreadLocalRandom.current().nextInt(13, 20);
        }
    }

    private boolean doesXPBombExistAlready(EventLocation location) {
        return switch (location.area()) {
            case COPPER -> xpBombMaterials.contains(Material.RAW_COPPER_BLOCK);
            case IRON -> xpBombMaterials.contains(Material.RAW_IRON_BLOCK);
            case GOLD -> xpBombMaterials.contains(Material.RAW_GOLD_BLOCK);
            case COBALT -> xpBombMaterials.contains(Material.DEEPSLATE_LAPIS_ORE);
            case DIAMOND -> xpBombMaterials.contains(Material.DIAMOND_ORE);
            case MOLTEN -> xpBombMaterials.contains(Material.DEEPSLATE_REDSTONE_ORE);
        };
    }
}
