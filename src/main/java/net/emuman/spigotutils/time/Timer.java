package net.emuman.spigotutils.time;

import org.bukkit.plugin.java.JavaPlugin;

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

    @Override
    public void increment() {
        super.increment();
        if (getTicks() >= duration) {
            stop();
        }
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public abstract void onTimerFinish();

}
