package goldenshadow.taqminigames.data.dataconfig;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.enums.Game;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to create the gui items for the game config
 */
public class ConfigItems {

    public ItemStack aledarCartItem() {
        boolean disabled = TAqMinigames.getEventConfig().getDisabledGames().contains(Game.CART_RACING);
        ItemStack itemStack = new ItemStack(disabled ? Material.BARRIER : Material.FILLED_MAP);
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(135, 109, 156));
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ChatColor.YELLOW + "Aledar Cart Racing");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Map: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getAledarCartData().MAP);
        return common(disabled, itemStack, meta, lore);
    }

    public ItemStack auraItem() {
        boolean disabled = TAqMinigames.getEventConfig().getDisabledGames().contains(Game.AURA_AND_VOLLEY);
        ItemStack itemStack = new ItemStack(disabled ? Material.BARRIER : Material.FILLED_MAP);
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(230, 149, 57));
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ChatColor.YELLOW + "Aura & Volley");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Map 1: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getAuraAndVolleyData().MAP.getA());
        lore.add(ChatColor.GRAY + "Map 2: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getAuraAndVolleyData().MAP.getB());
        lore.add(ChatColor.GRAY + "Map 3: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getAuraAndVolleyData().MAP.getC());
        lore.add(ChatColor.GRAY + "Status: " + ChatColor.AQUA + ChatColor.BOLD + (disabled ? "DISABLED" : "ENABLED"));
        lore.add("");
        lore.add(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "LEFT-CLICK " + ChatColor.RESET + ChatColor.YELLOW + "to change map 1");
        lore.add(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "SHIFT LEFT-CLICK " + ChatColor.RESET + ChatColor.YELLOW + "to change map 2");
        lore.add(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "SHIFT RIGHT-CLICK " + ChatColor.RESET + ChatColor.YELLOW + "to change map 3");
        lore.add(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "RIGHT-CLICK " + ChatColor.RESET + ChatColor.YELLOW + "to change status");
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack avosItem() {
        boolean disabled = TAqMinigames.getEventConfig().getDisabledGames().contains(Game.AVOS_RACE);
        ItemStack itemStack = new ItemStack(disabled ? Material.BARRIER : Material.FILLED_MAP);
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(211, 232, 230));
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ChatColor.YELLOW + "Avos Race");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Map: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getAvosRaceData().MAP);
        return common(disabled, itemStack, meta, lore);
    }

    public ItemStack excavationItem() {
        boolean disabled = TAqMinigames.getEventConfig().getDisabledGames().contains(Game.EXCAVATION);
        ItemStack itemStack = new ItemStack(disabled ? Material.BARRIER : Material.FILLED_MAP);
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(36, 92, 38));
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ChatColor.YELLOW + "Excavation Site E");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Map: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getExcavationData().MAP);
        return common(disabled, itemStack, meta, lore);
    }

    public ItemStack nesaakItem() {
        boolean disabled = TAqMinigames.getEventConfig().getDisabledGames().contains(Game.NESAAK_SNOWBALL_FIGHT);
        ItemStack itemStack = new ItemStack(disabled ? Material.BARRIER : Material.FILLED_MAP);
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(33, 164, 194));
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ChatColor.YELLOW + "Nesaak Snowball Fight");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Map: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getNesaakData().MAP);
        return common(disabled, itemStack, meta, lore);
    }

    public ItemStack netherItem() {
        boolean disabled = TAqMinigames.getEventConfig().getDisabledGames().contains(Game.NETHER_PVP);
        ItemStack itemStack = new ItemStack(disabled ? Material.BARRIER : Material.FILLED_MAP);
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(255, 42, 31));
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ChatColor.YELLOW + "Nether PvP");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Map: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getNetherPvPData().MAP);
        return common(disabled, itemStack, meta, lore);
    }

    public ItemStack profItem() {
        boolean disabled = TAqMinigames.getEventConfig().getDisabledGames().contains(Game.PROFFERS_PIT);
        ItemStack itemStack = new ItemStack(disabled ? Material.BARRIER : Material.FILLED_MAP);
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(112, 112, 111));
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ChatColor.YELLOW + "Proffer's Pit");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Map: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getProffersPitData().MAP);
        return common(disabled, itemStack, meta, lore);
    }

    public ItemStack skyItem() {
        boolean disabled = TAqMinigames.getEventConfig().getDisabledGames().contains(Game.SKY_ISLAND_LOOTRUN);
        ItemStack itemStack = new ItemStack(disabled ? Material.BARRIER : Material.FILLED_MAP);
        MapMeta meta = (MapMeta) itemStack.getItemMeta();
        assert meta != null;
        meta.setColor(Color.fromRGB(69, 97, 209));
        meta.addItemFlags(ItemFlag.values());

        meta.setDisplayName(ChatColor.YELLOW + "Sky Island Lootrun");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Map: " + ChatColor.AQUA + ChatColor.BOLD + TAqMinigames.getEventConfig().getSkyIslandData().MAP);
        return common(disabled, itemStack, meta, lore);
    }

    @NotNull
    private ItemStack common(boolean disabled, ItemStack itemStack, MapMeta meta, List<String> lore) {
        lore.add(ChatColor.GRAY + "Status: " + ChatColor.AQUA + ChatColor.BOLD + (disabled ? "DISABLED" : "ENABLED"));
        lore.add("");
        lore.add(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "LEFT-CLICK " + ChatColor.RESET + ChatColor.YELLOW + "to change map");
        lore.add(ChatColor.YELLOW + String.valueOf(ChatColor.BOLD) + "RIGHT-CLICK " + ChatColor.RESET + ChatColor.YELLOW + "to change status");
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
