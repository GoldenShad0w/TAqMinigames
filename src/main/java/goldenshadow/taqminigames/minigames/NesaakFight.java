package goldenshadow.taqminigames.minigames;

import goldenshadow.taqminigames.enums.Game;
import org.bukkit.entity.Player;

public class NesaakFight extends Minigame{
    @Override
    public void onDeath(Player player) {

    }

    @Override
    public void playerReconnect(Player player) {

    }

    @Override
    public Game getGame() {
        return Game.NESAAK_SNOWBALL_FIGHT;
    }
}
