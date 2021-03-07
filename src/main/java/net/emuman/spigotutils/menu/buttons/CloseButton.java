package net.emuman.spigotutils.menu.buttons;

import net.emuman.spigotutils.items.ItemManager;
import net.emuman.spigotutils.menu.MenuPage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
     * Creates a new CloseButton.
     *
     * The display item is a barrier with the name "Close" in red text, bolded.
     *
     * @param page the page that the button is on.
     */
    public CloseButton(MenuPage page) {
        super(page, ItemManager.createItem(ChatColor.RED.toString() + ChatColor.BOLD + "Close", Material.BARRIER));
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.getWhoClicked().closeInventory();
    }

}
