package net.emuman.spigotutils.armorstands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A model containing several ArmorStandObject instances for ease of collective use.
 */
public class ArmorStandModel extends ArmorStandLocationUser {

    /**
     * A utility data class that contains both an ArmorStandObject and a displacement, in the form of an ArmorStandLocation.
     */
    private static class ASOLocPair {
        public ArmorStandObject aso;
        public ArmorStandLocation displacement;

        public ASOLocPair(ArmorStandObject aso, ArmorStandLocation displacement) {
            this.aso = aso;
            this.displacement = displacement;
        }
    }

    private List<ASOLocPair> objects;
    private ArmorStandLocation location;
    private static JsonParser jsonParser = new JsonParser();

    /**
     * Creates a new ArmorStandModel.
     *
     * @param plugin   the plugin this armor stand model runs on.
     * @param location the location for this armor stand model.
     */
    public ArmorStandModel(JavaPlugin plugin, ArmorStandLocation location) {
        super(plugin);
        this.location = location;
        this.objects = new ArrayList<>();
    }

    /**
     * Adds an ArmorStandObject to the model.
     *
     * @param object the ArmorStandObject to add to the model.
     */
    public void addObject(ArmorStandObject object, ArmorStandLocation displacement) {
        objects.add(new ASOLocPair(object, displacement));
    }

    /**
     * Adds an ArmorStandObject to the model.
     *
     * @param object the ArmorStandObject to add to the model.
     */
    public void addObject(ArmorStandObject object, Vector displacement) {
        objects.add(new ASOLocPair(object, new ArmorStandLocation(
                object.getLocation().getWorld(), displacement.getX(), displacement.getY(), displacement.getZ())));
    }

    /**
     * Adds an ArmorStandObject to the model.
     *
     * @param item         the item for the ArmorStandObject to be holding/wearing.
     * @param displacement the relative displacement of the ArmorStandObject.
     * @param size         the size of the ArmorStandObject.
     */
    public ArmorStandObject addObject(ItemStack item, ArmorStandLocation displacement, ArmorStandObject.Size size) {
        ArmorStandObject aso = new ArmorStandObject(plugin, item, getCompensatedLocation(displacement), size);
        objects.add(new ASOLocPair(aso, displacement));
        return aso;
    }

    /**
     * Loads an ArmorStandModel from JSON format. This is mainly to be used in conjunction with my Blender ArmorStand addon.
     *
     * @param plugin       the plugin that the new armor stand model runs on.
     * @param baseLocation the location for the armor stand model to be loaded in to.
     * @param filename     the filename (absolute path) for the .json that is loaded in.
     * @return             the ArmorStandModel generated from the provided JSON.
     */
    public static ArmorStandModel fromJSON(JavaPlugin plugin, ArmorStandLocation baseLocation, String filename) {
        ArmorStandModel model = new ArmorStandModel(plugin, baseLocation);
        try {
            File file = new File(filename);
            JsonObject jsonObject = jsonParser.parse(new FileReader(file)).getAsJsonObject();
            for (JsonElement e : jsonObject.get("objects").getAsJsonArray()) {
                JsonObject object = e.getAsJsonObject();
                Material material = Material.getMaterial(object.get("material").getAsString());
                if (material == null) continue;
                ItemStack itemStack = new ItemStack(material);
                JsonArray dispArray = object.get("displacement").getAsJsonArray();
                JsonArray rotArray = object.get("rotation").getAsJsonArray();
                ArmorStandLocation displacement = new ArmorStandLocation(
                        baseLocation.getWorld(),
                        dispArray.get(0).getAsDouble(),
                        dispArray.get(1).getAsDouble(),
                        dispArray.get(2).getAsDouble(),
                        (float) Math.toDegrees(rotArray.get(1).getAsDouble()),
                        (float) Math.toDegrees(rotArray.get(0).getAsDouble()),
                        (float) Math.toDegrees(rotArray.get(2).getAsDouble()));
                String sizeStr = object.get("size").getAsString();
                ArmorStandObject.Size size = ArmorStandObject.Size.valueOf(sizeStr);
                model.addObject(itemStack, displacement, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return model;
    }

    /**
     * @return a copy of the location of the armor stand model.
     */
    @Override
    public ArmorStandLocation getLocation() {
        return location.clone();
    }

    /**
     * @param location the new location of the armor stand model.
     */
    @Override
    public void setLocation(ArmorStandLocation location) {
        this.location = location;
        for (ASOLocPair asoloc : objects) {
            asoloc.aso.setLocation(getCompensatedLocation(asoloc.displacement));
        }
    }

    /**
     * Helper method to get new armor stand object locations based on their displacement from the model.
     *
     * Currently has some limitations documented within the function comments.
     *
     * @param displacement the displacement of the armor stand object relative to the armor stand model.
     * @return             the new absolute location of the armor stand object.
     */
    private ArmorStandLocation getCompensatedLocation(ArmorStandLocation displacement) {
        Vector dispVec = displacement.toVector()
                /*.rotateAroundX(Math.toRadians(location.getPitch()))*/
                .rotateAroundY(Math.toRadians(-location.getYaw()))
                /*.rotateAroundZ(Math.toRadians(-location.getRoll()))*/;
        ArmorStandLocation newLocation = getLocation().add(dispVec);
        // We need to apply the displacement rotation before we apply the model rotation, or else
        // things will happen in the wrong order. Therefore, vector math is the easiest path, I think.
        // The only issue with that is a vector cannot properly represent roll, as it is simply a direction.
        // Therefore, this does not work in many cases, and we are limited to only yaw rotation. I am working
        // on a solution.
//        Vector directionVec = displacement.getDirection()
//                .rotateAroundX(Math.toRadians(location.getPitch()))
//                .rotateAroundY(Math.toRadians(-location.getYaw()))
//                .rotateAroundZ(Math.toRadians(-location.getRoll()));
//        newLocation.setDirection(directionVec);
        newLocation.setRotation(
                displacement.getYaw() + location.getYaw(),
                displacement.getPitch(),
                displacement.getRoll()
        );
        return newLocation;
    }

}
