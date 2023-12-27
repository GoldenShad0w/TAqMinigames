package goldenshadow.taqminigames.event;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.SoundFile;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import goldenshadow.taqminigames.util.Constants;
import goldenshadow.taqminigames.util.Utilities;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class used to manage the selection of a new game
 */
public class GameSelection {

    private final List<Game> possibleGames;
    private int tick = 0;
    private final List<Player> participants;
    private int selectionState = 0;
    private final Game[] chosenGames = new Game[3];
    private Game actualNextgame;
    private final boolean weighted;
    private final Game favor;

    /**
     * This constructor is called when a new game should be selected
     * @param possibleGames The list of possible games to be selected from. It should never have less than 3 entries
     * @param weighted Whether the game selection should suggest more short games at the beginning and longer games at the end
     */
    public GameSelection(List<Game> possibleGames, boolean weighted) {
        this.possibleGames = new ArrayList<>(possibleGames);
        participants = ParticipantManager.getAll();
        this.weighted = weighted;
        favor = null;
    }

    /**
     * This constructor is called when a new game should be selected
     * @param possibleGames The list of possible games to be selected from. It should never have less than 3 entries
     * @param weighted Whether the game selection should suggest more short games at the beginning and longer games at the end
     * @param favor A specific game that should be forced to be suggested and if not voted out, forced to be picked
     */
    public GameSelection(List<Game> possibleGames, boolean weighted, Game favor) {
        this.possibleGames = new ArrayList<>(possibleGames);
        participants = ParticipantManager.getAll();
        this.weighted = weighted;
        this.favor = favor;
        if (!possibleGames.contains(favor)) possibleGames.add(favor);
    }


    /**
     * The tick loop which moves the game selection forward. Should be called every second
     */
    public void tick() {
        switch (tick) {
            case 0 -> {
                SoundtrackManager.setCurrent(new SoundFile("soundtrack.game_selection", 1), false);
                for (Player p : participants) {
                    p.teleport(Constants.LOBBY_GAME_SELECTION);
                    String raw = "The next minigame is about to be selected! Three possible options will be chosen randomly and you will be able to vote for the one you want to play. The game with the least amount of votes will be eliminated for this round. One of the two remaining games will be picked randomly!";

                    List<String> list = new ArrayList<>(Arrays.asList(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "Game Selection", " "));

                    for (String s : Utilities.splitString(raw, 50)) {
                        list.add(ChatColor.YELLOW + s);
                    }
                    ChatMessageFactory.sendInfoMessageBlock(p, list.toArray(String[]::new));
                }
            }
            case 5, 9, 13 -> {
                for (Player p : participants) {
                    p.sendTitle(" ", ChatColor.YELLOW + "Choosing possible game.", 5, 25, 0);
                    p.playSound(p, Sound.BLOCK_DISPENSER_FAIL, 1,1);
                }
            }
            case 6, 10, 14 -> {
                for (Player p : participants) {
                    p.sendTitle(" ", ChatColor.YELLOW + "Choosing possible game..", 5, 25, 0);
                    p.playSound(p, Sound.BLOCK_DISPENSER_FAIL, 1,1);
                }
            }
            case 7, 11, 15 -> {
                for (Player p : participants) {
                    p.sendTitle(" ", ChatColor.YELLOW + "Choosing possible game...", 5, 25, 0);
                    p.playSound(p, Sound.BLOCK_DISPENSER_FAIL, 1,1);
                }
            }
            case 8, 12, 16 -> {

                switch (selectionState) {
                    case 0 -> {
                        Game game = getPossibleGame();
                        participants.forEach(p -> p.sendTitle(" ", ChatColor.RED + game.getLabel(), 5, 10, 5));
                        chosenGames[0] = game;
                        spawnFireWork(Constants.LOBBY_POSSIBLE_GAME_RED, Color.RED);
                        spawnTextDisplay(Constants.LOBBY_POSSIBLE_GAME_RED, ChatColor.AQUA + "Possible Game:\n" + ChatColor.RED + ChatColor.BOLD + game.getLabel());
                        selectionState++;
                    }
                    case 1 -> {
                        Game game = getPossibleGame();
                        participants.forEach(p -> p.sendTitle(" ", ChatColor.GREEN + game.getLabel(), 5, 10, 5));
                        chosenGames[1] = game;
                        spawnFireWork(Constants.LOBBY_POSSIBLE_GAME_LIME, Color.LIME);
                        spawnTextDisplay(Constants.LOBBY_POSSIBLE_GAME_LIME, ChatColor.AQUA + "Possible Game:\n" + ChatColor.GREEN + ChatColor.BOLD + game.getLabel());
                        selectionState++;
                    }
                    case 2 -> {
                        Game game = getPossibleGame();
                        participants.forEach(p -> p.sendTitle(" ", ChatColor.BLUE + game.getLabel(), 5, 10, 5));
                        chosenGames[2] = game;
                        spawnFireWork(Constants.LOBBY_POSSIBLE_GAME_BLUE, Color.BLUE);
                        spawnTextDisplay(Constants.LOBBY_POSSIBLE_GAME_BLUE, ChatColor.AQUA + "Possible Game:\n" + ChatColor.BLUE + ChatColor.BOLD + game.getLabel());
                        selectionState++;
                    }
                }
            }
            case 17 -> {
                for (Player p : participants) {
                    ChatMessageFactory.sendInfoMessageBlock(p, ChatColor.YELLOW + "The possible games are:",  ChatColor.RED + String.valueOf(ChatColor.BOLD) + chosenGames[0].getLabel(), ChatColor.GREEN + String.valueOf(ChatColor.BOLD) + chosenGames[1].getLabel(), ChatColor.BLUE + String.valueOf(ChatColor.BOLD) + chosenGames[2].getLabel(), " ", ChatColor.YELLOW + "Stand on the platform of the game", ChatColor.YELLOW + "you want to play!");
                }
            }
            case 27, 28, 29 -> {
                for (Player p : participants) {
                    String s = tick == 27 ? "." : tick == 28 ? ".." : "...";
                    p.sendTitle(" ", ChatColor.YELLOW + "Counting players" + s, 5,25,0);
                    p.playSound(p, Sound.BLOCK_DISPENSER_FAIL, 1,1);
                }
            }
            case 30 -> {
                VoteColor leastVotes = countVotes();
                Material block = leastVotes == VoteColor.RED ? Material.RED_CONCRETE : leastVotes == VoteColor.LIME ? Material.LIME_CONCRETE : Material.BLUE_CONCRETE;
                Utilities.fillAreaWithBlock(Constants.LOBBY_SELECTION_AREA[0], Constants.LOBBY_SELECTION_AREA[1], Material.STRUCTURE_VOID, block);
                Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> Utilities.fillAreaWithBlock(Constants.LOBBY_SELECTION_AREA[0], Constants.LOBBY_SELECTION_AREA[1], block, Material.STRUCTURE_VOID), 200L);


                Game game = chosenGames[leastVotes.ordinal()];
                chosenGames[leastVotes.ordinal()] = null;

                for (Player p : participants) {
                    p.playSound(p, Sound.ENTITY_WITHER_BREAK_BLOCK, 0.5f,1);
                    ChatMessageFactory.sendInfoMessageBlock(p,  ChatColor.YELLOW + game.getLabel() + " was eliminated!");
                }
            }
            case 31, 32, 33 -> {
                for (Player p : participants) {
                    String s = tick == 31 ? "." : tick == 32 ? ".." : "...";
                    p.sendTitle(" ", ChatColor.YELLOW + "Picking next game" + s, 5,20,0);
                    p.playSound(p, Sound.BLOCK_DISPENSER_FAIL, 1,1);
                }
            }
            case 34 -> {
                boolean chosen = false;
                if (favor != null) {
                    for (Game g : chosenGames) {
                        if (g == favor) {
                            actualNextgame = g;
                            chosen = true;
                        }
                    }
                }
                if (!chosen) {
                    do {
                        actualNextgame = chosenGames[ThreadLocalRandom.current().nextInt(0, chosenGames.length)];
                    } while (actualNextgame == null);
                }
                for (Player p : participants) {
                    p.sendTitle(" ", ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + actualNextgame.getLabel(), 5, 40, 5);
                    p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 1,1);
                    ChatMessageFactory.sendInfoMessageBlock(p,  ChatColor.YELLOW + "The next minigame will be:", ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + actualNextgame.getLabel());
                }
            }
            case 35, 37, 38, 39 -> {

                if (tick == 35) {
                    Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Next minigame will start in 5 seconds!"));
                }
                else if (tick == 37) {
                    Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Next minigame will start in 3 seconds!"));
                }
                else if (tick == 38) {
                    Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Next minigame will start in 2 seconds!"));
                }
                else {
                    Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Next minigame will start in 1 second!"));
                }

            }
            case 40 -> {
                Bukkit.broadcastMessage(ChatMessageFactory.singleLineInfo("Teleporting..."));
                TAqMinigames.possibleGames.remove(actualNextgame);
                TAqMinigames.parseMinigame(actualNextgame);
            }
        }
        tick++;
    }


    /**
     * Internal method used to spawn a text display showing a possible minigame
     * @param location The location it should be spawned at
     * @param text The text it should contain. New lines can be added with "\n"
     */
    private void spawnTextDisplay(Location location, String text) {
        TextDisplay textDisplay = (TextDisplay) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.TEXT_DISPLAY, false);
        textDisplay.setText(text);
        textDisplay.setBillboard(Display.Billboard.CENTER);
        Transformation transformation = textDisplay.getTransformation();
        transformation.getScale().set(new Vector3f(0.1f,0.1f,0.1f));
        textDisplay.setTransformation(transformation);
        textDisplay.setInterpolationDelay(-1);
        textDisplay.setInterpolationDuration(40);

        Transformation newTransformation = textDisplay.getTransformation();
        newTransformation.getScale().set(new Vector3f(1f,1f,1f));
        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> textDisplay.setTransformation(newTransformation), 1L);



        Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), textDisplay::remove, 640L);
    }

    /**
     * Internal method used to spawn a colorful firework with doesn't deal damage
     * @param location The location it should be spawned at
     * @param color The color the explosion should have
     */
    private void spawnFireWork(Location location, Color color) {
        Firework firework = (Firework) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.FIREWORK, false);
        FireworkMeta fwm = firework.getFireworkMeta();
        fwm.addEffect(FireworkEffect.builder().withColor(color).with(FireworkEffect.Type.BALL).withTrail().build());
        firework.setFireworkMeta(fwm);
        firework.addScoreboardTag("m_firework");
        firework.detonate();
    }

    /**
     * Used to get a possible game for the list of possible games
     * @return A possible game
     * @throws RuntimeException Throws an exception if the list is empty. To prevent this, never call the constructor of this class with a list that has a size smaller than 3
     */
    private Game getPossibleGame() {
        if (possibleGames.isEmpty()) throw new RuntimeException("List of possible games was empty! This should never happen. If there are less than 8 games in total then you need to re-enable some!");
        if (favor != null && possibleGames.contains(favor)) {
            possibleGames.remove(favor);
            return favor;
        }
        Game game;
        if (weighted) {
            List<Game> list = Game.getWeightedList(TAqMinigames.gameIndex, possibleGames);
            if (list.size() > 1) {
                game = list.get(ThreadLocalRandom.current().nextInt(0, list.size()));
            } else {
                game = list.get(0);
            }
        } else {
            game = possibleGames.get(ThreadLocalRandom.current().nextInt(0, possibleGames.size()));
        }
        if (game != null) possibleGames.remove(game);
        return game;
    }

    /**
     * Used to count to votes
     * @return The game index with the least amount of votes. 0 for blue, 1 for red and 2 for lime
     */
    private VoteColor countVotes() {
        int blueVotes = 0;
        int redVotes = 0;
        int limeVotes = 0;
        for (Player p : ParticipantManager.getParticipants()) {
            if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BLUE_CONCRETE) {
                blueVotes++;
                continue;
            }
            if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.RED_CONCRETE) {
                redVotes++;
                continue;
            }
            if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.LIME_CONCRETE) {
                limeVotes++;
            }
        }
        int lowest = Math.min(Math.min(blueVotes, redVotes), limeVotes);
        if (blueVotes == lowest) {
            return VoteColor.BLUE;
        }
        if (limeVotes == lowest) {
            return VoteColor.LIME;
        }
        return VoteColor.RED;
    }

    /**
     * Used to check if tick() should continue being called
     * @return True if it should still be called, false if the game selection is done
     */
    public boolean isInProgress() {
        return tick < 41;
    }

    public enum VoteColor {
        RED,
        LIME,
        BLUE
    }

}
