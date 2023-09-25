package goldenshadow.taqminigames.minigames.proffers_pit;

import goldenshadow.taqminigames.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.List;


public class ShopItems {

    public static ItemStack getBoots(int price) {
        ItemStack itemStack = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(58, 179, 171));
        meta.setDisplayName(ChatColor.RED + String.valueOf(ChatColor.BOLD) + "Breeze");
        meta.setLore(List.of(
                " ",
                ChatColor.GRAY + "Base Health Bonus: " + ChatColor.WHITE + "0",
                ChatColor.GRAY + "Minimum Level Req: " + ChatColor.WHITE + "100",
                " ",
                ChatColor.GREEN + "+50% " + ChatColor.GRAY + "Walk Speed",
                " ",
                ChatColor.RED + "Legendary Item",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP"
                ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getSword(int price) {
        ItemStack itemStack = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.AQUA + String.valueOf(ChatColor.BOLD) + "Pocket Knife");
        meta.setLore(List.of(
                " ",
                ChatColor.GRAY + "Base Attack Damage: " + ChatColor.WHITE + "5",
                ChatColor.GRAY + "Minimum Level Req: " + ChatColor.WHITE + "100",
                " ",
                ChatColor.GREEN + "+10% " + ChatColor.GRAY + "Walk Speed",
                " ",
                ChatColor.AQUA + "Rare Item",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setCustomModelData(22);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getPickT2(int price) {
        ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.WHITE + "Gathering Pickaxe");
        meta.setLore(List.of(
                ChatColor.GRAY + "Tier 2",
                " ",
                ChatColor.GOLD + "Can break:",
                ChatColor.YELLOW + "- Copper",
                ChatColor.YELLOW + "- Iron",
                " ",
                ChatColor.DARK_AQUA + "Gathering Tool",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP + Tier 1 Pickaxe"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setCustomModelData(2);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getPickT3(int price) {
        ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.WHITE + "Gathering Pickaxe");
        meta.setLore(List.of(
                ChatColor.GRAY + "Tier 3",
                " ",
                ChatColor.GOLD + "Can break:",
                ChatColor.YELLOW + "- Copper",
                ChatColor.YELLOW + "- Iron",
                ChatColor.YELLOW + "- Gold",
                " ",
                ChatColor.DARK_AQUA + "Gathering Tool",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP  + Tier 2 Pickaxe"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setCustomModelData(3);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getPickT4(int price) {
        ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.WHITE + "Gathering Pickaxe");
        meta.setLore(List.of(
                ChatColor.GRAY + "Tier 4",
                " ",
                ChatColor.GOLD + "Can break:",
                ChatColor.YELLOW + "- Copper",
                ChatColor.YELLOW + "- Iron",
                ChatColor.YELLOW + "- Gold",
                ChatColor.YELLOW + "- Cobalt",
                " ",
                ChatColor.DARK_AQUA + "Gathering Tool",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP + Tier 3 Pickaxe"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setCustomModelData(4);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getPickT5(int price) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.WHITE + "Gathering Pickaxe");
        meta.setLore(List.of(
                ChatColor.GRAY + "Tier 5",
                " ",
                ChatColor.GOLD + "Can break:",
                ChatColor.YELLOW + "- Copper",
                ChatColor.YELLOW + "- Iron",
                ChatColor.YELLOW + "- Gold",
                ChatColor.YELLOW + "- Cobalt",
                ChatColor.YELLOW + "- Diamond",
                " ",
                ChatColor.DARK_AQUA + "Gathering Tool",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP + Tier 4 Pickaxe"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setCustomModelData(5);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getPickT6(int price) {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_PICKAXE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.WHITE + "Gathering Pickaxe");
        meta.setLore(List.of(
                ChatColor.GRAY + "Tier 6",
                " ",
                ChatColor.GOLD + "Can break:",
                ChatColor.YELLOW + "- Copper",
                ChatColor.YELLOW + "- Iron",
                ChatColor.YELLOW + "- Gold",
                ChatColor.YELLOW + "- Cobalt",
                ChatColor.YELLOW + "- Diamond",
                ChatColor.YELLOW + "- Molten",
                " ",
                ChatColor.DARK_AQUA + "Gathering Tool",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP + Tier 5 Pickaxe"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setCustomModelData(6);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getPotion(int price) {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.AQUA);
        meta.setDisplayName(ChatColor.BLUE + String.valueOf(ChatColor.BOLD) + "Potion of Swiftness " + ChatColor.RESET + ChatColor.GRAY + "[3/3]");
        meta.setLore(List.of(
                ChatColor.DARK_GRAY + "Right click to use",
                " ",
                ChatColor.GRAY + "Minimum Level Req: " + ChatColor.WHITE + "100",
                " ",
                ChatColor.YELLOW + "Speed II for 15 seconds",
                " ",
                ChatColor.BLUE + "Consumable Item",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getMelon(int price) {
        ItemStack itemStack = new ItemStack(Material.GLISTERING_MELON_SLICE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.BLUE + String.valueOf(ChatColor.BOLD) + "Glistering Melon " + ChatColor.RESET + ChatColor.GRAY + "[1/1]");
        meta.setLore(List.of(
                ChatColor.DARK_GRAY + "Right click to use",
                " ",
                ChatColor.GRAY + "Minimum Level Req: " + ChatColor.WHITE + "100",
                " ",
                ChatColor.YELLOW + "Haste II for 30 seconds",
                " ",
                ChatColor.BLUE + "Consumable Item",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getSoup(int price) {
        ItemStack itemStack = new ItemStack(Material.BEETROOT_SOUP);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.BLUE + String.valueOf(ChatColor.BOLD) + "Hunter's Soup " + ChatColor.RESET + ChatColor.GRAY + "[1/1]");
        meta.setLore(List.of(
                ChatColor.DARK_GRAY + "Right click to use",
                " ",
                ChatColor.GRAY + "Minimum Level Req: " + ChatColor.WHITE + "100",
                " ",
                ChatColor.YELLOW + "+6 Health",
                " ",
                ChatColor.BLUE + "Consumable Item",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getHuntedToken() {
        ItemStack itemStack = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + String.valueOf(ChatColor.BOLD) + "Hunted Mode");
        meta.setLore(List.of(
                " ",
                ChatColor.GRAY + "While active, you gain a " + ChatColor.RED + "+50% XP boost",
                ChatColor.GRAY + "but other hunted players will be able",
                ChatColor.GRAY + "to " + ChatColor.RED + "attack and kill you!",
                " ",
                ChatColor.GRAY + "Dying will cost you " + ChatColor.RED + "10$ of your Prof XP",
                ChatColor.GRAY + "Activation Cooldown: " + ChatColor.RED + "30 seconds",
                " ",
                ChatColor.RED + "Right click to toggle"
        ));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getHuntedTokenWithPrice(int price) {
        ItemStack itemStack = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_RED + String.valueOf(ChatColor.BOLD) + "Hunted Mode");
        meta.setLore(List.of(
                " ",
                ChatColor.GRAY + "While active, you gain a " + ChatColor.RED + "+50% XP boost",
                ChatColor.GRAY + "but other hunted players will be able",
                ChatColor.GRAY + "to " + ChatColor.RED + "attack and kill you!",
                " ",
                ChatColor.GRAY + "Dying will cost you " + ChatColor.RED + "10% of your Prof XP",
                ChatColor.GRAY + "Activation Cooldown: " + ChatColor.RED + "30 seconds",
                " ",
                ChatColor.RED + "Right click to toggle",
                " ",
                ChatColor.DARK_PURPLE + "Price: " + ChatColor.LIGHT_PURPLE + price + " Prof XP"
        ));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack getFiller() {
        ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static Inventory getShopGUI() {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.BOLD + "Mining Supplies");
        for (int i = 0; i < 27; i++) {
            switch (i) {
                case 3 -> inventory.setItem(i, getBoots(Constants.PROF_BOOTS_PRICE));
                case 4 -> inventory.setItem(i, getHuntedTokenWithPrice(Constants.PROF_HUNTED_PRICE));
                case 5 -> inventory.setItem(i, getSword(Constants.PROF_SWORD_PRICE));
                case 11 -> inventory.setItem(i, getPickT2(Constants.PROF_T2_PRICE));
                case 12 -> inventory.setItem(i, getPickT3(Constants.PROF_T3_PRICE));
                case 13 -> inventory.setItem(i, getPickT4(Constants.PROF_T4_PRICE));
                case 14 -> inventory.setItem(i, getPickT5(Constants.PROF_T5_PRICE));
                case 15 -> inventory.setItem(i, getPickT6(Constants.PROF_T6_PRICE));
                case 21 -> inventory.setItem(i, getPotion(Constants.PROF_SPEED_PRICE));
                case 22 -> inventory.setItem(i, getMelon(Constants.PROF_HASTE_PRICE));
                case 23 -> inventory.setItem(i, getSoup(Constants.PROF_HEALTH_PRICE));
                default -> inventory.setItem(i, getFiller());
            }
        }
        return inventory;
    }

}
