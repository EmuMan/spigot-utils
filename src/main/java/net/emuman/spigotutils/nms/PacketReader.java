package net.emuman.spigotutils.nms;

import net.minecraft.server.v1_16_R3.Packet;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Abstract class to define an object that receives packet data.
 */
public abstract class PacketReader {

    /**
     * Abstract method called by PacketHandler when a packet is received.
     *
     * @param player the player that sent the packet.
     * @param packet the packet that was sent.
     */
    public abstract void readPacket(Player player, Packet<?> packet);

    /**
     * Helper method using reflections to gain access to private fields of an object instance.
     *
     * @param instance the object instance to access.
     * @param name     the name of the target private field.
     * @return         whatever is contained in the field, null if no such field was found.
     */
    protected Object getValue(Object instance, String name) {
        Object result = null;

        try {
            // Have to use reflections for private fields
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
