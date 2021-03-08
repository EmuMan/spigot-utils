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

    private ArmorStand armorStand;
    private Size size;

    public ArmorStandObject(ItemStack item, Location loc, Size size) {
        if (loc.getWorld() == null) return;
        armorStand = loc.getWorld().spawn(loc, ArmorStand.class);
        this.size = size;

        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
    }

    /**
     * Sets the location of the armor stand object, accounting for displacement.
     *
     * @param location the location to set the object to.
     */
    public void setLocation(Location location) {
    }

    public Location convertLocation(Location location, EulerAngle angle) {
        return null;
    }

}
