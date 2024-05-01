package goldenshadow.taqminigames.commands;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.data.dataconfig.ConfigManager;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.enums.SoundFile;
import goldenshadow.taqminigames.event.ParticipantManager;
import goldenshadow.taqminigames.event.ScoreboardWrapper;
import goldenshadow.taqminigames.event.SoundtrackManager;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("hosting")) {
                    if (args.length >= 2) {
                        if (args[1].equalsIgnoreCase("start")) {
                            if (!TAqMinigames.isRunning()) {
                                if (Game.values().length - TAqMinigames.getEventConfig().getDisabledGames().size() >= 7) {
                                    TAqMinigames.start();
                                    player.sendMessage(ChatMessageFactory.adminInfoMessage("Starting the event!"));
                                    return true;
                                }
                                player.sendMessage(ChatMessageFactory.adminErrorMessage("You need to have at least 7 games enabled to run the event!"));
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminErrorMessage("You cannot start the event while it is already running!"));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("stop")) {
                            if (TAqMinigames.isRunning()) {
                                TAqMinigames.stop();
                                player.sendMessage(ChatMessageFactory.adminInfoMessage("Stopped TAq Minigames!"));
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminErrorMessage("The event is currently not running!"));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("game_config")) {
                            if (!TAqMinigames.isRunning()) {
                                player.openInventory(ConfigManager.getGUI());
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminErrorMessage("You cannot change the game config while a game is running!"));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("announce_winner")) {
                            if (TAqMinigames.isRunning()) {
                                TAqMinigames.announceWinner();
                                player.sendMessage(ChatMessageFactory.adminInfoMessage("Running winner sequence!"));
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminErrorMessage("The event is currently not running!"));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("toggle_prestart")) {
                            TAqMinigames.inPreStartPhase = !TAqMinigames.inPreStartPhase;
                            player.sendMessage(ChatMessageFactory.adminInfoMessage("Toggled pre start phase to " + TAqMinigames.inPreStartPhase + "!"));
                            if (TAqMinigames.inPreStartPhase) {
                                SoundtrackManager.setCurrent(new SoundFile("minigames.lobby", 177450), true);
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    ScoreboardWrapper.queueData(p,
                                            " ",
                                            ChatColor.AQUA + "Starting soon!");
                                    ScoreboardWrapper.updateBoards();
                                }
                            } else {
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    ScoreboardWrapper.removeScoreboard(p);
                                }
                            }
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("next_minigame")) {
                            if (TAqMinigames.isRunning()) {
                                if (TAqMinigames.gameIndex < 5) {
                                    if (TAqMinigames.minigame == null) {
                                        TAqMinigames.nextMinigame(true);
                                        player.sendMessage(ChatMessageFactory.adminInfoMessage("Selecting next minigame!"));
                                        return true;
                                    }
                                    player.sendMessage(ChatMessageFactory.adminErrorMessage("You cannot start a new minigame while another is already running!"));
                                    return true;
                                }
                                player.sendMessage(ChatMessageFactory.adminErrorMessage("All 5 minigames for this event have been played!"));
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminErrorMessage("The game must be running in order to start a new minigame!"));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("next_minigame_favor")) {
                            if (TAqMinigames.isRunning()) {
                                if (TAqMinigames.gameIndex < 5) {
                                    if (TAqMinigames.minigame == null) {
                                        if (args.length == 3) {
                                            Game game = Game.valueOf(args[2]);
                                            if (TAqMinigames.possibleGames.contains(game)) {
                                                TAqMinigames.nextMinigame(true, game);
                                                player.sendMessage(ChatMessageFactory.adminInfoMessage("Selecting next minigame with favor!"));
                                                return true;
                                            }
                                        }
                                        player.sendMessage(ChatMessageFactory.adminErrorMessage("Invalid input!"));
                                        return true;
                                    }
                                    player.sendMessage(ChatMessageFactory.adminErrorMessage("You cannot start a new minigame while another is already running!"));
                                    return true;
                                }
                                player.sendMessage(ChatMessageFactory.adminErrorMessage("All 5 minigames for this event have been played!"));
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminErrorMessage("The game must be running in order to start a new minigame!"));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("insert_player")) {
                            if (TAqMinigames.isRunning()) {
                                if (args.length == 4) {
                                    Player p = Bukkit.getPlayer(args[3]);
                                    if (p != null) {
                                        if (!ParticipantManager.getAll().contains(p)) {
                                            if (args[2].equalsIgnoreCase("PARTICIPANT")) {
                                                ParticipantManager.addParticipant(p, true);
                                                p.sendMessage(ChatMessageFactory.singleLineInfo("You are now a participant! You will be able to play once the next minigame starts."));
                                                p.setGameMode(GameMode.SPECTATOR);
                                                player.sendMessage(ChatMessageFactory.adminInfoMessage("Inserted player as participant!"));
                                                return true;
                                            } else if (args[2].equalsIgnoreCase("SPECTATOR")) {
                                                ParticipantManager.addParticipant(p, false);
                                                p.sendMessage(ChatMessageFactory.singleLineInfo("You are now a spectator! Use the number keys to teleport to people."));
                                                player.sendMessage(ChatMessageFactory.adminInfoMessage("Inserted player as spectator!"));
                                                p.setGameMode(GameMode.SPECTATOR);
                                                return true;
                                            } else {
                                                player.sendMessage(ChatMessageFactory.adminErrorMessage("Invalid role!"));
                                                return true;
                                            }
                                        }
                                    } else {
                                        player.sendMessage(ChatMessageFactory.adminErrorMessage("Not a valid player!"));
                                        return true;
                                    }
                                }
                                player.sendMessage(ChatMessageFactory.adminErrorMessage("Invalid input!"));
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminErrorMessage("The game must be running to run this command!"));
                            return true;
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("debug")) {
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("start_game")) {
                            if (args.length == 3) {
                                if (TAqMinigames.isRunning() && TAqMinigames.minigame == null) {
                                    if (Game.contains(args[2])) {
                                        SoundtrackManager.stopAllForAll();
                                        Game game = Game.valueOf(args[2]);
                                        TAqMinigames.parseMinigame(game);
                                        return true;
                                    }
                                    player.sendMessage(ChatMessageFactory.adminUsageMessage("/minigames debug start_game <game>"));
                                    return true;
                                }
                                player.sendMessage(ChatMessageFactory.adminErrorMessage("The event must be running and no other minigame must be active to run this command!"));
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminUsageMessage("/minigames debug start_game <game>"));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("end_game")) {
                            if (TAqMinigames.isRunning() && TAqMinigames.minigame != null) {
                                TAqMinigames.minigame.end();
                                player.sendMessage(ChatMessageFactory.adminInfoMessage("Ended current game!"));
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminErrorMessage("The event must be running and a minigame must be in progress to run this command!"));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("add_points") || args[1].equalsIgnoreCase("set_points")) {
                            if (TAqMinigames.isRunning()) {
                                if (args.length == 4) {
                                    Player p = Bukkit.getPlayer(args[2]);
                                    if (p != null) {
                                        if (ParticipantManager.getParticipants().contains(p)) {
                                            if (isAnInt(args[3])) {
                                                if (args[1].equalsIgnoreCase("add_points")) {
                                                    if (TAqMinigames.minigame != null) {
                                                        TAqMinigames.minigame.scoreManager.increaseScore(p, Integer.parseInt(args[3]), false);
                                                    } else {
                                                        TAqMinigames.totalScoreManager.increaseScore(p, Integer.parseInt(args[3]), false);
                                                        String[] data = TAqMinigames.totalScoreManager.getScoreboardLines(player, ChatColor.AQUA, ChatColor.GREEN);
                                                        for (Player pl : ParticipantManager.getAll()) {
                                                            ScoreboardWrapper.queueData(pl, " ",
                                                                    ChatColor.AQUA + "The next game will start soon!",
                                                                    " ",
                                                                    ChatColor.DARK_AQUA + "Your " + TAqMinigames.totalScoreManager.getDescriptor() + ": " + ChatColor.GREEN + TAqMinigames.totalScoreManager.getScore(player.getUniqueId()),
                                                                    " ",
                                                                    data[0],
                                                                    " ",
                                                                    data[1],
                                                                    data[2],
                                                                    data[3],
                                                                    " "
                                                            );
                                                        }
                                                    }
                                                    player.sendMessage(ChatMessageFactory.adminInfoMessage("Added points!"));
                                                    return true;
                                                }
                                                if (args[1].equalsIgnoreCase("set_points")) {
                                                    if (TAqMinigames.minigame != null) {
                                                        TAqMinigames.minigame.scoreManager.setScore(p, Integer.parseInt(args[3]));
                                                    } else {
                                                        TAqMinigames.totalScoreManager.setScore(p, Integer.parseInt(args[3]));
                                                        String[] data = TAqMinigames.totalScoreManager.getScoreboardLines(player, ChatColor.AQUA, ChatColor.GREEN);
                                                        for (Player pl : ParticipantManager.getAll()) {
                                                            ScoreboardWrapper.queueData(pl, " ",
                                                                    ChatColor.AQUA + "The next game will start soon!",
                                                                    " ",
                                                                    ChatColor.DARK_AQUA + "Your " + TAqMinigames.totalScoreManager.getDescriptor() + ": " + ChatColor.GREEN + TAqMinigames.totalScoreManager.getScore(player.getUniqueId()),
                                                                    " ",
                                                                    data[0],
                                                                    " ",
                                                                    data[1],
                                                                    data[2],
                                                                    data[3],
                                                                    " "
                                                            );
                                                        }
                                                    }
                                                    player.sendMessage(ChatMessageFactory.adminInfoMessage("Set points!"));
                                                    return true;
                                                }
                                            } else {
                                                player.sendMessage(ChatMessageFactory.adminErrorMessage("Amount must be an integer!"));
                                                return true;
                                            }
                                        } else {
                                            player.sendMessage(ChatMessageFactory.adminErrorMessage("Target is not a participant!"));
                                            return true;
                                        }
                                    } else {
                                        player.sendMessage(ChatMessageFactory.adminErrorMessage("Not a valid player!"));
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(ChatMessageFactory.adminErrorMessage("Invalid input!"));
                                    return true;
                                }
                            } else {
                                player.sendMessage(ChatMessageFactory.adminErrorMessage("The game must be running to run this command!"));
                                return true;
                            }
                        }
                        if (args[1].equalsIgnoreCase("add_time")) {
                            if (TAqMinigames.isRunning()) {
                                if (args.length == 4) {
                                    if (isAnInt(args[2]) && isAnInt(args[3])) {
                                        if (TAqMinigames.minigame != null) {
                                            int m = Integer.parseInt(args[2]);
                                            int s = Integer.parseInt(args[3]);
                                            TAqMinigames.minigame.timer.addTime(m, s);
                                            player.sendMessage(ChatMessageFactory.adminInfoMessage("Added " + m + " minutes and " + s + " seconds to time!"));
                                            return true;
                                        }
                                        player.sendMessage(ChatMessageFactory.adminErrorMessage("There must be a minigame running!"));
                                        return true;
                                    }
                                }
                                player.sendMessage(ChatMessageFactory.adminErrorMessage("Syntax: /m debug add_time <minutes> <seconds>"));
                                return true;
                            }
                            player.sendMessage(ChatMessageFactory.adminErrorMessage("The event must be running to run this command!"));
                            return true;
                        }
                        if (args[1].equalsIgnoreCase("toggle_trigger_highlight")) {
                            TAqMinigames.debugTriggers = !TAqMinigames.debugTriggers;
                            player.sendMessage(ChatMessageFactory.adminInfoMessage("Set trigger highlight to " + TAqMinigames.debugTriggers + "!"));
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isAnInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
