package net.emuman.spigotutils;

import net.emuman.spigotutils.menu.BasicMenu;
import net.emuman.spigotutils.menu.MenuManager;
import net.emuman.spigotutils.menu.MenuPage;
import net.emuman.spigotutils.menu.buttons.*;
import net.emuman.spigotutils.nms.PacketHandler;
import net.emuman.spigotutils.testcommands.*;
import net.emuman.spigotutils.testlisteners.ClickNPCHandler;
import net.emuman.spigotutils.testlisteners.PlayerJoinQuitHandler;
import net.emuman.spigotutils.worlds.EmptyGenerator;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SpigotUtilsTestPlugin extends JavaPlugin {

    private BasicMenu testMenu;
    private World testWorld;

    @Override
    public void onEnable() {
        PacketHandler.setPlugin(this);

        MenuManager menuManager = new MenuManager();
        testMenu = new BasicMenu();
        menuManager.addMenu(testMenu);
        MenuPage menuPage1 = testMenu.addPage(3, "Test Menu");
        menuPage1.addButton(new CloseButton(menuPage1), 8, 2);
        menuPage1.addButton(new MenuButton(menuPage1, new ItemStackBuilder(Material.SKELETON_SKULL)
                .setName(ChatColor.RED + "Death").build()) {
            @Override
            public void onClick(InventoryClickEvent event) {
                event.getWhoClicked().setHealth(0.0d);
            }
        }, 3, 1);
        Map<ItemStack, Integer> diamondCost = new HashMap<>();
        diamondCost.put(new ItemStack(Material.IRON_INGOT), 3);
        diamondCost.put(new ItemStack(Material.GOLD_INGOT), 1);
        menuPage1.addButton(new ItemShopButton(menuPage1,
                new ItemStackBuilder(Material.DIAMOND)
                        .addEnchant(Enchantment.LURE, 1, false).addItemFlags(ItemFlag.HIDE_ENCHANTS).build(),
                new ItemStack(Material.DIAMOND),
                diamondCost), 5, 1);

        MenuPage menuPage2 = testMenu.addPage(3, "Armor Color");
        menuPage2.addButton(new CloseButton(menuPage2), 8, 2);
        menuPage2.addButton(new MenuButton(menuPage2, new ItemStackBuilder(Material.BLUE_WOOL).setName(ChatColor.BLUE + "Blue Armor").build()) {
            @Override
            public void onClick(InventoryClickEvent event) {
                event.getWhoClicked().getInventory().setHelmet(
                        new ItemStackBuilder(ItemStackBuilder.newLeatherArmor(Material.LEATHER_HELMET, Color.BLUE))
                                .addEnchant(Enchantment.WATER_WORKER, 1, false).build());
            }
        }, 3, 1);
        menuPage2.addButton(new MenuButton(menuPage2, new ItemStackBuilder(Material.RED_WOOL).setName(ChatColor.RED + "Red Armor").build()) {
            @Override
            public void onClick(InventoryClickEvent event) {
                event.getWhoClicked().getInventory().setHelmet(
                        new ItemStackBuilder(ItemStackBuilder.newLeatherArmor(Material.LEATHER_HELMET, Color.RED))
                                .addEnchant(Enchantment.WATER_WORKER, 1, false).build());
            }
        }, 5, 1);

        menuPage1.addButton(new DummyButton(menuPage1, new ItemStack(Material.GREEN_STAINED_GLASS_PANE)), 0, 0);
        menuPage1.addButton(new PageSelectButton(menuPage1, new ItemStack(Material.RED_STAINED_GLASS_PANE), menuPage2), 1, 0);

        menuPage2.addButton(new PageSelectButton(menuPage2, new ItemStack(Material.RED_STAINED_GLASS_PANE), menuPage1), 0, 0);
        menuPage2.addButton(new DummyButton(menuPage2, new ItemStack(Material.GREEN_STAINED_GLASS_PANE)), 1, 0);


        WorldCreator worldCreator = new WorldCreator("endtimes");
        worldCreator.environment(World.Environment.THE_END);
        worldCreator.generator(new EmptyGenerator());
        testWorld = worldCreator.createWorld();
        assert testWorld != null;
        testWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        testWorld.setGameRule(GameRule.KEEP_INVENTORY, true);
        testWorld.setTime(18000);

        this.getCommand("testsidebar").setExecutor(new TestSidebarCommand(this));
        this.getCommand("testnpc").setExecutor(new TestNPCCommand());
        this.getCommand("testuuid").setExecutor(new TestUUIDCommand());
        this.getCommand("testworld").setExecutor(new TestWorldCommand(testWorld));
        this.getCommand("testasm").setExecutor(new TestASMCommand(this));
        this.getCommand("testvillager").setExecutor(new TestVillagerCommand());

        this.getServer().getPluginManager().registerEvents(new PlayerJoinQuitHandler(), this);
        this.getServer().getPluginManager().registerEvents(new ClickNPCHandler(), this);
        this.getServer().getPluginManager().registerEvents(menuManager, this);

//        JavaPlugin plugin = this;
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                System.out.println(Bukkit.getWorlds());
//                World newWorld = WorldUtils.fromExisting("D:\\Minecraft Servers\\Spigot\\1.16.5\\testworld", "yuhmoment");
//                if (newWorld != null) {
//                    for (Player player : Bukkit.getOnlinePlayers()) {
//                        player.teleport(newWorld.getSpawnLocation());
//                    }
//                }
//                System.out.println(Bukkit.getWorlds());
//            }
//        }.runTaskLater(this, 100L);
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                System.out.println(Bukkit.getWorlds());
//                for (Player player : Bukkit.getOnlinePlayers()) {
//                    player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
//                }
//                WorldUtils.remove("yuhmoment");
//                System.out.println(Bukkit.getWorlds());
//            }
//        }.runTaskLater(this, 400L);




        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketHandler.inject(player);
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketHandler.uninject(player);
        }
    }

    public BasicMenu getTestMenu() {
        return testMenu;
    }

    public World getTestWorld() {
        return testWorld;
    }

}
