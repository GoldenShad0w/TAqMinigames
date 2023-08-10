package goldenshadow.taqminigames.minigames.proffers_pit;

import org.bukkit.Location;

public record EventLocation(Location location, EventArea area, EventType eventType) {


    public enum EventArea {
        COPPER("Copper Quarry"),
        IRON("Iron Mines"),
        GOLD("Gold Canyon"),
        COBALT("Cobalt Hills"),
        DIAMOND("Diamond Caves"),
        MOLTEN("Molten Caves");

        EventArea(String name) {
            this.name = name;
        }
        private final String name;

        public String getName() {
            return name;
        }
    }

    public enum EventType {
        TOTEM,
        XP,
        GAS,
        EARTHQUAKE
    }
}
