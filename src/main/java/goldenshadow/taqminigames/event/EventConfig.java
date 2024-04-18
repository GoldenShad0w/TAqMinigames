package goldenshadow.taqminigames.event;

import goldenshadow.taqminigames.TAqMinigames;
import goldenshadow.taqminigames.data.*;
import goldenshadow.taqminigames.enums.Game;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class EventConfig {

    private final GenericData genericData;
    private AledarCartData aledarCartData;
    private AuraAndVolleyData auraAndVolleyData;
    private AvosRaceData avosRaceData;
    private ExcavationData excavationData;
    private NesaakData nesaakData;
    private NetherPvPData netherPvPData;
    private ProffersPitData proffersPitData;
    private SkyIslandData skyIslandData;

    private List<Game> disabledGames;

    public EventConfig() {
        genericData = new GenericData();
        updateConfig();
    }

    public void updateConfig() {
        FileConfiguration config = TAqMinigames.getPlugin().getConfig();
        disabledGames = new ArrayList<>();

        //disabled games
        if (config.getBoolean("aledar_enabled")) disabledGames.add(Game.CART_RACING);
        if (config.getBoolean("aura_enabled")) disabledGames.add(Game.AURA_AND_VOLLEY);
        if (config.getBoolean("avos_enabled")) disabledGames.add(Game.AVOS_RACE);
        if (config.getBoolean("excavation_enabled")) disabledGames.add(Game.EXCAVATION);
        if (config.getBoolean("nesaak_enabled")) disabledGames.add(Game.NESAAK_SNOWBALL_FIGHT);
        if (config.getBoolean("nether_enabled")) disabledGames.add(Game.NETHER_PVP);
        if (config.getBoolean("prof_enabled")) disabledGames.add(Game.PROFFERS_PIT);
        if (config.getBoolean("sky_enabled")) disabledGames.add(Game.SKY_ISLAND_LOOTRUN);

        //maps
        aledarCartData = new AledarCartData(AledarCartData.Map.valueOf(config.getString("aledar_map")));
        auraAndVolleyData = new AuraAndVolleyData(AuraAndVolleyData.Map.valueOf(config.getString("aura_map1")), AuraAndVolleyData.Map.valueOf(config.getString("aura_map2")), AuraAndVolleyData.Map.valueOf(config.getString("aura_map3")));
        avosRaceData = new AvosRaceData(AvosRaceData.Map.valueOf(config.getString("avos_map")));
        excavationData = new ExcavationData(ExcavationData.Map.valueOf(config.getString("excavation_map")));
        nesaakData = new NesaakData(NesaakData.Map.valueOf(config.getString("nesaak_map")));
        netherPvPData = new NetherPvPData(NetherPvPData.Map.valueOf(config.getString("nether_map")));
        proffersPitData = new ProffersPitData(ProffersPitData.Map.valueOf(config.getString("prof_map")));
        skyIslandData = new SkyIslandData(SkyIslandData.Map.valueOf(config.getString("sky_map")));
    }



    public GenericData getGenericData() {
        return genericData;
    }

    public ExcavationData getExcavationData() {
        return excavationData;
    }

    public AledarCartData getAledarCartData() {
        return aledarCartData;
    }

    public AuraAndVolleyData getAuraAndVolleyData() {
        return auraAndVolleyData;
    }

    public AvosRaceData getAvosRaceData() {
        return avosRaceData;
    }

    public NesaakData getNesaakData() {
        return nesaakData;
    }

    public NetherPvPData getNetherPvPData() {
        return netherPvPData;
    }

    public ProffersPitData getProffersPitData() {
        return proffersPitData;
    }

    public SkyIslandData getSkyIslandData() {
        return skyIslandData;
    }

    public List<Game> getDisabledGames() {
        return disabledGames;
    }
}
