package net.emuman.spigotutils;

import net.emuman.spigotutils.animatedtext.*;
import net.emuman.spigotutils.gui.PartialDynamicSidebar;
import net.emuman.spigotutils.time.ControllableLoop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;

public class SpigotUtilsTestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
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
        ControllableLoop loop;
        loop = new ControllableLoop(this, animation::getPeriod, () -> sb.setTitle(animation.getNext()));
        sb.runTaskTimer(this, 0, 5);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(sb.getBoard());
        }
    }

}
