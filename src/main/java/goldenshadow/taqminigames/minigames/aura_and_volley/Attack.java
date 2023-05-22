package goldenshadow.taqminigames.minigames.aura_and_volley;

/**
 * An abstract class representing a tower attack for aura and volley
 */
public abstract class Attack {

    protected int tick = 0;
    protected boolean isDone = false;

    /**
     * Creates a new attack object
     */
    public Attack() {
        tick();
    }

    /**
     * 20hz loop
     */
    public void tick() {
        tick++;
    }

    /**
     * Getter for whether the attack is over
     * @return True if it is, false if it isn't
     */
    public boolean isDone() {
        return isDone;
    }
}
