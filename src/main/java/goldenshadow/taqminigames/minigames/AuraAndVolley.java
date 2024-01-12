package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.GameState;
import goldenshadow.taqminigames.event.BossbarWrapper;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreManager;
import goldenshadow.taqminigames.event.SoundtrackManager;
import goldenshadow.taqminigames.minigames.aura_and_volley.Attack;
import goldenshadow.taqminigames.minigames.aura_and_volley.Aura;
import goldenshadow.taqminigames.minigames.aura_and_volley.Volley;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Timer;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class representing the aura and volley minigame
 */
public class AuraAndVolley extends Minigame{

    public List<Player> alivePlayers;
    public UUID barUUID;

    private final List<Attack> activeAttacks = new ArrayList<>();

    private static final String[] deathMessages = {" skill issued!"," forgot to jump!"," is not a pro warrer!", " won't be on the next HQ team!", " died!", " forgot to dodge!"};
    private int round = 0;

    /**
     * Used to initialise the game
     */
    public AuraAndVolley() {
        alivePlayers = ParticipantManager.getParticipants();
        SoundtrackManager.stopAllForAll();
        gameState = GameState.STARTING;
        scoreManager = new ScoreManager("Emeralds", true);
        timer = new Timer(0, 29, () -> timer = new Timer(2,59, this::endRound));
        for (Player player : alivePlayers) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, true, false, false));
        }
        ParticipantManager.teleportAllPlayers(Constants.AURA_TUTORIAL_LOCATION);
    }

    @Override
    public void fastTick() {
        Iterator<Attack> it = activeAttacks.iterator();
        while (it.hasNext()) {
            Attack a = it.next();
            a.tick();
            if (a.isDone()) {
                it.remove();
            }
        }
        super.fastTick();
    }

    @Override
    public void tick() {
        ParticipantManager.getAll().forEach(this::updateScoreboard);
        switch (tick) {
            case 0 -> ChatMessageFactory.sendInfoBlockToAll(" ", ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Aura & Volley", " ");
            case 4 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("The goal of this minigame is to dodge the towers deadly attacks", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 8 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Each time the tower attacks, it will either cast aura or volley", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 12 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("Aura can be dodged by jumping over it and looks like this...", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 13 -> {
                ParticipantManager.getAll().forEach(p -> {
                    p.sendTitle("", ChatColor.DARK_RED + String.valueOf(ChatColor.UNDERLINE) + "/!\\" + ChatColor.RESET + ChatColor.GRAY + " Tower" + ChatColor.GOLD + " Aura", 20, 20, 20);
                    p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL,SoundCategory.VOICE, 1,1);
                });
                activeAttacks.add(new Aura(Constants.AURA_TOWER_CENTERS[0]));

            }
            case 16 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("And volley is an attack from the sky that needs to be avoided by leaving the area it is targeting. It looks like this...", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 17 -> {
                ParticipantManager.getAll().forEach(p -> {
                    p.sendTitle("", ChatColor.DARK_RED + String.valueOf(ChatColor.UNDERLINE) + "/!\\" + ChatColor.RESET + ChatColor.GRAY + " Tower" + ChatColor.LIGHT_PURPLE + " Volley", 20, 20, 20);
                    p.playSound(p, Sound.ENTITY_WITHER_SPAWN,SoundCategory.VOICE, 1,1);
                });
                activeAttacks.add(new Volley(Constants.AURA_MAP_LOCATIONS[0]));
            }
            case 20 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("There are three rounds. During each round, the attacks will slowly get faster and your jump height will decrease", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 24 -> ChatMessageFactory.sendInfoBlockToAll(Utilities.colorList(Utilities.splitString("You gain " + ((int) (Constants.AURA_SURVIVE * ScoreManager.getScoreMultiplier())) + " Emeralds every time someone dies", 50), ChatColor.YELLOW).toArray(String[]::new));
            case 25 -> {
                initRound();
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
                }
                gameState = GameState.RUNNING;
            }
            default -> {
                if (gameState == GameState.RUNNING) {
                    if (tick > 30) {

                        if (timer.getSecondsLeft() >= 150) {
                            BossbarWrapper.updateTitle(barUUID,ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "Jump Height decrease in " + (timer.getSecondsLeft() - 150) + " seconds " + ChatColor.AQUA + ChatColor.BOLD + "[VI -> III]");
                            BossbarWrapper.updateProgress(barUUID,((double) (timer.getSecondsLeft() - 150)) / 30);
                        } else if (timer.getSecondsLeft() >= 80) {
                            BossbarWrapper.updateTitle(barUUID,ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "Jump Height decrease in " + (timer.getSecondsLeft() - 80) + " seconds " + ChatColor.AQUA + ChatColor.BOLD + "[III -> None]");
                            BossbarWrapper.updateProgress(barUUID,((double) (timer.getSecondsLeft() - 80)) / 70);
                        }


                        //jump height decay
                        if (timer.getSecondsLeft() == 150) {
                            alivePlayers.forEach(p -> {
                                p.removePotionEffect(PotionEffectType.JUMP);
                                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, 2, true, false, true));
                                ChatMessageFactory.sendInfoMessageBlock(p, ChatColor.YELLOW + "Your jump height has decreased and the tower has gotten stronger!");
                            });
                        } else if (timer.getSecondsLeft() == 80) {
                            alivePlayers.forEach(p -> {
                                p.removePotionEffect(PotionEffectType.JUMP);
                                ChatMessageFactory.sendInfoMessageBlock(p, ChatColor.YELLOW + "Your jump height has run out and the tower has gotten stronger!");
                            });
                            BossbarWrapper.destroyBossbar(barUUID);
                        }

                        //Wave timings
                        if (timer.getSecondsLeft() > 150) {
                            if (tick == 32 || tick == 34) {
                                cast();
                            }

                        } else if (timer.getSecondsLeft() > 80) {
                            if (tick == 33 || tick == 35 || tick == 36) {
                                cast();
                            }
                        } else {
                            if (tick == 32 || tick == 33 || tick == 34 || tick == 35 || tick == 36) {
                                cast();
                            }
                        }
                        if (tick >= 37) {
                            tick = 31;
                        }
                    }
                }
            }
        }
        super.tick();
    }

    /**
     * Used to initialize every new round
     */
    private void initRound() {
        if (round < 3) {

            alivePlayers = ParticipantManager.getParticipants();
            ParticipantManager.teleportAllPlayers(Constants.AURA_MAP_LOCATIONS[round]);

            BossbarWrapper.destroyAll();
            barUUID = BossbarWrapper.createBossbar(ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + "Jump Height decrease in 30 seconds " + ChatColor.AQUA + ChatColor.BOLD + "[VI -> III]", BarColor.GREEN, BarStyle.SOLID, 1);

            for (Player p : ParticipantManager.getParticipants()) {
                p.setGameMode(GameMode.ADVENTURE);
                p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, PotionEffect.INFINITE_DURATION, 5, true, false, true));
            }
            round++;
            ParticipantManager.getAll().forEach(p -> ChatMessageFactory.sendInfoMessageBlock(p, ChatColor.YELLOW + "Round " + round));
        }
    }

    /**
     * Internal method used to cast an aura
     */
    private void castAura() {
        ParticipantManager.getAll().forEach(p -> {
            p.sendTitle("", ChatColor.DARK_RED + String.valueOf(ChatColor.UNDERLINE) + "/!\\" + ChatColor.RESET + ChatColor.GRAY + " Tower" + ChatColor.GOLD + " Aura", 20, 20, 20);
            p.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL,SoundCategory.VOICE, 1,1);
        });
        activeAttacks.add(new Aura(Constants.AURA_TOWER_CENTERS[round-1]));
    }

    /**
     * Internal method used to cast a volley
     */
    private void castVolley() {
        ParticipantManager.getAll().forEach(p -> {
            p.sendTitle("", ChatColor.DARK_RED + String.valueOf(ChatColor.UNDERLINE) + "/!\\" + ChatColor.RESET + ChatColor.GRAY + " Tower" + ChatColor.LIGHT_PURPLE + " Volley", 20, 20, 20);
            p.playSound(p, Sound.ENTITY_WITHER_SPAWN,SoundCategory.VOICE, 1,1);
        });
        Player[] targets = {alivePlayers.get(ThreadLocalRandom.current().nextInt(0, alivePlayers.size())),alivePlayers.get(ThreadLocalRandom.current().nextInt(0, alivePlayers.size())),alivePlayers.get(ThreadLocalRandom.current().nextInt(0, alivePlayers.size()))};
        for (Player player : targets){
            Location location = player.getLocation();
            while (location.getBlock().getRelative(BlockFace.DOWN).isEmpty() && location.getY() > -50) {
                location = location.add(0, -1,0);
            }
            activeAttacks.add(new Volley(location));
        }
    }

    /**
     * Called when a player gets hit by an aura or volley
     * @param player The players who died
     */
    @Override
    public void onDeath(Player player) {
        alivePlayers.remove(player);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_DEATH,SoundCategory.VOICE, 1,1);
        player.sendMessage(ChatMessageFactory.singleLineInfo("You died..."));
        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + deathMessages[ThreadLocalRandom.current().nextInt(0, deathMessages.length)]);
        for (Player p : alivePlayers) {
            scoreManager.increaseScore(p, Constants.AURA_SURVIVE / Math.max(ParticipantManager.getParticipants().size()-1, 1), "A player has died!" ,true);
        }
        player.setGameMode(GameMode.SPECTATOR);
        if (alivePlayers.size() <= 1) {
            if (alivePlayers.size() == 1) {
                scoreManager.increaseScore(alivePlayers.get(0), Constants.AURA_WIN, "You are the last person alive!", true);
                ChatMessageFactory.sendInfoBlockToAll(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "Winner of this round:", ChatColor.YELLOW + alivePlayers.get(0).getName());

            }
            if (gameState == GameState.RUNNING) {
                timer.runTaskEarly();
            }
        }
    }

    /**
     * Internal method used to cast a random attack.
     */
    private void cast() {
        int r = ThreadLocalRandom.current().nextInt(0, 3);
        if (r == 0) castVolley();
        else castAura();
    }

    /**
     * Used to insert a player into the game
     * @param player The player who should be inserted
     */
    @Override
    public void insertPlayer(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(Constants.AURA_MAP_LOCATIONS[round-1]);
        player.sendMessage(ChatMessageFactory.singleLineInfo("You will be able to join after this round has ended!"));
    }

    /**
     * Used to update the scoreboard of a player
     * @param player The player whose scoreboard that should be updated
     */
    @Override
    protected void updateScoreboard(Player player) {
        super.updateScoreboard(player);
    }

    /**
     * Used to end the minigame
     */
    @Override
    public void end() {
        ChatMessageFactory.sendInfoBlockToAll(ChatColor.YELLOW + "Round over!");
        for (Player p : ParticipantManager.getParticipants()) {
            p.removePotionEffect(PotionEffectType.JUMP);
        }
        super.end();
    }

    /**
     * Used to end a round
     */
    private void endRound() {
        gameState = GameState.STARTING;
        if (round == 1) {
            timer = new Timer(0, 10, () -> timer = new Timer(3,0, this::endRound));
        } else if (round == 2) {
            timer = new Timer(0, 10, () -> timer = new Timer(3,0, this::end));
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> tick = 25, 120L);
    }

    /**
     * Used to re-insert a player who reconnected
     * @param player The player who reconnected
     */
    @Override
    public void playerReconnect(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
    }

    /**
     * Used to correctly handle a player disconnecting
     * @param player The player
     */
    @Override
    public void playerDisconnect(Player player) {
        alivePlayers.remove(player);
    }

    @Override
    public Game getGame() {
        return Game.AURA_AND_VOLLEY;
    }
}
