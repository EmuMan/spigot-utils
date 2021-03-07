package net.emuman.spigotutils.menu.buttons;

import net.emuman.spigotutils.menu.MenuPage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PageSelectButton extends MenuButton {

    private MenuPage targetPage;

    /**
     * Creates a new MenuButton.
     *
     * @param page        the page that the button is on.
     * @param displayItem the item displayed as the button.
     * @param targetPage  the page that the button points to.
     */
    public PageSelectButton(MenuPage page, ItemStack displayItem, MenuPage targetPage) {
        super(page, displayItem);
        this.targetPage = targetPage;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.getWhoClicked().openInventory(targetPage.getInv());
    }

}
