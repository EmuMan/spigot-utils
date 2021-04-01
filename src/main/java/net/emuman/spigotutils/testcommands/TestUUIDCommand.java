package net.emuman.spigotutils.testcommands;

import io.github.emuman.commandbuilder.CommandTraceLog;
import io.github.emuman.commandbuilder.EndNode;
import io.github.emuman.commandbuilder.NodeBase;
import io.github.emuman.commandbuilder.SingleStringNode;
import io.github.emuman.commandbuilder.exceptions.CommandStructureException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TestUUIDCommand implements CommandExecutor {

    private NodeBase nodeBase;

    public TestUUIDCommand() {
        nodeBase = new SingleStringNode("name");
        nodeBase.addNode(new EndNode("end", values -> {
            String uuid = Bukkit.getOfflinePlayer((String) values.get("name")).getUniqueId().toString();
            ((CommandSender) values.get("sender")).sendMessage(uuid);
            System.out.println(uuid);
        }));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Map<String, Object> values = new HashMap<>();
        values.put("sender", sender);

        CommandTraceLog log;

        try {
            log = nodeBase.run(args, values);
        } catch (CommandStructureException e) {
            sender.sendMessage("There is something wrong with the command structure. Please contact the developer.");
            return true;
        }

        if (log.getReturnCode() != CommandTraceLog.ReturnCode.SUCCESS) {
            sender.sendMessage(ChatColor.RED + "Usage: /testuuid " + log.getTraceString() + (log.getMessage() == null ? "" : " (" + log.getMessage() + ")"));
        }

        return true;
    }

}
