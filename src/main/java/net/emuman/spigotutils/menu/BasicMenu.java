package net.emuman.spigotutils.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

public class BasicMenu implements Listener {

    private List<MenuPage> pages;

    public BasicMenu() {
        this.pages = new ArrayList<>();
    }

    /**
     * Creates a new page and adds it to the menu.
     *
     * @param rows  the number of rows in the new page.
     * @param title the name of the page.
     * @return      the index of the newly created page.
     */
    public int addPage(int rows, String title) {
        assert rows > 0 && rows <= 6;
        pages.add(new MenuPage(this, rows, title));
        return pages.size() - 1;
    }

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
     * Gets the size of the menu, in pages.
     *
     * @return the number of pages in the menu.
     */
    public int getSize() {
        return pages.size();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        // If there is a page with the clicked inventory, send the event to that page.
        for (MenuPage page : pages) {
            if (event.getClickedInventory().equals(page.getInv())) {
                page.onClick(event);
                return;
            }
        }
    }

}
