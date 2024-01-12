package goldenshadow.taqminigames;

import goldenshadow.taqminigames.commands.Command;
import goldenshadow.taqminigames.commands.TabComplete;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.SoundFile;
import goldenshadow.taqminigames.event.*;
import goldenshadow.taqminigames.events.*;
import goldenshadow.taqminigames.minigames.*;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Trigger;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
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
    public static boolean inPreStartPhase = false;

    @Override
    public void onEnable() {
        plugin = this;
        
        registerEvents();
        getConfig().options().copyDefaults(true);
        saveConfig();
        initLoops();
        Objects.requireNonNull(this.getCommand("minigames")).setExecutor(new Command());
        Objects.requireNonNull(this.getCommand("minigames")).setTabCompleter(new TabComplete());

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, Utilities::registerLobbyTrigger, 1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Used to start the event
     */
    public static void start() {
        if (!isRunning) {
            inPreStartPhase = false;
            isRunning = true;
            gameSelection = null;
            totalScoreManager = new ScoreManager("Emeralds", true);
            if (!SoundtrackManager.getSoundFile().name().equals("minigames.lobby")) {
                SoundtrackManager.setCurrent(new SoundFile("minigames.lobby", 177450), true);
            }

            // disabling proffers pit
            possibleGames.remove(Game.PROFFERS_PIT);

            ScoreManager.updateLobbyLeaderboard(totalScoreManager.getSortedDisplayList(ChatColor.AQUA, ChatColor.GREEN));
            for (Player p : Bukkit.getOnlinePlayers()) {
                ParticipantManager.addParticipant(p, p.getGameMode() == GameMode.ADVENTURE);
                p.sendTitle(ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + "Welcome", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "To TAq Minigames", 20, 100,20);
            }
        }
    }

    /**
     * Used to stop the event
     */
    public static void stop() {
        isRunning = false;
        SoundtrackManager.stopAllForAll();
        Trigger.unregisterAll();
        BossbarWrapper.destroyAll();
        Utilities.registerLobbyTrigger();
        ParticipantManager.teleportAllPlayers(Constants.LOBBY);
        gameIndex = 0;
        gameSelection = null;
        Bukkit.getOnlinePlayers().forEach(ScoreboardWrapper::removeScoreboard);
        ParticipantManager.resetAll();
        possibleGames = new ArrayList<>(Arrays.asList(Game.values()));
        ScoreManager.updateLobbyLeaderboard(new ArrayList<>());
        Utilities.fillAreaWithBlock(Constants.LOBBY_PODIUM_LOCATION[0], Constants.LOBBY_PODIUM_LOCATION[1], Material.AIR, null);
        gameIndex = 0;

    }

    /**
     * Used to start the next minigame
     * @param weighted Whether the list of possible games should be weighted
     */
    public static void nextMinigame(boolean weighted) {

        if (gameIndex > 0) {
            ScoreManager.increaseMultiplier();
            ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.YELLOW + "Emerald multiplier increased to " + ScoreManager.getScoreMultiplier() + "x!", " ");
        }
        for (Player p : ParticipantManager.getAll()) {
            p.getInventory().clear();
            p.sendTitle(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "Game " + (gameIndex + 1), ChatColor.AQUA + String.valueOf(ScoreManager.getScoreMultiplier()) + "x Emerald Multiplier", 10, 60, 10);
        }
        gameSelection = new GameSelection(possibleGames, weighted);
        gameIndex++;
    }

    /**
     * Used to start the next minigame and cause a specific game to be more likely to be selected
     * @param weighted Whether the list of possible games should be weighted
     * @param favor The game that should be more likely to be picked
     */
    public static void nextMinigame(boolean weighted, Game favor) {

        if (gameIndex > 0) {
            ScoreManager.increaseMultiplier();
            ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.YELLOW + "Emerald multiplier increased to " + ScoreManager.getScoreMultiplier() + "x!", " ");
        }
        for (Player p : ParticipantManager.getAll()) {
            p.getInventory().clear();
            p.sendTitle(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "Game " + (gameIndex + 1), ChatColor.AQUA + String.valueOf(ScoreManager.getScoreMultiplier()) + "x Emerald Multiplier", 10, 60, 10);
        }
        gameSelection = new GameSelection(possibleGames, weighted, favor);
        gameIndex++;
    }

    /**
     * Used to announce the winner of the event
     */
    public static void announceWinner() {
        UUID uuid = totalScoreManager.getFirst();
        Player winner = Bukkit.getPlayer(uuid);
        assert winner != null;
        for (Player p : ParticipantManager.getAll()) {
            p.teleport(Constants.LOBBY_WINNER_REST_LOCATION);
            p.sendTitle(ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + winner.getName(), ChatColor.DARK_AQUA + String.valueOf(ChatColor.BOLD) + "has won TAq Minigames", 20, 60, 20);

            String[] data = TAqMinigames.totalScoreManager.getScoreboardLines(p, ChatColor.AQUA, ChatColor.GREEN);
            ScoreboardWrapper.queueData(p, " ",
                    ChatColor.AQUA + "Thanks for playing!",
                    " ",
                    ChatColor.DARK_AQUA + "Your " + TAqMinigames.totalScoreManager.getDescriptor() + ": " + ChatColor.GREEN + TAqMinigames.totalScoreManager.getScore(p.getUniqueId()),
                    " ",
                    data[0],
                    " ",
                    data[1],
                    data[2],
                    data[3],
                    " "
            );
            ScoreboardWrapper.updateBoards();
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute in minecraft:minigames2 run clone " + Constants.LOBBY_PODIUM_TEMPLATE[0].getBlockX() + " " + Constants.LOBBY_PODIUM_TEMPLATE[0].getBlockY() + " " + Constants.LOBBY_PODIUM_TEMPLATE[0].getBlockZ() + " " + Constants.LOBBY_PODIUM_TEMPLATE[1].getBlockX() + " " + Constants.LOBBY_PODIUM_TEMPLATE[1].getBlockY() + " " + Constants.LOBBY_PODIUM_TEMPLATE[1].getBlockZ() + " " + Constants.LOBBY_PODIUM_LOCATION[0].getBlockX() + " " + Constants.LOBBY_PODIUM_LOCATION[0].getBlockY() + " " + Constants.LOBBY_PODIUM_LOCATION[0].getBlockZ());
        winner.teleport(Constants.LOBBY_WINNER_LOCATION);
        winner.getInventory().setHelmet(Utilities.getVictoryCrown());
        for (int i = 0; i < 3; i++) {
            Color color = i == 0 ? Color.BLUE : i == 1 ? Color.TEAL : Color.AQUA;
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
                for (Location loc : Constants.LOBBY_WINNER_FIREWORK_LOCATIONS) {
                    Firework firework = (Firework) Objects.requireNonNull(loc.getWorld()).spawnEntity(loc, EntityType.FIREWORK, false);
                    FireworkMeta fwm = firework.getFireworkMeta();
                    fwm.setPower(1);
                    fwm.addEffect(FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.BALL).withTrail().build());
                    firework.setFireworkMeta(fwm);
                    firework.addScoreboardTag("m_firework");
                    Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), firework::detonate, 15L);
                }
            }, 10 * i);
        }
    }

    /**
     * Used to register all listeners
     */
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
        Bukkit.getPluginManager().registerEvents(new PlayerBreakBlock(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryClick(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractWithEntity(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerItemConsume(), plugin);
        Bukkit.getPluginManager().registerEvents(new VehicleExit(), plugin);
        Bukkit.getPluginManager().registerEvents(new VehicleEntityCollision(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerSwapHandItem(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerDisconnect(), plugin);

    }

    /**
     * Getter for the plugin instance
     * @return The plugin
     */
    public static TAqMinigames getPlugin() {
        return plugin;
    }

    /**
     * Used to initialise all loops
     */
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

    /**
     * Used to see if the game is running
     * @return True if it is, false if it isn't
     */
    public static boolean isRunning() {
        return isRunning;
    }

    /**
     * Utility method used to get the correct game object from the game enum
     * @param game The game
     */
    public static void parseMinigame(Game game) {
        switch (game) {
            case AURA_AND_VOLLEY -> TAqMinigames.minigame = new AuraAndVolley();
            case AVOS_RACE -> TAqMinigames.minigame = new AvosRace();
            case NESAAK_SNOWBALL_FIGHT -> TAqMinigames.minigame = new NesaakFight();
            case PROFFERS_PIT -> TAqMinigames.minigame = new ProffersPit();
            case EXCAVATION -> TAqMinigames.minigame = new ExcavationSiteE();
            case CART_RACING -> TAqMinigames.minigame = new AledarCartRacing();
            case SKY_ISLAND_LOOTRUN -> TAqMinigames.minigame = new SkyIslandLootrun();
            case NETHER_PVP -> TAqMinigames.minigame = new NetherPvP();
        }
    }

}
