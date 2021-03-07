package net.emuman.spigotutils.armorstands;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Objects;

public class ArmorStandObject {

    public enum Size {
        BIG_HEAD,
        SMALL_HEAD,
        BIG_HAND,
        SMALL_HAND,
    }

    private static final Vector BIG_HEAD_DISPLACE = new Vector(
            0.0d,
            (5.0d / 16.0d) - 2.0d,
            0.0d
    );

    private static final Vector SMALL_HEAD_DISPLACE = new Vector(
            0.0d,
            (3.0d / 32.0d) - 1.0d,
            0.0d
    );

    private static final Vector BIG_HAND_DISPLACE = new Vector(
            (6.0d / 16.0d) / 16.0d - 11.0d / 16.0d - 2.0d,
            (2.5d / 16.0d) - 1.0d,
            -(1.0d / 8.0d) / 16.0d - 4.0d / 16.0d
    );

    private static final Vector SMALL_HAND_DISPLACE = new Vector(
            (1.0d / 4.0d - 1.0d / 16.0d) / 16.0d - 5.5d / 16.0d,
            (2.5d / 16.0d) - 1.0d,
            -(1.0d / 8.0d) / 16.0d - 4.0d / 16.0d
    );

    private static final Vector BIG_HEAD_PIVOT = new Vector(
            0.0d,
            23.0d / 16.0d,
            0.0d
    );

    private static final Vector BIG_LEFT_ARM_PIVOT = new Vector(
            5.0d,
            22.0d / 16.0d,
            0.0d
    );

    private ArmorStand armorStand;
    private Size size;

    public ArmorStandObject(ItemStack item, Location loc, Size size) {
        if (loc.getWorld() == null) return;
        armorStand = loc.getWorld().spawn(loc, ArmorStand.class);

        switch (size) {
            case SMALL_HEAD:
                armorStand.setSmall(true);
            case BIG_HEAD:
                armorStand.setHeadPose(new EulerAngle(0.0d, 0.0d, 0.0d));
                if (armorStand.getEquipment() != null) armorStand.getEquipment().setItemInOffHand(item);
                break;
            case SMALL_HAND:
                armorStand.setSmall(true);
            case BIG_HAND:
                armorStand.setArms(true);
                armorStand.setLeftArmPose(new EulerAngle(-15.0d, -45.0d, 0.0d));
                if (armorStand.getEquipment() != null) armorStand.getEquipment().setHelmet(item);
                break;
        }

        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        this.size = size;
        setLocation(loc);
    }

    /**
     * Sets the location of the armor stand object, accounting for displacement.
     *
     * @param location the location to set the object to.
     */
    public void setLocation(Location location) {
        switch (size) {
            case BIG_HEAD:
                armorStand.teleport(location.add(BIG_HEAD_DISPLACE));
                break;
            case SMALL_HEAD:
                armorStand.teleport(location.add(SMALL_HEAD_DISPLACE));
                break;
            case BIG_HAND:
                armorStand.teleport(location.add(BIG_HAND_DISPLACE));
                break;
            case SMALL_HAND:
                armorStand.teleport(location.add(SMALL_HAND_DISPLACE));
                break;
        }
    }

    public Location convertLocation(Location location, EulerAngle angle) {

    }

}
