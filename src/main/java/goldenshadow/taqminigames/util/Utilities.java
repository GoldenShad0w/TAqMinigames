package goldenshadow.taqminigames.util;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilities {


    /**
     * Used to fill a selection with a certain type of block. This should not be used for massive selections
     * @param pos1 One of the corners of the selection
     * @param pos2 The other corner of the selection
     * @param material The block that should be placed
     * @param replaceOnly The block that should be replaced. Null if all blocks should be replaced
     */
    public static void fillAreaWithBlock(Location pos1, Location pos2, Material material, Material replaceOnly) {
        if (pos1 == null || pos2 == null || pos1.getWorld() == null || pos2.getWorld() == null || !material.isBlock()) {
            return;
        }
        if (!Objects.equals(pos1.getWorld(), pos2.getWorld())) {
            return;
        }

        int x1 = pos1.getBlockX();
        int x2 = pos2.getBlockX();
        int y1 = pos1.getBlockY();
        int y2 = pos2.getBlockY();
        int z1 = pos1.getBlockZ();
        int z2 = pos2.getBlockZ();

        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2);
        int maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = pos1.getWorld().getBlockAt(x, y, z);
                    if (replaceOnly == null || block.getType() == replaceOnly) {
                        block.setType(material);
                    }
                }
            }
        }
    }

    /**
     * A utility method used to split a String into small ones. Words will not be split
     * @param raw The string that should be split
     * @param maxChars The max amount of char each split should have
     * @return List of smaller strings
     */
    public static List<String> splitString(String raw , int maxChars) {
        String[] words = raw.split("\\s+");
        List<String> segments = new ArrayList<>();
        StringBuilder currentSegment = new StringBuilder();

        for (String word : words) {
            if (currentSegment.length() == 0) {
                currentSegment.append(word.trim());
            } else if (currentSegment.length() + 1 + word.trim().length() <= maxChars) {
                currentSegment.append(" ").append(word.trim());
            } else {
                segments.add(currentSegment.toString());
                currentSegment = new StringBuilder(word.trim());
            }
        }

        segments.add(currentSegment.toString());
        return segments;
    }

    /**
     * Used to color a list of strings
     * @param list The list
     * @param color The color
     * @return The colored list
     */
    public static List<String> colorList(List<String> list, ChatColor color) {
        List<String> l = new ArrayList<>();
        for (String s : list) {
            l.add(color + s);
        }
        return l;
    }

    /**
     * Used to check if a location is within the given bounding box
     * @param boundingBox The bounding box
     * @param location The location
     * @return True if the world matches the WORLD_NAME and the location is within the bounding box, otherwise false
     */
    public static boolean isInBoundingBox(BoundingBox boundingBox, Location location) {
        if (location.getWorld() != null) {
            if (location.getWorld().toString().equals(Constants.WORLD_NAME)) {
                return boundingBox.contains(location.toVector());
            }
        }
        return false;
    }

    /**
     * Used to give an item saved within the aurum plugin to a player
     * @param player The player who should receive the item
     * @param s The namespace the item is saved under
     */
    public static void giveAurumItem(Player player, String s) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "aurum api give_item " + player.getName() + " " + s);
    }

    /**
     * Used to give a group of items saved within the aurum plugin to a player
     * @param player The player who should receive the item
     * @param s The namespace of the group
     */
    public static void giveAurumGroup(Player player, String s) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "aurum api give_group " + player.getName() + " " + s);
    }

    /**
     * Used to get the suffix of a placement number
     * @param number The number the suffix should be gotten for
     * @return The suffix (e.g. "st")
     */
    public static String getNumberSuffix(int number) {
        if (number % 10 == 1 && number != 11) {
            return "st";
        } else if (number % 10 == 2 && number != 12) {
            return "nd";
        } else {
            return number % 10 == 3 && number != 13 ? "rd" : "th";
        }
    }
}
