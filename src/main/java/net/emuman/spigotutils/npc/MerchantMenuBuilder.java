package net.emuman.spigotutils.npc;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

public class MerchantMenuBuilder {

    protected final ArrayList<MerchantRecipe> trades;
    protected final String name;

    /**
     * Creates a new MerchantMenuBuilder.
     *
     * @param name the name of the merchant menu that will be created.
     */
    public MerchantMenuBuilder(String name) {
        this.trades = new ArrayList<>();
        this.name = name;
    }

    /**
     * Adds a new trade to the merchant menu.
     *
     * @param result      the result of the trade (what is given to the player).
     * @param ingredients the required ingredients for the trade (what the player gives, can only have a size of 1 or 2).
     * @return the same MerchantMenu for consecutive operations.
     */
    public MerchantMenuBuilder addTrade(ItemStack result, List<ItemStack> ingredients) {
        MerchantRecipe recipe = new MerchantRecipe(result, 0, Integer.MAX_VALUE / 2, false, 0, 0.0f);
        recipe.setIngredients(ingredients);
        trades.add(recipe);
        return this;
    }

    /**
     * Creates the new merchant object with the set trades.
     *
     * @return the created merchant object.
     */
    public Merchant createMerchant() {
        Merchant merchant = Bukkit.createMerchant(name);
        merchant.setRecipes(trades);
        return merchant;
    }

    /**
     * @return the list of trades that the merchant menu contains.
     */
    public ArrayList<MerchantRecipe> getTrades() {
        return trades;
    }

    /**
     * @return the name of the merchant menu.
     */
    public String getName() {
        return name;
    }

}
