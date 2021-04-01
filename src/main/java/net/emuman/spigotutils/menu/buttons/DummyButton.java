package net.emuman.spigotutils.menu.buttons;

import net.emuman.spigotutils.menu.MenuPage;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * A simple button that does nothing upon clicking it.
 */
public class DummyButton extends MenuButton {

    /**
     * Creates a new DummyButton.
     *
     * @param page        the page that the button is on.
     * @param displayItem the item displayed as the button.
     */
    public DummyButton(MenuPage page, ItemStack displayItem) {
        super(page, displayItem);
    }

    /**
     * Called by the menu page when this button is clicked. Should generally not be called by user.
     *
     * @param event the InventoryClickEvent associated with the click.
     */
    @Override
    public void onClick(InventoryClickEvent event) {}

}
