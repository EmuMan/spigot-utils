package net.emuman.spigotutils.time;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * A timer that calls onTimerFinish when it reaches the set duration (current time can also be stopped/started/altered).
 */
public abstract class Timer extends Stopwatch {

    private long duration;

    /**
     * Creates a new timer.
     *
     * @param plugin   the plugin using the timer.
     * @param duration the duration of the timer.
     */
    public Timer(JavaPlugin plugin, long duration) {
        super(plugin);
        this.duration = duration;
    }

    /**
     * Called by Bukkit every tick. Should not be called by user.
     */
    @Override
    public void increment() {
        super.increment();
        if (getTicks() >= duration) {
            stop();
        }
    }

    /**
     * @return the set duration of the timer.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * @param duration the new duration of the timer.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Called when the timer reaches the set duration. Should be implemented by user.
     */
    public abstract void onTimerFinish();

}
