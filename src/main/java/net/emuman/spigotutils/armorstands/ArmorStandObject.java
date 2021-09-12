package net.emuman.spigotutils.armorstands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

/**
 * Represents an object held/worn by an armor stand, allowing for arbitrary models to be easily created.
 */
public class ArmorStandObject extends ArmorStandLocationUser{

    public enum Size {
        BIG_HEAD,
        SMALL_HEAD,
        BIG_HAND,
        SMALL_HAND,
    }

    private static final double[] bhJointVector = new double[] {0.0d, 4.0d / 16.0d, 0.0d};
    private static final double[] bhJointDisplacement = new double[] {0.0d, 23.0d / 16.0d, 0.0d};

    private static final double[] shJointVector = new double[] {0.0d, (3.714d * 0.75d) / 16.0d, 0.0d};
    private static final double[] shJointDisplacement = new double[] {0.0d, (23.0d / 2.0d) / 16.0d, 0.0d};

    private ArmorStand armorStand;
    private Size size;
    private World world;

    private boolean wasChanged;

    private ArmorStandLocation location;

    /**
     * Creates a new ArmorStandObject.
     *
     * @param plugin   the plugin this armor stand model runs on.
     * @param item     the item represented by this armor stand object.
     * @param location the location of the armor stand object.
     * @param size     the size of the armor stand object (can currently only be BIG_HEAD or SMALL_HEAD).
     */
    public ArmorStandObject(JavaPlugin plugin, ItemStack item, ArmorStandLocation location, Size size) {
        super(plugin);
        if (location.getWorld() == null) return;
        this.size = size;
        this.world = location.getWorld();
        this.location = location;

        this.armorStand = location.getWorld().spawn(getCompensatedLocation(), ArmorStand.class);

        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);

        if (size == Size.SMALL_HEAD || size == Size.SMALL_HAND) {
            armorStand.setSmall(true);
        }

        if (armorStand.getEquipment() != null) {
            if (size == Size.BIG_HAND || size == Size.SMALL_HAND) {
                armorStand.getEquipment().setItemInMainHand(item);
            } else {
                armorStand.getEquipment().setHelmet(item);
            }
        }

        update();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (wasChanged) {
                    update();
                    wasChanged = false;
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    /**
     * Helper method to reflect the changes to the armor stand object location onto the actual armor stand itself.
     *
     * Should only be called once per tick, hence the wasChanged variable.
     */
    private void update() {
        if (armorStand != null) {
            armorStand.teleport(getCompensatedLocation());
            if (size == Size.BIG_HEAD || size == Size.SMALL_HEAD) {
                armorStand.setHeadPose(new EulerAngle(
                        Math.toRadians(location.getPitch()),
                        Math.toRadians(location.getYaw()),
                        Math.toRadians(location.getRoll()))); // TODO: Maybe use NMS to smooth this out?
            } else {

            }
        }
    }

    /**
     * Helper method to get the location of the actual armor stand based off of the location of the armor stand object.
     *
     * Uses vector math to essentially trace the positional and rotational displacements of the object backwards.
     *
     * @return the location of the actual armor stand.
     */
    private Location getCompensatedLocation() {
        Vector jointVector;
        Vector jointDisplacement;

        switch (size) {
            case BIG_HEAD:
                jointVector = new Vector(bhJointVector[0], bhJointVector[1], bhJointVector[2]);
                jointDisplacement = new Vector(bhJointDisplacement[0], bhJointDisplacement[1], bhJointDisplacement[2]);
                break;
            case SMALL_HEAD:
                jointVector = new Vector(shJointVector[0], shJointVector[1], shJointVector[2]);
                jointDisplacement = new Vector(shJointDisplacement[0], shJointDisplacement[1], shJointDisplacement[2]);
                break;
            case BIG_HAND:
            case SMALL_HAND:
            default:
                jointVector = new Vector(0.0d, 0.0d, 0.0d);
                jointDisplacement = new Vector(0.0d, 0.0d, 0.0d);
                break;
        }
        jointVector = jointVector.rotateAroundX(Math.toRadians(location.getPitch()))
                .rotateAroundY(Math.toRadians(-location.getYaw()))
                .rotateAroundZ(Math.toRadians(-location.getRoll()));

        return new Location(world, location.getX(), location.getY(), location.getZ()).subtract(jointVector).subtract(jointDisplacement);
    }

    /**
     * @return the armor stand object's location.
     */
    public ArmorStandLocation getLocation() {
        return location.clone();
    }

    /**
     * @param location the armor stand object's new location.
     */
    public void setLocation(ArmorStandLocation location) {
        this.location = location;
        wasChanged = true;
    }

    public void destroy() {
        armorStand.remove();
    }

}
