package net.emuman.spigotutils.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class NMSBase {

    protected Class<?> getNMSClass(String className) {
        try {
            return Class.forName("net.minecraft.server." +
                    Bukkit.getServer().getClass().getName().split("\\.")[3] + "." + className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendPacket(Player target, Object packet) {
        try {
            Object handle = target.getClass().getMethod("getHandle").invoke(target);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet"))
                    .invoke(playerConnection, packet);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
