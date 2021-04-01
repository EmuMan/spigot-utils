package net.emuman.spigotutils.testcommands;

import net.emuman.spigotutils.SpigotUtilsTestPlugin;
import net.emuman.spigotutils.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestNPCCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("testnpc")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                NPC npc = new NPC(SpigotUtilsTestPlugin.getPlugin(SpigotUtilsTestPlugin.class),
                        player.getLocation(),
                        ChatColor.GREEN + "" + ChatColor.BOLD + "EmuNPC",
                        "EmuMan4");
                npc.spawnForPlayer(player);
            }
            return true;
        }
        return false;
    }

}
