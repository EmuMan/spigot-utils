package net.emuman.spigotutils.menu.buttons;

import net.emuman.spigotutils.menu.MenuPage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PreviousPageButton extends MenuButton {

    private boolean wrap;

    /**
     * Creates a new PreviousPageButton.
     *
     * @param page        the page that the button is on.
     * @param displayItem the item displayed as the button.
     * @param wrap        whether to wrap the page selection (first page -> last page).
     */
    public PreviousPageButton(MenuPage page, ItemStack displayItem, boolean wrap) {
        super(page, displayItem);
        this.wrap = wrap;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        MenuPage previousPage = getPage().previousPage(wrap);
        if (previousPage != null) {
            event.getWhoClicked().openInventory(previousPage.getInv());
        }
    }

}
