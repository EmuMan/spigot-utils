package net.emuman.spigotutils.time;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.IntSupplier;

public class ControllableLoop extends Stopwatch {

    private int period;
    private IntSupplier periodSupplier = null;
    private final Runnable onLoop;

    public ControllableLoop(JavaPlugin plugin, int period, Runnable onLoop) {
        super(plugin);
        this.period = period;
        this.onLoop = onLoop;
    }

    public ControllableLoop(JavaPlugin plugin, IntSupplier periodSupplier, Runnable onLoop) {
        super(plugin);
        this.periodSupplier = periodSupplier;
        this.onLoop = onLoop;
    }

    @Override
    public void increment() {
        super.increment();
        if (getTicks() >= period) {
            setTicks(0);
            if (periodSupplier != null) period = periodSupplier.getAsInt();
            onLoop.run();
        }
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

}
