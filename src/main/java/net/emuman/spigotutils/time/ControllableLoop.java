package net.emuman.spigotutils.time;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.IntSupplier;

/**
 * Essentially a Bukkit runnable that can be stopped and started, and can also have a variable period.
 */
public class ControllableLoop extends Stopwatch {

    private int period;
    private IntSupplier periodSupplier = null;
    private final Runnable onLoop;

    /**
     * Creates a new ControllableLoop.
     *
     * @param plugin the plugin running the loop.
     * @param period the period of the loop, measured in game ticks.
     * @param onLoop the runnable that is called on every loop.
     */
    public ControllableLoop(JavaPlugin plugin, int period, Runnable onLoop) {
        super(plugin);
        this.period = period;
        this.onLoop = onLoop;
    }

    /**
     * Creates a new ControllableLoop.
     *
     * @param plugin         the plugin running the loop.
     * @param periodSupplier the supplier that determines the period of the loop, measured in game ticks (updates every period tick, not every game tick).
     * @param onLoop         the runnable that is called on every loop.
     */
    public ControllableLoop(JavaPlugin plugin, IntSupplier periodSupplier, Runnable onLoop) {
        super(plugin);
        this.periodSupplier = periodSupplier;
        this.onLoop = onLoop;
    }

    /**
     * Called by Bukkit every tick. Should not be called by user.
     */
    @Override
    public void increment() {
        super.increment();
        if (getTicks() >= period) {
            setTicks(0);
            if (periodSupplier != null) period = periodSupplier.getAsInt();
            onLoop.run();
        }
    }

    /**
     * @return the current period of the loop.
     */
    public int getPeriod() {
        return period;
    }

    /**
     * @param period the new period of the loop.
     */
    public void setPeriod(int period) {
        this.period = period;
    }

}
