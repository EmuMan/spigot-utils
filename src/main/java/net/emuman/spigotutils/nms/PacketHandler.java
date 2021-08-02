package net.emuman.spigotutils.nms;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.emuman.spigotutils.npc.NPCPacketReader;
import net.minecraft.network.protocol.Packet;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Handles incoming packets from every client and distributes them accordingly to the PacketReaders specified in PacketHandler#getReaders.
 */
public class PacketHandler {

    private static final Map<UUID, Channel> channels = new HashMap<>();
    private static List<PacketReader> readers = null;

    private static JavaPlugin plugin;

    /**
     * Starts listening for packets coming from the specified player's connection.
     *
     * @param player the player whose connection should be listened to.
     */
    public static void inject(Player player) {
        if (readers == null) return;
        CraftPlayer craftPlayer = (CraftPlayer) player;

        // uhhhh i sure hope this works
        Channel channel = craftPlayer.getHandle().b.a.k;

        channels.put(player.getUniqueId(), channel);

        // There is already something listening or something idk
        if (channel.pipeline().get("PacketInjector") != null) return;

        // CodedRed says that using this sort of generic packet causes a lot more lag, but I couldn't see any such
        // difference when testing using timings. If there is something I am missing, please let me know.
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {

            @Override
            protected void decode(ChannelHandlerContext context, Packet<?> packet, List<Object> args) {
                args.add(packet);
                for (PacketReader reader : readers) {
                    reader.readPacket(player, packet);
                }
            }

        });
    }

    /**
     * Stops listening for packets coming from the specified player's connection.
     *
     * @param player the player whose connection should no longer be listened to.
     */
    public static void uninject(Player player) {
        Channel channel = channels.get(player.getUniqueId());
        if (channel != null && channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
        }
    }

    /**
     * @return the list of readers to pass packet information on to.
     */
    private static List<PacketReader> getReaders() {
        return Arrays.asList(new NPCPacketReader(plugin));
    }

    /**
     * If any custom event listeners are used, this must be called in the plugin's onEnable.
     *
     * @param plugin the plugin that is associated with the reception of the incoming traffic.
     */
    public static void setPlugin(JavaPlugin plugin) {
        PacketHandler.plugin = plugin;
        readers = getReaders();
    }
}
