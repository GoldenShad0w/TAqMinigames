package goldenshadow.taqminigames.commands;

import goldenshadow.taqminigames.enums.Game;
import goldenshadow.taqminigames.event.ParticipantManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TabComplete implements TabCompleter {

    List<String> arguments = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("TAqMinigames.admin")){
            List<String> result = new ArrayList<>();
            if (args.length == 1) {
                arguments = new ArrayList<>(Arrays.asList("hosting", "debug", "place"));
                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
            if (args.length == 2) {

                if (args[0].equalsIgnoreCase("hosting")) {
                    arguments = new ArrayList<>(Arrays.asList("start", "stop", "next_minigame", "insert_player"));
                } else if (args[0].equalsIgnoreCase("debug")) {
                    arguments = new ArrayList<>(Arrays.asList("add_points", "set_points", "start_game", "end_game"));
                }
                else arguments.clear();
                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[1].toLowerCase()))
                        result.add(a);
                }
                return result;
            }
            if (args.length == 3) {

                if (args[0].equalsIgnoreCase("hosting") && args[1].equalsIgnoreCase("insert_player")) {
                    arguments = Bukkit.getOnlinePlayers().stream().filter(x -> !ParticipantManager.getParticipants().contains(x)).map(Player::getName).collect(Collectors.toList());
                } else if (args[0].equalsIgnoreCase("debug")) {
                    if (args[1].equalsIgnoreCase("add_points") || args[1].equalsIgnoreCase("set_points")) {
                        arguments = ParticipantManager.getParticipants().stream().map(Player::getName).collect(Collectors.toList());
                    } else if (args[1].equalsIgnoreCase("start_game")) {
                        arguments = new ArrayList<>();
                        for (Game g : Game.values()) {
                            arguments.add(g.toString());
                        }
                    }

                }
                else arguments.clear();
                for (String a : arguments) {
                    if (a.toLowerCase().startsWith(args[2].toLowerCase()))
                        result.add(a);
                }
                return result;
            }

            if (args.length > 3) {
                arguments.clear();
                return arguments;
            }
        }

        return null;
    }
}
