package net.emuman.spigotutils.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * A manager class that listens for InventoryClickEvents and passes them onto the menus it manages. Should be registered as a listener in onEnable.
 */
public class MenuManager implements Listener {

    private List<BasicMenu> menus;

    /**
     * Creates a new MenuManager.
     */
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

    /**
     * Simple onClick listener, called by Bukkit if instance is registered.
     *
     * @param event the InventoryClickEvent associated with the click.
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        // Send the event to every menu
        for (BasicMenu menu : menus) {
            menu.onClick(event);
        }
    }

}
