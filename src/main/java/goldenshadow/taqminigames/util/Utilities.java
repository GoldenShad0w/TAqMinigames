package goldenshadow.taqminigames.util;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
            if (currentSegment.isEmpty()) {
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
     * @return The number with the appropriate suffix
     */
    public static String getNumberSuffix(int number) {
        if (number % 10 == 1 && number != 11) {
            return number + "st";
        } else if (number % 10 == 2 && number != 12) {
            return number + "nd";
        } else {
            return number % 10 == 3 && number != 13 ? number + "rd" : number + "th";
        }
    }

    /**
     * Used to convert seconds to milliseconds
     * @param seconds The seconds that should be converted
     * @return That amount in milliseconds
     */
    public static long secondsToMillis(double seconds) {
        return (long) (seconds * 1000L);
    }

    /**
     * Used to register that trigger in the lobby which teleports falling players back onto the island
     */
    public static void registerLobbyTrigger() {
        Trigger.register(new Trigger(new BoundingBox(10, 33, 281, 193, 18, -8), Constants.WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> p.teleport(Constants.LOBBY), 0, false, false));
        for (Location loc : Constants.LOBBY_JUMP_PADS) {
            BoundingBox box = new BoundingBox(loc.getX()+1.5, loc.getY()+1, loc.getZ()+1.5, loc.getX()-1.5, loc.getY(), loc.getZ()-1.5);
            Trigger.register(new Trigger(box, Constants.WORLD, p -> p.getGameMode() == GameMode.ADVENTURE, p -> {
                p.setVelocity(p.getLocation().getDirection().multiply(2.2).setY(1.5));
                assert p.getLocation().getWorld() != null;
                p.playSound(p.getLocation(), Sound.ITEM_TRIDENT_HIT_GROUND, 1,1);
            }, Utilities.secondsToMillis(2), false, false));
        }
    }

    public static void lockArmorStand(ArmorStand stand) {
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.OFF_HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.ADDING_OR_CHANGING);
        stand.addEquipmentLock(EquipmentSlot.HAND, ArmorStand.LockType.REMOVING_OR_CHANGING);
    }

    public static ItemStack getVictoryCrown() {
        ItemStack itemStack = new ItemStack(Material.CARVED_PUMPKIN);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setCustomModelData(17);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        meta.setDisplayName(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "Victory Crown");
        List<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ChatColor.YELLOW + "A crown awarded to the winner");
        lore.add(ChatColor.YELLOW + "of TAq Minigames");
        lore.add(ChatColor.DARK_GRAY + "(Not for resale on forum)");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.values());
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
