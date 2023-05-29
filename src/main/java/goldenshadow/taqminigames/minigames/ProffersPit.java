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
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
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

        timer = new Timer(0, 29, () -> timer = new Timer(20,0, this::end));
        for (Player player : ParticipantManager.getParticipants()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, true, false, false));
            player.setBedSpawnLocation(Constants.PROF_START_LOCATION, true);
            player.setLevel(100);
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
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Each better tier of resource doubles the XP rewarded for mining it", 50), ChatColor.YELLOW).toArray(String[]::new));
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
            case 150 -> {
                if (gameState == GameState.RUNNING) {
                    currentEvents.add(new GasLeak());
                    /*
                    switch (ThreadLocalRandom.current().nextInt(0,3)) {
                        case 0 -> currentEvents.add(new GasLeak());
                        case 1 -> currentEvents.add(new MobTotem());
                        case 2 -> {
                            switch (ThreadLocalRandom.current().nextInt(0,5)) {
                                case 0 -> {
                                    currentEvents.add(new DoubleXP("Copper"));
                                    xpBombMaterials.add(Material.RAW_COPPER_BLOCK);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.RAW_COPPER_BLOCK), 2399L);
                                }
                                case 1 -> {
                                    currentEvents.add(new DoubleXP("Iron"));
                                    xpBombMaterials.add(Material.RAW_IRON_BLOCK);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.RAW_IRON_BLOCK), 2399L);
                                }
                                case 2 -> {
                                    currentEvents.add(new DoubleXP("Gold"));
                                    xpBombMaterials.add(Material.RAW_GOLD_BLOCK);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.RAW_GOLD_BLOCK), 2399L);
                                }
                                case 3 -> {
                                    currentEvents.add(new DoubleXP("Cobalt"));
                                    xpBombMaterials.add(Material.LAPIS_ORE);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.LAPIS_ORE), 2399L);
                                }
                                case 4 -> {
                                    currentEvents.add(new DoubleXP("Diamond"));
                                    xpBombMaterials.add(Material.DIAMOND_ORE);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.DIAMOND_ORE), 2399L);
                                }
                                case 5 -> {
                                    currentEvents.add(new DoubleXP("Molten"));
                                    xpBombMaterials.add(Material.DEEPSLATE_REDSTONE_ORE);
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> xpBombMaterials.remove(Material.DEEPSLATE_REDSTONE_ORE), 2399L);
                                }
                            }
                        }
                    }
                    */
                }
            }
        }
        if (tick > 150) {
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
        scoreManager.increaseScore(player, -200,false);
        player.sendMessage(ChatColor.DARK_RED + "[-200 Prof XP] " + ChatColor.RED + "You died!");
        huntedPlayers.remove(player.getUniqueId());
        player.setGlowing(false);
        huntedColorTeam.removeEntry(player.getName());
        player.setWorldBorder(null);
    }

    @Override
    public void playerReconnect(Player player) {
        player.setWorldBorder(null);
        huntedPlayers.remove(player.getUniqueId());
        huntedColorTeam.removeEntry(player.getName());
        player.setCooldown(Material.FIRE_CHARGE, 200);

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
                player.setGlowing(false);
                huntedColorTeam.removeEntry(player.getName());
                player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 100, false, false, false));
                player.setWorldBorder(null);
                player.sendMessage(ChatMessageFactory.singleLineInfo("You have exited out of hunted mode!"));
            } else {
                huntedPlayers.add(player.getUniqueId());
                player.playSound(player, Sound.ENTITY_RAVAGER_ROAR, 1, 1);
                huntedColorTeam.addEntry(player.getName());
                player.setGlowing(true);
                player.setWorldBorder(TAqMinigames.getPlugin().getServer().createWorldBorder());
                assert player.getWorldBorder() != null;
                player.getWorldBorder().setWarningDistance(Integer.MAX_VALUE);
                player.sendMessage(ChatMessageFactory.singleLineInfo("You have entered hunted mode!"));
            }
            player.setCooldown(Material.FIRE_CHARGE, 600);
        }
    }

    public void blockMined(Player player, Block block) {
        int amount = 0;
        double multiplier = 1;

        switch (block.getType()) {
            case RAW_COPPER_BLOCK -> amount = xpBombMaterials.contains(block.getType()) ? 2 : 1;
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
            case LAPIS_ORE -> {
                amount = 8;
                if (!(highestMined.containsKey(player.getUniqueId()) && highestMined.get(player.getUniqueId()) > 3)) {
                    highestMined.put(player.getUniqueId(), 3);
                }
            }
            case DIAMOND_ORE -> {
                amount = 16;
                if (!(highestMined.containsKey(player.getUniqueId()) && highestMined.get(player.getUniqueId()) > 4)) {
                    highestMined.put(player.getUniqueId(), 4);
                }
            }
            case DEEPSLATE_REDSTONE_ORE -> {
                amount = 32;
                if (!(highestMined.containsKey(player.getUniqueId()) && highestMined.get(player.getUniqueId()) > 5)) {
                    highestMined.put(player.getUniqueId(), 5);
                }
            }
        }

        if (xpBombMaterials.contains(block.getType())) multiplier += 2;
        if (huntedPlayers.contains(player.getUniqueId())) multiplier += .5;

        amount *= multiplier;

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(net.md_5.bungee.api.ChatColor.YELLOW + "+" + amount + " Prof XP" + (multiplier > 1 ? net.md_5.bungee.api.ChatColor.LIGHT_PURPLE + " [" + multiplier + "x]" : "")));
        scoreManager.increaseScore(player, amount, false);
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
        Material type = block.getType();
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> block.setType(type), 200L);
    }

    public void damageTaken(EntityDamageEvent event) {
        if (gameState == GameState.RUNNING) {
            if (event.getEntity() instanceof Player p) {
                if (event instanceof EntityDamageByEntityEvent damageByEntityEvent) {
                    if (damageByEntityEvent.getDamager() instanceof Player attacker) {
                        if (!(huntedPlayers.contains(attacker.getUniqueId()) && huntedPlayers.contains(p.getUniqueId()))) {
                            return;
                        }
                        if (p.getCooldown(Material.FIRE_CHARGE) < 200) {
                            p.setCooldown(Material.FIRE_CHARGE, 200);
                        }
                    }
                }
                if (ParticipantManager.getParticipants().contains(p)) {
                    int damage = (int) Math.max(1, (event.getDamage() * (highestMined.getOrDefault(p.getUniqueId(), 1))));
                    scoreManager.increaseScore(p, -damage,false);
                    p.sendMessage(ChatColor.DARK_RED + "[-" + damage + " Prof XP] " + ChatColor.RED + "You took damage!");
                    if (!huntedPlayers.contains(p.getUniqueId())) {
                        event.setDamage(0);
                    }
                }
            }
        }
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
                            if (score >= Constants.PROF_T2_PRICE) {
                                scoreManager.increaseScore(player, -Constants.PROF_T2_PRICE, false);
                                Utilities.giveAurumItem(player, "m_prof_t2");
                                player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                            } else {
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                            }
                        }
                        else if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 3) {
                            if (score >= Constants.PROF_T3_PRICE) {
                                scoreManager.increaseScore(player, -Constants.PROF_T3_PRICE, false);
                                Utilities.giveAurumItem(player, "m_prof_t3");
                                player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                            } else {
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                            }
                        }
                        else if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 4) {
                            if (score >= Constants.PROF_T4_PRICE) {
                                scoreManager.increaseScore(player, -Constants.PROF_T4_PRICE, false);
                                Utilities.giveAurumItem(player, "m_prof_t4");
                                player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                            } else {
                                player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                            }
                        }
                    }
                    if (event.getCurrentItem().getType() == Material.DIAMOND_PICKAXE) {
                        if (score >= Constants.PROF_T5_PRICE) {
                            scoreManager.increaseScore(player, -Constants.PROF_T5_PRICE, false);
                            Utilities.giveAurumItem(player, "m_prof_t5");
                            player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1,1);
                        } else {
                            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1,1);
                        }
                        return;
                    }
                    if (event.getCurrentItem().getType() == Material.NETHERITE_PICKAXE) {
                        if (score >= Constants.PROF_T6_PRICE) {
                            scoreManager.increaseScore(player, -Constants.PROF_T6_PRICE, false);
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
}
