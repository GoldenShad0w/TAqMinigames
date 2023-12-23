package goldenshadow.taqminigames.minigames.proffers_pit;

import org.bukkit.Location;

public record EventLocation(Location location, EventArea area, EventType eventType) {

    /**
     * Event area enum
     */
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

        /**
         * Getter for the name
         * @return The name
         */
        public String getName() {
            return name;
        }
    }

    /**
     * Event type enum
     */
    public enum EventType {
        TOTEM,
        XP,
        GAS,
        EARTHQUAKE
    }
}
