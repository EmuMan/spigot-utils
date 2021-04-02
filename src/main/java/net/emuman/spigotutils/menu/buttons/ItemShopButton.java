package net.emuman.spigotutils.menu.buttons;

import net.emuman.spigotutils.menu.MenuPage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple button that allows for items to be purchased for other items.
 */
public class ItemShopButton extends MenuButton {

    private final ItemStack shopItem;
    private final Map<ItemStack, Integer> itemCosts;


    /**
     * Creates a new MenuButton.
     *
     * @param page        the page that the button is on.
     * @param displayItem the item displayed as the button.
     * @param shopItem    the item that is purchased with the button.
     * @param itemCosts   the cost of the item, in [item, amount] pairs.
     */
    public ItemShopButton(MenuPage page, ItemStack displayItem, ItemStack shopItem, Map<ItemStack, Integer> itemCosts) {
        super(page, displayItem);
        this.shopItem = shopItem;
        this.itemCosts = itemCosts;
    }

    /**
     * Called by the menu page when this button is clicked. Should generally not be called by user.
     *
     * @param event the InventoryClickEvent associated with the click.
     */
    @Override
    public void onClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        for (Map.Entry<ItemStack, Integer> cost : itemCosts.entrySet()) {
            if (!player.getInventory().containsAtLeast(cost.getKey(), cost.getValue())) return;
        }
        player.getInventory().removeItem(itemCosts.entrySet().stream().map(e -> {
            ItemStack price = e.getKey().clone();
            price.setAmount(e.getValue());
            return price;
        }).toArray(ItemStack[]::new));
        HashMap<Integer, ItemStack> leftovers = player.getInventory().addItem(shopItem);
        leftovers.values().forEach(itemStack -> player.getWorld().dropItem(player.getLocation(), itemStack));
    }

}
