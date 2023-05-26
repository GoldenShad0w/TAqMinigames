package goldenshadow.taqminigames.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public enum Game {
    EXCAVATION ("Excavation Site E", 30),
    SKY_ISLAND_LOOTRUN ("Sky Island Lootrun", 20),
    NETHER_PVP ("Nether PvP", 10),
    NESAAK_SNOWBALL_FIGHT ("Nesaak Snowball Fight", 15),
    AVOS_RACE ("Avos Race", 10),
    PROFFERS_PIT ("Proffer's Pit", 20),
    CART_RACING ("Aledar Cart Racing", 10),
    AURA_AND_VOLLEY ("Aura & Volley", 10);

    private final String label;
    private final int duration;

    Game(String label, int duration) {
        this.label = label;
        this.duration = duration;
    }

    public String getLabel() {
        return label;
    }

    public int getDuration() {
        return duration;
    }

    public static boolean contains(String value) {
        for (Game g : values()) {
            if (g.toString().equals(value)) return true;
        }
        return false;
    }

    public static List<Game> getWeightedList(int gameIndex, List<Game> possibleGames) {
        if (possibleGames.size() < 3) throw new RuntimeException("List received had a size smaller than 3! This should never happen!");
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
        while (list.size() < 3) {
            List<Game> fillUp = possibleGames.stream().filter(x -> !list.contains(x)).toList();
            if (fillUp.size() < 1) throw new RuntimeException("Fill up list was empty!");
            list.add(fillUp.get(ThreadLocalRandom.current().nextInt(0,fillUp.size())));
        }
        return list;
    }
}
