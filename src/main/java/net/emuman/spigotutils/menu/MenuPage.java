package net.emuman.spigotutils.menu;

import net.emuman.spigotutils.menu.buttons.MenuButton;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class MenuPage {

    private Inventory inv;

    private BasicMenu menu;
    private Map<Integer, MenuButton> buttons;
    private int rows;

    public MenuPage(BasicMenu menu, int rows, String title) {
        this.menu = menu;
        this.rows = rows;
        this.inv = Bukkit.createInventory(null, rows * 9, title);
    }

    /**
     * Adds a button to the menu page.
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

    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        if (event.getCurrentItem() == null) return;
        MenuButton button = buttons.get(event.getSlot());
        if (button != null) button.onClick(event);
    }

    public Inventory getInv() {
        return inv;
    }

    public BasicMenu getMenu() {
        return menu;
    }

    public int getRows() {
        return rows;
    }

    public int getSize() {
        return inv.getSize();
    }
}
