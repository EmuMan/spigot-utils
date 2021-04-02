package net.emuman.spigotutils.testcommands;

import net.emuman.spigotutils.npc.VillagerBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;

public class TestVillagerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        VillagerBuilder builder = new VillagerBuilder("Test Villager", Villager.Type.DESERT, Villager.Profession.LIBRARIAN);
        builder.addTrade(new ItemStack(Material.DIAMOND), Arrays.asList(new ItemStack(Material.GOLD_INGOT), new ItemStack(Material.IRON_INGOT, 3)));
        builder.addTrade(new ItemStack(Material.GOLD_INGOT), Collections.singletonList(new ItemStack(Material.IRON_INGOT, 6)));
        builder.spawnVillager(player.getLocation());
        return true;
    }

}
