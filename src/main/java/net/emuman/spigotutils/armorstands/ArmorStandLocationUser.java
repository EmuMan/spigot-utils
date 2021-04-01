package net.emuman.spigotutils.armorstands;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Extended upon by classes that plan to use ArmorStandLocation as a locational determinant.
 */
public abstract class ArmorStandLocationUser {

    public abstract ArmorStandLocation getLocation();
    public abstract void setLocation(ArmorStandLocation location);

    protected JavaPlugin plugin;

    /**
     * Simple constructor that sets the plugin for use in later BukkitRunnables.
     *
     * @param plugin the plugin that this ArmorStandLocationUser will run on.
     */
    protected ArmorStandLocationUser(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Moves the user of the armor stand location smoothly to the desired location.
     *
     * @param targetLocation the location to move the armor stand location user to.
     * @param duration       the duration of the movement, in game ticks.
     */
    public void move(ArmorStandLocation targetLocation, int duration) {
        // TODO: Untested
        Vector displacementVector = new Vector(
                targetLocation.getX() - getLocation().getX(),
                targetLocation.getY() - getLocation().getY(),
                targetLocation.getZ() - getLocation().getZ()
        ).multiply(1.0d / duration);
        ArmorStandLocationUser targetMovable = this;
        new BukkitRunnable() {
            int ticksLeft = duration;
            @Override
            public void run() {
                if (ticksLeft == 1) {
                    targetMovable.setLocation(targetLocation);
                    this.cancel();
                    return;
                }
                targetMovable.setLocation(targetMovable.getLocation().add(displacementVector));
                ticksLeft--;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

}
