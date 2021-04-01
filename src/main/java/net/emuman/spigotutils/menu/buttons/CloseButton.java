package net.emuman.spigotutils.menu.buttons;

import net.emuman.spigotutils.items.ItemStackBuilder;
import net.emuman.spigotutils.menu.MenuPage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A simple button that closes the menu.
 */
public class CloseButton extends MenuButton {

    /**
     * Creates a new CloseButton.
     *
     * @param page        the page that the button is on.
     * @param displayItem the item displayed as the button.
     */
    public CloseButton(MenuPage page, ItemStack displayItem) {
        super(page, displayItem);
    }

    /**
     * Creates a new CloseButton with the display item set to a barrier with the name "Close" in red text, bolded.
     *
     * @param page the page that the button is on.
     */
    public CloseButton(MenuPage page) {
        super(page, new ItemStackBuilder(Material.BARRIER).setName(ChatColor.RED + "" + ChatColor.BOLD + "Close").build());
    }

    /**
     * Called by the menu page when this button is clicked. Should generally not be called by user.
     *
     * @param event the InventoryClickEvent associated with the click.
     */
    @Override
    public void onClick(InventoryClickEvent event) {
        event.getWhoClicked().closeInventory();
    }

}
