package goldenshadow.taqminigames.commands;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.minigames.AuraAndVolley;
import goldenshadow.taqminigames.util.ChatMessageFactory;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("hosting")) {
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("start")) {
                            if (!TAqMinigames.isRunning()) {
                                TAqMinigames.start();
                                player.sendMessage(ChatMessageFactory.adminInfoMessage("Starting the event!"));
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
                        if (args[1].equalsIgnoreCase("next_minigame")) {
                            if (TAqMinigames.isRunning()) {
                                if (TAqMinigames.gameIndex < 5) {
                                    if (TAqMinigames.minigame == null) {
                                        TAqMinigames.nextMinigame();
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
                    }
                }
                if (args[0].equalsIgnoreCase("debug")) {
                    if (args.length > 1) {
                        if (args[1].equalsIgnoreCase("start_game")) {
                            if (args.length == 3) {
                                if (TAqMinigames.isRunning() && TAqMinigames.minigame == null) {
                                    if (Game.contains(args[2])) {
                                        Game game = Game.valueOf(args[2]);
                                        switch (game) {
                                            case AURA_AND_VOLLEY -> TAqMinigames.minigame = new AuraAndVolley();
                                            //TODO fill with more
                                        }
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
                    }
                }
            }
        }
        return false;
    }
}
