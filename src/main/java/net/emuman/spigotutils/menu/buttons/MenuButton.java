package net.emuman.spigotutils.menu.buttons;

import net.emuman.spigotutils.menu.MenuPage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }

    public MenuPage getPage() {
        return page;
    }

    public abstract void onClick(InventoryClickEvent event);

}
