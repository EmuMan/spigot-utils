package net.emuman.spigotutils.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic menu that allows for several menu pages and button additions.
 */
public class BasicMenu implements Listener {

    private final List<MenuPage> pages;

    /**
     * Creates a new BasicMenu.
     */
    public BasicMenu() {
        this.pages = new ArrayList<>();
    }

    /**
     * Creates a new page and adds it to the menu.
     *
     * @param rows  the number of rows in the new page.
     * @param title the name of the page.
     * @return      the newly created page.
     */
    public MenuPage addPage(int rows, String title) {
        assert rows > 0 && rows <= 6;
        MenuPage newPage = new MenuPage(this, rows, title);
        pages.add(newPage);
        return newPage;
    }

    /**
     * Gets the page after the specified page, wrapping if specified.
     *
     * @param currentPage the specified current page.
     * @param wrap        true if wrapping (last page points to first page), false otherwise.
     * @return            the page after the specified current page (returns null if page is last and wrapping is false).
     */
    public MenuPage pageAfter(MenuPage currentPage, boolean wrap) {
        int index = pages.indexOf(currentPage);
        if (index == pages.size() - 1) {
            // This is the last page, either wrap around or return null.
            if (wrap) {
                return pages.get(0);
            } else {
                return null;
            }
        } else {
            return pages.get(index + 1);
        }
    }

    /**
     * Gets the page before the specified page, wrapping if specified.
     *
     * @param currentPage the specified current page.
     * @param wrap        true if wrapping (first page points to last page), false otherwise.
     * @return            the page before the specified current page (returns null if page is first and wrapping is false).
     */
    public MenuPage pageBefore(MenuPage currentPage, boolean wrap) {
        int index = pages.indexOf(currentPage);
        if (index == 0) {
            // This is the first page, either wrap around or return null.
            if (wrap) {
                return pages.get(pages.size() - 1);
            } else {
                return null;
            }
        } else {
            return pages.get(index - 1);
        }
    }

    /**
     * @return the number of pages in the menu.
     */
    public int getSize() {
        return pages.size();
    }

    /**
     * Gets the page with the specified index.
     *
     * @param index the index of the page to retrieve.
     * @return      the page with the specified index, null if not found.
     */
    public MenuPage getPage(int index) {
        return pages.get(index);
    }

    /**
     * Called by the MenuManager listener when an inventory is clicked. Generally does not need to be called manually.
     *
     * @param event the InventoryClickEvent associated with the click.
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        // If there is a page with the clicked inventory, send the event to that page.
        for (MenuPage page : pages) {
            if (event.getClickedInventory() != null &&
                    (event.getClickedInventory().equals(page.getInv()) ||
                            event.getWhoClicked().getOpenInventory().getTopInventory().equals(page.getInv()))) {
                page.onClick(event);
                return;
            }
        }
    }

}
