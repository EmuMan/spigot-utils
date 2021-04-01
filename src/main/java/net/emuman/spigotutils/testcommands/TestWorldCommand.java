package net.emuman.spigotutils.testcommands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestWorldCommand implements CommandExecutor {

    private final World world;

    public TestWorldCommand(World world) {
        this.world = world;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            ((Player) sender).teleport(world.getSpawnLocation());
        }
        return true;
    }

}
