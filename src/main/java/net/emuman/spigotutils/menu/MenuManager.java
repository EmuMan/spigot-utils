package net.emuman.spigotutils.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class MenuManager implements Listener {

    private List<BasicMenu> menus;

    public MenuManager() {
        menus = new ArrayList<>();
    }

    /**
     * Adds a menu to the menu manager.
     *
     * @param menu the menu to be added.
     */
    public void addMenu(BasicMenu menu) {
        menus.add(menu);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        // Send the event to every menu
        for (BasicMenu menu : menus) {
            menu.onClick(event);
        }
    }

}
