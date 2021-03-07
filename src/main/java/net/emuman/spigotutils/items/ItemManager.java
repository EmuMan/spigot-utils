package net.emuman.spigotutils.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemManager {

    private Map<String, ItemStack> items;

    public ItemManager() {
        items = new HashMap<>();
    }

    /**
     * Creates and returns a new ItemStack with the given information.
     *
     * @param name     the name of the item.
     * @param material the material of the item.
     * @param lore     the lore of the item (can be null).
     * @return         the created ItemStack.
     */
    public static ItemStack createItem(String name, Material material, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        if (lore != null) meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Creates and returns a new ItemStack with the given information, without any lore.
     *
     * @param name     the name of the item.
     * @param material the material of the item.
     * @return         the created ItemStack.
     */
    public static ItemStack createItem(String name, Material material) {
        return createItem(name, material, null);
    }

    /**
     * Creates and adds a new ItemStack to the manager.
     *
     * @param name     the name of the item.
     * @param id       the ID of the item.
     * @param material the material of the item.
     * @param lore     the lore of the item (can be null).
     * @return         the created ItemStack.
     */
    public ItemStack addItem(String name, String id, Material material, List<String> lore) {
        ItemStack itemStack = createItem(name, material, lore);
        items.put(id, itemStack);
        return itemStack;
    }

    /**
     * Creates and adds a new ItemStack to the manager, without any lore.
     *
     * @param name     the name of the item.
     * @param id       the ID of the item.
     * @param material the material of the item.
     * @return         the created ItemStack.
     */
    public ItemStack addItem(String name, String id, Material material) {
        return addItem(name, id, material, null);
    }

    /**
     * Gets a new ItemStack of the item with the given ID.
     *
     * @param id     the ID of the item to use.
     * @param amount the amount of the item to return.
     * @return       the new ItemStack.
     */
    public ItemStack getItem(String id, int amount) {
        if (!items.containsKey(id)) return null;
        ItemStack itemStack = new ItemStack(items.get(id));
        itemStack.setAmount(amount);
        return itemStack;
    }

}
