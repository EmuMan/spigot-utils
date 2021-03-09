package net.emuman.spigotutils.time;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

// Extending BukkitRunnable forces us to expose the run method, which is undesirable.
public class Stopwatch {

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
     * Called by Bukkit/Spigot every tick. Should not be called by user.
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
     * Returns whether the stopwatch is running.
     *
     * @return true if the stopwatch is running, false if not.
     */
    public boolean isRunning() {
        return running;
    }

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

}
