package net.emuman.spigotutils.armorstands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

/**
 * Represents the location of an armor stand object/model in space, including world, pitch, yaw, and roll.
 *
 * This does NOT represent the actual location of the armor stand, but the location of the associated object/model.
 */
public class ArmorStandLocation extends Location {

    private float roll;

    /**
     * Creates a new ArmorStandLocation.
     *
     * @param world the world of the armor stand location.
     * @param x     the x-position of the armor stand location.
     * @param y     the y-position of the armor stand location.
     * @param z     the z-position of the armor stand location.
     */
    public ArmorStandLocation(World world, double x, double y, double z) {
        this(world, x, y, z, 0.0f, 0.0f, 0.0f);
    }

    /**
     * Creates a new ArmorStandLocation.
     *
     * @param world the world of the armor stand location.
     * @param x     the x-position of the armor stand location.
     * @param y     the y-position of the armor stand location.
     * @param z     the z-position of the armor stand location.
     * @param yaw   the yaw of the armor stand location.
     * @param pitch the pitch of the armor stand location.
     * @param roll  the roll of the armor stand location.
     */
    public ArmorStandLocation(World world, double x, double y, double z, float yaw, float pitch, float roll) {
        super(world, x, y, z, yaw, pitch);
        this.roll = roll;
    }

    /**
     * Creates a new ArmorStandLocation from a standard Bukkit Location object.
     *
     * @param location the Location to use as the basis of the new ArmorStandLocation.
     */
    public ArmorStandLocation(Location location) {
        super(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.roll = 0.0f;
    }

    /**
     * Sets the rotational values for this armor stand location.
     *
     * @param yaw   the new yaw of the armor stand location.
     * @param pitch the new pitch of the armor stand location.
     * @param roll  the new roll of the armor stand location.
     */
    public void setRotation(float yaw, float pitch, float roll) {
        super.setYaw(yaw);
        super.setPitch(pitch);
        this.roll = roll;
    }

    /**
     * @return the roll of the armor stand location.
     */
    public float getRoll() {
        return roll;
    }

    /**
     * @param roll the new roll of the armor stand location.
     */
    public void setRoll(float roll) {
        this.roll = roll;
    }

    /**
     * Sets the direction of the armor stand location to the specified vector.
     *
     * @param vector the new direction of the armor stand location.
     * @return       this same instance for repeated calls.
     */
    @Override
    public ArmorStandLocation setDirection(Vector vector) {
        return (ArmorStandLocation) super.setDirection(vector);
    }

    /**
     * @return the direction of the armor stand location in vector form.
     */
    @Override
    public Vector getDirection() {
        return super.getDirection().rotateAroundZ(roll);
    }

    /**
     * Adds the specified location onto this armor stand location.
     *
     * @param vec the location to be added to this armor stand location.
     * @return    this same instance for repeated calls.
     */
    @Override
    public ArmorStandLocation add(Location vec) {
        return (ArmorStandLocation) super.add(vec);
    }

    /**
     * Adds the specified vector onto this armor stand location.
     *
     * @param vec the vector to be added to this armor stand location.
     * @return    this same instance for repeated calls.
     */
    @Override
    public ArmorStandLocation add(Vector vec) {
        return (ArmorStandLocation) super.add(vec);
    }

    /**
     * Adds the specified position values onto this armor stand location.
     *
     * @param x the x-component to add onto this armor stand location.
     * @param y the y-component to add onto this armor stand location.
     * @param z the z-component to add onto this armor stand location.
     * @return  this same instance for repeated calls.
     */
    @Override
    public ArmorStandLocation add(double x, double y, double z) {
        return (ArmorStandLocation) super.add(x, y, z);
    }

    /**
     * Subtracts the specified location from this armor stand location.
     *
     * @param vec the location to be subtracted from this armor stand location.
     * @return    this same instance for repeated calls.
     */
    @Override
    public ArmorStandLocation subtract(Location vec) {
        return (ArmorStandLocation) super.subtract(vec);
    }

    /**
     * Subtracts the specified vector from this armor stand location.
     *
     * @param vec the vector to be subtracted from this armor stand location.
     * @return    this same instance for repeated calls.
     */
    @Override
    public ArmorStandLocation subtract(Vector vec) {
        return (ArmorStandLocation) super.subtract(vec);
    }

    /**
     * Subtracts the specified position values from this armor stand location.
     *
     * @param x the x-component to subtract from this armor stand location.
     * @param y the y-component to subtract from this armor stand location.
     * @param z the z-component to subtract from this armor stand location.
     * @return  this same instance for repeated calls.
     */
    @Override
    public ArmorStandLocation subtract(double x, double y, double z) {
        return (ArmorStandLocation) super.subtract(x, y, z);
    }

    /**
     * Multiplies the armor stand location by the given scalar.
     *
     * @param m the scalar for the armor stand location.
     * @return  this same instance for repeated calls.
     */
    @Override
    public ArmorStandLocation multiply(double m) {
        return (ArmorStandLocation) super.multiply(m);
    }

    /**
     * Zeroes out the position values for this armor stand location.
     *
     * @return this same instance for repeated calls.
     */
    @Override
    public ArmorStandLocation zero() {
        return (ArmorStandLocation) super.zero();
    }

    @Override
    public String toString() {
        return "ArmorStandLocation{world=" + getWorld() + ",x=" + getX() + ",y=" + getY() + ",z=" + getZ() +
                ",pitch=" + getPitch() + ",yaw=" + getYaw() + ",roll=" + getRoll() + '}';
    }

    /**
     * Checks whether or not every value (x, y, z, pitch, yaw, roll) is finite.
     *
     * @throws IllegalArgumentException if any number is not finite.
     */
    @Override
    public void checkFinite() throws IllegalArgumentException {
        super.checkFinite();
        NumberConversions.checkFinite(this.roll, "roll not finite");
    }

    /**
     * @return a new ArmorStandLocation instance with the same values as this one.
     */
    @Override
    public ArmorStandLocation clone() {
        // TODO: Does this take roll into account or do I have to add it manually?
        return (ArmorStandLocation) super.clone();
    }

    /**
     * Checks if another object is equal to this armor stand location.
     *
     * @param obj the object to compare this instance to.
     * @return    true if the object is equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        ArmorStandLocation other = (ArmorStandLocation) obj;
        return (super.equals(obj) && Float.floatToIntBits(this.roll) == Float.floatToIntBits(other.roll));
    }

    /**
     * Normalizes the provided roll, binding it in between -180.0f and 180.0f.
     *
     * @param roll the roll to be normalized.
     * @return     the normalized roll.
     */
    public static float normalizeRoll(float roll) {
        roll %= 360.0F;
        if (roll >= 180.0F) {
            roll -= 360.0F;
        } else if (roll < -180.0F) {
            roll += 360.0F;
        }

        return roll;
    }

}
