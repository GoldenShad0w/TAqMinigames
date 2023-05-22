package goldenshadow.taqminigames.util;

import goldenshadow.taqminigames.TAqMinigames;
import org.bukkit.Bukkit;

/**
 * A class which creates a time that ticks down and runs a task when it reaches zero
 */
public class Timer {

    private int minutes;
    private int seconds;
    private final Runnable endTask;

    /**
     * Creates a new timer
     * @param minutes Amount of minutes it should start with
     * @param seconds Amount of seconds it should start with
     * @param endTask Task that should be performed when the timer runs out
     */
    public Timer(int minutes, int seconds, Runnable endTask) {

        this.minutes = Math.max(minutes, 0);
        this.seconds = Math.max(adjustForOverflow(seconds),0);
        this.endTask = endTask;
    }

    /**
     * Used to make the timer tick down by one second
     */
    public void tick() {
        seconds--;
        shiftMinutes();
    }

    /**
     * Used to make the time adjust for if a minute has passed and call the endTask if the time has run out
     */
    private void shiftMinutes() {
        if (seconds < 0) {
            seconds = 59;
            minutes--;
        }
        if (minutes == 0 && seconds == 0) {
            Bukkit.getScheduler().runTask(TAqMinigames.getPlugin(), endTask);
        }
    }


    /**
     * Creates a string representation of the timer which can be used to display its time
     * @return Returns the time in the format MM:SS
     */
    @Override
    public String toString() {
        return (minutes < 10 ? "0" + minutes : minutes) +
                ":" +
                (seconds < 10 ? "0" + seconds : seconds);
    }

    /**
     * Used to add time to the timer
     * @param minutes Amount of minutes to be added. Values below zero will be converted to 0
     * @param seconds Amount of seconds to be added. Values below zero will be converted to 0
     */
    public void addTime(int minutes, int seconds) {
        this.minutes += (Math.max(minutes, 0));
        this.seconds += (Math.max(adjustForOverflow(seconds), 0));
    }

    /**
     * Internal method used to turn overflowing seconds in to minutes
     * @param seconds Seconds to be converted
     * @return The new amount of seconds
     */
    private int adjustForOverflow(int seconds) {
        if (seconds > 59) {
            minutes += seconds / 60;
            seconds %= 60;
        }
        return seconds;
    }

    /**
     * Used to run the task early
     */
    public void runTaskEarly() {
        Bukkit.getScheduler().runTask(TAqMinigames.getPlugin(), endTask);
    }

    /**
     * Getter for the remaining minutes
     * @return The remaining whole minutes
     */
    public int getMinutesLeft() {
        return minutes;
    }

    /**
     * Getter for the remaining seconds
     * @return The remaining seconds
     */
    public int getSecondsLeft() {
        return seconds + (minutes*60);
    }


}
