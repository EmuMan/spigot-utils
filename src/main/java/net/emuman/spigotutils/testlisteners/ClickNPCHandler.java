package net.emuman.spigotutils.testlisteners;

import net.emuman.spigotutils.SpigotUtilsTestPlugin;
import net.emuman.spigotutils.npc.ClickNPCEvent;
import net.emuman.spigotutils.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ClickNPCHandler implements Listener {

    @EventHandler
    public void onClickNPC(ClickNPCEvent e) {
        if (e.isAttack()) {
            e.getNPC().despawnForPlayer(e.getPlayer());
        } else {
            e.getPlayer().openInventory(SpigotUtilsTestPlugin.getPlugin(SpigotUtilsTestPlugin.class).getTestMenu().getPage(0).getInv());
        }
    }

}
