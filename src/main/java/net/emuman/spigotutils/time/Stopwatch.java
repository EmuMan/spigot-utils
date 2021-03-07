package net.emuman.spigotutils.time;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Stopwatch extends BukkitRunnable {

    private long ticks;
    private boolean running;

    /**
     * Creates a new Stopwatch.
     *
     * @param plugin the plugin using the stopwatch.
     */
    public Stopwatch(JavaPlugin plugin) {
        ticks = 0;
        this.runTaskTimer(plugin, 0, 1);
    }

    /**
     * Called by Bukkit/Spigot every tick. Should not be called by user.
     */
    @Override
    public void run() {
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
