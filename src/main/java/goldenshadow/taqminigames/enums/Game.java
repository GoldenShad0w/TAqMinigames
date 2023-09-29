package goldenshadow.taqminigames.enums;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public enum Game {
    EXCAVATION ("Excavation Site E"),
    SKY_ISLAND_LOOTRUN ("Sky Island Lootrun"),
    NETHER_PVP ("Nether PvP"),
    NESAAK_SNOWBALL_FIGHT ("Nesaak Snowball Fight"),
    AVOS_RACE ("Avos Race"),
    PROFFERS_PIT ("Proffer's Pit"),
    CART_RACING ("Aledar Cart Racing"),
    AURA_AND_VOLLEY ("Aura & Volley");

    private final String label;

    Game(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }



    public static boolean contains(String value) {
        for (Game g : values()) {
            if (g.toString().equals(value)) return true;
        }
        return false;
    }

    public static List<Game> getWeightedList(int gameIndex, List<Game> possibleGames) {
        if (possibleGames.isEmpty()) throw new RuntimeException("List received by getWeightedList was empty! This should never happen!");
        List<Game> list = new ArrayList<>();
        if (gameIndex < 3) {
            if (possibleGames.contains(AURA_AND_VOLLEY)) list.add(AURA_AND_VOLLEY);
            if (possibleGames.contains(AVOS_RACE)) list.add(AVOS_RACE);
            if (possibleGames.contains(NETHER_PVP)) list.add(NETHER_PVP);
            if (possibleGames.contains(CART_RACING)) list.add(CART_RACING);
        }
        if (gameIndex == 2 || gameIndex == 3) {
            if (possibleGames.contains(NESAAK_SNOWBALL_FIGHT)) list.add(NESAAK_SNOWBALL_FIGHT);
        }
        if (gameIndex > 2) {
            if (possibleGames.contains(SKY_ISLAND_LOOTRUN)) list.add(SKY_ISLAND_LOOTRUN);
            if (possibleGames.contains(PROFFERS_PIT)) list.add(PROFFERS_PIT);
            if (possibleGames.contains(EXCAVATION)) list.add(EXCAVATION);
        }
        while (list.isEmpty()) {
            List<Game> fillUp = new ArrayList<>(possibleGames);
            if (fillUp.isEmpty()) throw new RuntimeException("Fill up list was empty!");
            list.add(fillUp.get(ThreadLocalRandom.current().nextInt(0,fillUp.size())));
        }
        return list;
    }
}
