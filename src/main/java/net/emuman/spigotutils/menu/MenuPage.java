package net.emuman.spigotutils.menu;

import net.emuman.spigotutils.menu.buttons.MenuButton;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a page in a given menu, can contain numerous buttons.
 */
public class MenuPage {

    private final Inventory inv;

    private final BasicMenu menu;
    private final Map<Integer, MenuButton> buttons;
    private final int rows;

    /**
     * Creates a new MenuPage.
     *
     * @param menu  the menu that this page belongs to.
     * @param rows  the number of rows in this page (must be between 1 and 6, both inclusive).
     * @param title the display title of this menu page.
     */
    public MenuPage(BasicMenu menu, int rows, String title) {
        this.menu = menu;
        this.rows = rows;
        this.inv = Bukkit.createInventory(null, rows * 9, title);
        this.buttons = new HashMap<>();
    }

    /**
     * Adds a button to the menu page. Can be used to overwrite existing buttons.
     *
     * Coordinates start at the top left.
     *
     * @param button the button to add to the page.
     * @param x      the x-coordinate of the button, starting from 0.
     * @param y      the y-coordinate of the button, starting from 0.
     */
    public void addButton(MenuButton button, int x, int y) {
        if (x < 0 || x >= 9 || y < 0 || y >= rows) return;
        buttons.put(x + y * 9, button);
        inv.setItem(x + y * 9, button.getDisplayItem());
    }

    /**
     * Returns the page after this one.
     *
     * If wrap is set to true, the last page will point to the first. Else, null will be returned.
     *
     * @param wrap whether or not to wrap the output.
     * @return     the next page.
     */
    public MenuPage nextPage(boolean wrap) {
        return getMenu().pageAfter(this, wrap);
    }

    /**
     * Returns the page before this one.
     *
     * If wrap is set to true, the first page will point to the last. Else, null will be returned.
     *
     * @param wrap whether or not to wrap the output.
     * @return     the next page.
     */
    public MenuPage previousPage(boolean wrap) {
        return getMenu().pageBefore(this, wrap);
    }

    /**
     * Called by the menu when an inventory is clicked. Should generally not be called by user.
     *
     * @param event the InventoryClickEvent associated with the click.
     */
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory().equals(getInv())) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null) return;
            MenuButton button = buttons.get(event.getSlot());
            if (button != null) button.onClick(event);
        } else {
            // This inventory is open, but it is not the one that was clicked.
            if (event.getClick().isShiftClick()) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * This is the call that should be made when opening the menu page for a given player (Player#openInventory(MenuPage#getInv)).
     *
     * @return the Inventory that this page wraps around.
     */
    public Inventory getInv() {
        return inv;
    }

    /**
     * @return the menu that this page belongs to.
     */
    public BasicMenu getMenu() {
        return menu;
    }

    /**
     * @return the number of rows in this page.
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return the size of the inventory (rows * columns).
     */
    public int getSize() {
        return inv.getSize();
    }
}
