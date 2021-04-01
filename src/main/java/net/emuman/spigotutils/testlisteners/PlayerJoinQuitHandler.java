package net.emuman.spigotutils.testlisteners;

import net.emuman.spigotutils.nms.PacketHandler;
import net.emuman.spigotutils.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (NPC npc : NPC.getAllNPCs()) {
            npc.spawnForPlayer(e.getPlayer());
        }
        PacketHandler.inject(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        PacketHandler.uninject(e.getPlayer());
    }

}
