package net.emuman.spigotutils.time;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A stopwatch that keeps track of how many ticks have passed (tick count can also be stopped/started/altered).
 */
public class Stopwatch {
    // Extending BukkitRunnable forces us to expose the BukkitRunnable#run method, which is undesirable.

    private long ticks;
    private boolean running;
    private BukkitRunnable runnable;

    /**
     * Creates a new Stopwatch.
     *
     * @param plugin the plugin using the stopwatch.
     */
    public Stopwatch(JavaPlugin plugin) {
        ticks = 0;
        start();
        // I don't think I can use lambda expressions here unfortunately
        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                increment();
            }
        };
        this.runnable.runTaskTimer(plugin, 0, 1);
    }

    /**
     * Called by Bukkit every tick. Should not be called by user.
     */
    public void increment() {
        if (running) ticks += 1;
    }

    /**
     * Starts the stopwatch. If the stopwatch is already running, nothing is changed.
     */
    public void start() {
        running = true;
    }

    /**
     * Stops the stopwatch. If the stopwatch is already stopped, nothing is changed.
     */
    public void stop() {
        running = false;
    }

    /**
     * @return true if the stopwatch is running, false if not.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @return the number of ticks the stopwatch has been running.
     */
    public long getTicks() {
        return ticks;
    }

    /**
     * @param ticks the number of ticks to set the stopwatch to.
     */
    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

}
