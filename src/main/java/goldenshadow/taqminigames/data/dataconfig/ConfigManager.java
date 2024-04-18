package goldenshadow.taqminigames.data.dataconfig;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.data.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ConfigManager {

    private static final ConfigItems configItems = new ConfigItems();


    /**
     * Used to get the config gui
     * @return The GUI
     */
    public static Inventory getGUI() {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.BOLD + "TAq Minigames Config");
        fillInventory(inventory);
        return inventory;
    }

    /**
     * Used to fill the config GUI with the correct items
     * @param inventory The GUI
     */
    private static void fillInventory(Inventory inventory) {
        inventory.setItem(0, configItems.aledarCartItem());
        inventory.setItem(1, configItems.auraItem());
        inventory.setItem(2, configItems.avosItem());
        inventory.setItem(3, configItems.excavationItem());
        inventory.setItem(4, configItems.nesaakItem());
        inventory.setItem(5, configItems.netherItem());
        inventory.setItem(6, configItems.profItem());
        inventory.setItem(7, configItems.skyItem());
    }

    /**
     * Called when a player clicks on an item in the gui
     * @param event The event
     */
    public static void guiClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getOpenInventory().getTitle().equals(ChatColor.BOLD + "TAq Minigames Config")) {
            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() != null) {
                if (event.getClickedInventory() != player.getInventory()) {
                    event.setCancelled(true);

                    switch (event.getSlot()) {
                        case 0 -> {
                            if (event.isLeftClick()) {
                                int i = TAqMinigames.getEventConfig().getAledarCartData().MAP.ordinal() + 1;
                                AledarCartData.Map map = AledarCartData.Map.values()[(i < AledarCartData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("aledar_map", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isRightClick()) {
                                TAqMinigames.getPlugin().getConfig().set("aledar_enabled", !TAqMinigames.getPlugin().getConfig().getBoolean("aledar_enabled"));
                                TAqMinigames.getPlugin().saveConfig();
                            }
                        }
                        case 1 -> {
                            if (event.isLeftClick() && !event.isShiftClick()) {
                                int i = TAqMinigames.getEventConfig().getAuraAndVolleyData().MAP.getA().ordinal() + 1;
                                AuraAndVolleyData.Map map = AuraAndVolleyData.Map.values()[(i < AuraAndVolleyData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("aura_map1", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isLeftClick() && event.isShiftClick()) {
                                int i = TAqMinigames.getEventConfig().getAuraAndVolleyData().MAP.getB().ordinal() + 1;
                                AuraAndVolleyData.Map map = AuraAndVolleyData.Map.values()[(i < AuraAndVolleyData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("aura_map2", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isRightClick() && event.isShiftClick()) {
                                int i = TAqMinigames.getEventConfig().getAuraAndVolleyData().MAP.getB().ordinal() + 1;
                                AuraAndVolleyData.Map map = AuraAndVolleyData.Map.values()[(i < AuraAndVolleyData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("aura_map3", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isRightClick() && !event.isShiftClick()) {
                                TAqMinigames.getPlugin().getConfig().set("aura_enabled", !TAqMinigames.getPlugin().getConfig().getBoolean("aura_enabled"));
                                TAqMinigames.getPlugin().saveConfig();
                            }
                        }
                        case 2 -> {
                            if (event.isLeftClick()) {
                                int i = TAqMinigames.getEventConfig().getAvosRaceData().MAP.ordinal() + 1;
                                AvosRaceData.Map map = AvosRaceData.Map.values()[(i < AvosRaceData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("avos_map", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isRightClick()) {
                                TAqMinigames.getPlugin().getConfig().set("avos_enabled", !TAqMinigames.getPlugin().getConfig().getBoolean("avos_enabled"));
                                TAqMinigames.getPlugin().saveConfig();
                            }
                        }
                        case 3 -> {
                            if (event.isLeftClick()) {
                                int i = TAqMinigames.getEventConfig().getExcavationData().MAP.ordinal() + 1;
                                ExcavationData.Map map = ExcavationData.Map.values()[(i < ExcavationData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("excavation_map", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isRightClick()) {
                                TAqMinigames.getPlugin().getConfig().set("excavation_enabled", !TAqMinigames.getPlugin().getConfig().getBoolean("excavation_enabled"));
                                TAqMinigames.getPlugin().saveConfig();
                            }
                        }
                        case 4 -> {
                            if (event.isLeftClick()) {
                                int i = TAqMinigames.getEventConfig().getNesaakData().MAP.ordinal() + 1;
                                NesaakData.Map map = NesaakData.Map.values()[(i < NesaakData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("nesaak_map", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isRightClick()) {
                                TAqMinigames.getPlugin().getConfig().set("nesaak_enabled", !TAqMinigames.getPlugin().getConfig().getBoolean("nesaak_enabled"));
                                TAqMinigames.getPlugin().saveConfig();
                            }
                        }
                        case 5 -> {
                            if (event.isLeftClick()) {
                                int i = TAqMinigames.getEventConfig().getNetherPvPData().MAP.ordinal() + 1;
                                NetherPvPData.Map map = NetherPvPData.Map.values()[(i < NetherPvPData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("nether_map", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isRightClick()) {
                                TAqMinigames.getPlugin().getConfig().set("nether_enabled", !TAqMinigames.getPlugin().getConfig().getBoolean("nether_enabled"));
                                TAqMinigames.getPlugin().saveConfig();
                            }
                        }
                        case 6 -> {
                            if (event.isLeftClick()) {
                                int i = TAqMinigames.getEventConfig().getProffersPitData().MAP.ordinal() + 1;
                                ProffersPitData.Map map = ProffersPitData.Map.values()[(i < ProffersPitData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("prof_map", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isRightClick()) {
                                TAqMinigames.getPlugin().getConfig().set("prof_enabled", !TAqMinigames.getPlugin().getConfig().getBoolean("prof_enabled"));
                                TAqMinigames.getPlugin().saveConfig();
                            }
                        }
                        case 7 -> {
                            if (event.isLeftClick()) {
                                int i = TAqMinigames.getEventConfig().getSkyIslandData().MAP.ordinal() + 1;
                                SkyIslandData.Map map = SkyIslandData.Map.values()[(i < SkyIslandData.Map.values().length ? i : 0)];
                                TAqMinigames.getPlugin().getConfig().set("sky_map", map.toString());
                                TAqMinigames.getPlugin().saveConfig();
                            } else if (event.isRightClick()) {
                                TAqMinigames.getPlugin().getConfig().set("sky_enabled", !TAqMinigames.getPlugin().getConfig().getBoolean("sky_enabled"));
                                TAqMinigames.getPlugin().saveConfig();
                            }
                        }
                    }
                    TAqMinigames.getEventConfig().updateConfig();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                        if (event.getClickedInventory() != null) {
                            fillInventory(event.getClickedInventory());
                        }
                    }, 2L);
                }
            }
        }
    }
}


