package net.emuman.spigotutils.armorstands;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private List<ArmorStandObject> objects;

    public Model() {
        this.objects = new ArrayList<>();
    }

    /**
     * Adds an ArmorStandObject to the model.
     *
     * @param object the ArmorStandObject to add to the model.
     */
    public void addObject(ArmorStandObject object) {
        objects.add(object);
    }

    /**
     * Adds an ArmorStandObject to the model.
     *
     * @param location the location of the ArmorStandObject.
     * @param size     the size of the ArmorStandObject.
     */
    public void addObject(Location location, ArmorStandObject.Size size) {
        objects.add(new ArmorStandObject(location, size));
    }

}
