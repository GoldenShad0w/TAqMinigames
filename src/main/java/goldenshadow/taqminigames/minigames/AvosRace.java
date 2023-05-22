package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Timer;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;


public class AvosRace extends Minigame implements Listener {

    private int finishedPlayers = 0;
    private final int[] stageCompletions = {0,0,0,0,0};
    private final HashMap<UUID, Integer> playerStages = new HashMap<>();
    private static final String[] deathMessages = {" flew into a wall!", " needs their flying licence revoked!", " thought they could fly though walls!", " has their eyes closed!", " learned that the wall was stronger than them!", " skill issued!", " has a serious case of skill issue!", " crash landed", " overestimated themselves", " didn't make it!", " needs some flying lessons!"};
    private final int mapIndex;
    private final ScoreManager scoreManager;

    public AvosRace() {
        mapIndex = TAqMinigames.getPlugin().getConfig().getInt("avos-race-next-map");
        if (mapIndex + 1 > TAqMinigames.getPlugin().getConfig().getInt("avos-race-maps")) {
            TAqMinigames.getPlugin().getConfig().set("avos-race-next-map", 1);
        } else TAqMinigames.getPlugin().getConfig().set("avos-race-next-map", mapIndex + 1);
        TAqMinigames.getPlugin().saveConfig();

        assert Constants.WORLD != null;
        Constants.WORLD.setGameRule(GameRule.FALL_DAMAGE, true);

        scoreManager = new ScoreManager("Emeralds");
        gameState = GameState.STARTING;

        for (Player p : ParticipantManager.getParticipants()) {
            playerStages.put(p.getUniqueId(), 0);
        }

        timer = new Timer(0, 30, () -> timer = new Timer(10,0, this::end));

        ParticipantManager.teleportAllPlayers(Constants.AVOS_MAP_LOCATIONS[mapIndex-1]);

    }

    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Avos Race", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to reach the end of this track as fast as possible", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Hitting a wall or landing before you reach the end will reset you to the beginning", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The track is split up into multiple stages. Completing a stage and finishing will earn you emeralds", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 14 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Completing a stage can give you up to " + (Constants.AVOS_STAGE_COMPLETE * ScoreManager.getScoreMultiplier()) + " emeralds, with the reward decreasing by " + (Constants.AVOS_FALLOFF * ScoreManager.getScoreMultiplier()) + " emeralds every time someone else completes the stage before you", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 18 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Finishing will earn you " + (Constants.AVOS_FINISH * ScoreManager.getScoreMultiplier()) + " emeralds, with the same decrease as the stage completion reward", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 22 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Remember that you have you to jump mid-air to open your elytra", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Once you are gliding, there only way to regain momentum is to glide downhill", 50), ChatColor.YELLOW).toArray(String[]::new));
        }
        super.tick();
    }

    @Override
    public void onDeath(Player player) {

    }

    @Override
    public void playerReconnect(Player player) {

    }

    @Override
    public Game getGame() {
        return Game.AVOS_RACE;
    }

    public boolean isHotFloor(Player player) {
        return false;
    }


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
    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        if (gameState == GameState.RUNNING) {
            Player p = event.getPlayer();
            if (ParticipantManager.getParticipants().contains(p)) {
                if (playerStages.containsKey(p.getUniqueId())) {
                    if (Utilities.isInBoundingBox(Constants.AVOS_STAGES[mapIndex - 1][playerStages.get(p.getUniqueId())], p.getLocation())) {
                        stageComplete(p);
                    }
                }
            }
        }
    }

    private void stageComplete(Player player) {
        int oldStage = playerStages.get(player.getUniqueId());
        int placement = stageCompletions[oldStage] + 1;
        scoreManager.increaseScore(player, Math.max(Constants.AVOS_STAGE_COMPLETE - ((placement-1) * Constants.AVOS_FALLOFF), 100), placement + Utilities.getNumberSuffix(placement) + " to complete this stage!", true);
        stageCompletions[oldStage] = placement;
        playerStages.put(player.getUniqueId(), oldStage+1);
    }

    @Override
    public void end() {

        super.end();
    }
}
