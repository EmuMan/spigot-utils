package net.emuman.spigotutils.menu.buttons;

import net.emuman.spigotutils.menu.MenuPage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class NextPageButton extends MenuButton {

    private boolean wrap;

    /**
     * Creates a new NextPageButton.
     *
     * @param page        the page that the button is on.
     * @param displayItem the item displayed as the button.
     * @param wrap        whether to wrap the page selection (last page -> first page).
     */
    public NextPageButton(MenuPage page, ItemStack displayItem, boolean wrap) {
        super(page, displayItem);
        this.wrap = wrap;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        MenuPage nextPage = getPage().nextPage(wrap);
        if (nextPage != null) {
            event.getWhoClicked().openInventory(nextPage.getInv());
        }
    }

}
