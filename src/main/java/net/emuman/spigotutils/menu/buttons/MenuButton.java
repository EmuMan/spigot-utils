package net.emuman.spigotutils.menu.buttons;

import net.emuman.spigotutils.menu.MenuPage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Abstract class representing a button on an inventory menu.
 */
public abstract class MenuButton {

    private MenuPage page;
    private ItemStack displayItem;

    /**
     * Creates a new MenuButton.
     *
     * @param page        the page that the button is on.
     * @param displayItem the item displayed as the button.
     */
    public MenuButton(MenuPage page, ItemStack displayItem) {
        this.page = page;
        this.displayItem = displayItem;
    }

    /**
     * @return the ItemStack displayed as the button.
     */
    public ItemStack getDisplayItem() {
        return displayItem;
    }

    /**
     * @param displayItem the ItemStack to set the button display to.
     */
    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    /**
     * @return the page that this MenuButton is on.
     */
    public MenuPage getPage() {
        return page;
    }

    /**
     * Abstract class that is called by the menu page whenever this button is clicked. Should generally not be called by user.
     *
     * @param event the InventoryClickEvent associated with the click.
     */
    public abstract void onClick(InventoryClickEvent event);

}
