package net.emuman.spigotutils.testcommands;

import net.emuman.spigotutils.SpigotUtilsTestPlugin;
import net.emuman.spigotutils.armorstands.ArmorStandLocation;
import net.emuman.spigotutils.armorstands.ArmorStandModel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TestASMCommand implements CommandExecutor {

    private JavaPlugin plugin;

    public TestASMCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        ArmorStandModel model = ArmorStandModel.fromJSON(SpigotUtilsTestPlugin.getPlugin(SpigotUtilsTestPlugin.class),
                new ArmorStandLocation(player.getLocation()),
                "D:\\Minecraft Servers\\Spigot\\armor_stand_models\\test_model.json");
        new BukkitRunnable() {
            @Override
            public void run() {
                ArmorStandLocation loc = model.getLocation();
                model.setLocation(new ArmorStandLocation(player.getLocation()).add(0.0d, -(5.0d / 16.0d), 0.0d));
            }
        }.runTaskTimer(plugin, 0L, 1L);
        player.sendMessage("You have been given a pedestal.");
        return true;
    }

}
