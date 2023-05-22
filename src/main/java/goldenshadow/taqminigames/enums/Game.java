package goldenshadow.taqminigames.enums;

public enum Games {
    NONE ("---"),
    EXCAVATION ("Excavation Site E"),
    SKY_ISLAND_LOOTRUN ("Sky Island Lootrun"),
    NETHER_PVP ("Nether PvP"),
    NESAAK_SNOWBALL_FIGHT ("Nesaak Snowball Fight"),
    AVOS_RACE ("Avos Race"),
    PROFFERS_PIT ("Proffer's Pit"),
    CART_RACING ("Aledar Cart Racing"),
    AURA_AND_VOLLEY ("Aura & Volley");

    private final String label;

    Games(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
