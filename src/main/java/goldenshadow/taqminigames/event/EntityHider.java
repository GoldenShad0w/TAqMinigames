package goldenshadow.taqminigames.event;

import goldenshadow.taqminigames.TAqMinigames;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.*;

//TODO rewrite this to support default hidden entities better and be easier to use for twain game
public class EntityHider {

    public enum HideType {UNTIL_DEATH, UNTIL_CHANGED}

    private static final HashMap<UUID, HashSet<UUID>> untilChanged = new HashMap<>();
    private static final HashMap<UUID, HashSet<UUID>> untilDeath = new HashMap<>();


    /**
     * Used to hide an entity for a specific player
     * @param player The player the entity should be hidden for
     * @param entity The entity that should be hidden
     * @param hideType Under what condition the entity should reappear
     */
    public static void hideEntity(Player player, Entity entity, HideType hideType) {
        HashSet<UUID> set = new HashSet<>();
        if (hideType == HideType.UNTIL_DEATH) {
            if (untilDeath.containsKey(player.getUniqueId())) set = untilDeath.get(player.getUniqueId());
            set.add(entity.getUniqueId());
            untilDeath.put(player.getUniqueId(), set);
        }
        if (hideType == HideType.UNTIL_CHANGED) {
            if (untilChanged.containsKey(player.getUniqueId())) set = untilChanged.get(player.getUniqueId());
            set.add(entity.getUniqueId());
            untilChanged.put(player.getUniqueId(), set);
        }
        player.hideEntity(TAqMinigames.getPlugin(), entity);
    }

    /**
     * Used to make a hidden entity reappear for a player
     * @param player The player the entity should be revealed to
     * @param uuid The UUID of the entity that should reappear
     */
    public static void revealEntity(Player player, UUID uuid) {
        if (untilDeath.containsKey(player.getUniqueId())) {
            HashSet<UUID> set = untilDeath.get(player.getUniqueId());
            set.removeIf(x -> x.equals(uuid));
            untilDeath.put(player.getUniqueId(), set);
        }
        if (untilChanged.containsKey(player.getUniqueId())) {
            HashSet<UUID> set = untilChanged.get(player.getUniqueId());
            set.removeIf(x -> x.equals(uuid));
            untilChanged.put(player.getUniqueId(), set);
        }
        Entity entity = Bukkit.getEntity(uuid);
        if (entity != null) {
            player.showEntity(TAqMinigames.getPlugin(), entity);
        }
    }

    /**
     * Used to make all hidden entities reappear for all players
     */
    public static void resetAll() {
        for (UUID playerUUID : untilChanged.keySet()) {
            for (UUID entityUUID : untilChanged.get(playerUUID)) {
                Player player = Bukkit.getPlayer(playerUUID);
                Entity entity = Bukkit.getEntity(entityUUID);
                if (player != null && entity != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                        if (entity.isVisibleByDefault()) {
                            player.showEntity(TAqMinigames.getPlugin(),entity);
                        } else {
                            player.hideEntity(TAqMinigames.getPlugin(), entity);
                        }
                    }, 2L);
                }
            }
        }
        for (UUID playerUUID : untilDeath.keySet()) {
            for (UUID entityUUID : untilDeath.get(playerUUID)) {
                Player player = Bukkit.getPlayer(playerUUID);
                Entity entity = Bukkit.getEntity(entityUUID);
                if (player != null && entity != null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(TAqMinigames.getPlugin(), () -> {
                        if (entity.isVisibleByDefault()) {
                            player.showEntity(TAqMinigames.getPlugin(),entity);
                        } else {
                            player.hideEntity(TAqMinigames.getPlugin(), entity);
                        }
                    }, 2L);
                }
            }
        }
        untilDeath.clear();
        untilChanged.clear();
    }


    /**
     * Used to handle when an entity dies
     * @param event The death event
     */
    public static void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (untilDeath.containsKey(player.getUniqueId())) {
            HashSet<UUID> set = untilDeath.get(player.getUniqueId());
            untilDeath.put(player.getUniqueId(), new HashSet<>());
            for (UUID uuid : set) {
                revealEntity(player, uuid);
            }
        }

    }



}
