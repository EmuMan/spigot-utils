package net.emuman.spigotutils.testcommands;

import net.emuman.spigotutils.animatedtext.TextAnimation;
import net.emuman.spigotutils.animatedtext.TextMultiAnimation;
import net.emuman.spigotutils.animatedtext.TextWipeAnimation;
import net.emuman.spigotutils.gui.PartialDynamicSidebar;
import net.emuman.spigotutils.time.ControllableLoop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class TestSidebarCommand implements CommandExecutor {

    private PartialDynamicSidebar sidebar;
    private final JavaPlugin plugin;

    public TestSidebarCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void generateSidebar() {
        PartialDynamicSidebar sb = new PartialDynamicSidebar("test", "Animated Sidebar");
        sb.addLine(" ");
        sb.addLine("Entities: ");
        sb.setLineSuffix(1, () -> "§b" + Bukkit.getWorlds().get(0).getEntities().size());
        sb.addLine("  ");
        sb.addLine(ChatColor.YELLOW + "emuman.net");
        String titleText = "Emu Network";
        TextAnimation animation = new TextMultiAnimation(Arrays.asList(
                new TextWipeAnimation(titleText, ChatColor.RED, ChatColor.BLUE,
                        Arrays.asList(ChatColor.GOLD, ChatColor.WHITE, ChatColor.AQUA)),
                new TextWipeAnimation(titleText, ChatColor.BLUE, ChatColor.RED,
                        Arrays.asList(ChatColor.AQUA, ChatColor.WHITE, ChatColor.GOLD))
        ), TextMultiAnimation.OrderType.REPEAT_FORWARDS);
        new ControllableLoop(plugin, animation::getPeriod, () -> sb.setTitle(animation.getNext()));
        sb.runTaskTimer(plugin, 0, 5);
        sidebar = sb;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("testsidebar")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                generateSidebar();
                player.setScoreboard(sidebar.getBoard());
            }
            return true;
        }
        return false;
    }
}
