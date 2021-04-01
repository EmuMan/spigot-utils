package net.emuman.spigotutils.npc;

import net.emuman.spigotutils.SpigotUtilsTestPlugin;
import net.emuman.spigotutils.nms.PacketReader;
import net.minecraft.server.v1_16_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Reads incoming packets. Used by PacketHandler. Should almost never be instantiated by user.
 */
public class NPCPacketReader extends PacketReader {

    private final JavaPlugin plugin;

    /**
     * Creates a new NPCPacketReader. Called by PacketHandler#getReaders. Should not be called by user.
     *
     * @param plugin the plugin associated with the packet reader.
     */
    public NPCPacketReader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Called by PacketHandler when a packet comes in from any player.
     *
     * @param player the player who sent the packet.
     * @param packet the packet that was sent.
     */
    @Override
    public void readPacket(Player player, Packet<?> packet) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            Object hand = getValue(packet, "d");
            if (hand != null && hand.toString().equalsIgnoreCase("OFF_HAND")) return;
            String action = getValue(packet, "action").toString();
            if (action.equalsIgnoreCase("INTERACT_AT")) return;
            boolean isAttack;
            if (action.equalsIgnoreCase("ATTACK")) isAttack = true;
            else if (action.equalsIgnoreCase("INTERACT")) isAttack = false;
            else return;

            int id = (int) getValue(packet, "a");
            for (NPC npc : NPC.getAllNPCs()) {
                if (npc.getEntityPlayer().getId() == id) {
                    // Idk why it is done this way but CodedRed did it like this and so will I
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getPluginManager().callEvent(new ClickNPCEvent(player, npc, isAttack));
                        }
                    }.runTaskLater(plugin, 0);
                }
            }
        }
    }

}
