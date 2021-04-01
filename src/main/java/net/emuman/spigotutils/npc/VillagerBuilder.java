package net.emuman.spigotutils.npc;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows for the fast creation of villagers with custom attributes and trades.
 */
public class VillagerBuilder {

    private Villager villager;
    private final ArrayList<MerchantRecipe> trades;
    private final String name;
    private final Villager.Type type;
    private final Villager.Profession profession;

    /**
     * Creates a new VillagerBuilder. Does not yet spawn in the associated villager.
     *
     * @param name       the name of the villager that will be created.
     * @param type       the type of the villager that will be created.
     * @param profession the profession of the villager that will be created.
     */
    public VillagerBuilder(String name, Villager.Type type, Villager.Profession profession) {
        this.villager = null;
        this.trades = new ArrayList<>();
        this.name = name;
        this.type = type;
        this.profession = profession;
    }

    /**
     * Adds a new trade to the villager's menu.
     *
     * @param result      the result of the trade (what is given to the player).
     * @param ingredients the required ingredients for the trade (what the player gives, can only have a size of 1 or 2).
     * @return            the same VillagerBuilder for consecutive operations.
     */
    public VillagerBuilder addTrade(ItemStack result, List<ItemStack> ingredients) {
        MerchantRecipe recipe = new MerchantRecipe(result, 0, Integer.MAX_VALUE / 2, false, 0, 0.0f);
        recipe.setIngredients(ingredients);
        trades.add(recipe);
        return this;
    }

    /**
     * Spawns the villager to the given location, with all previously defined specifications.
     *
     * @param location the location to spawn the villager into.
     * @return         the Villager that was spawned in.
     */
    public Villager spawnVillager(Location location) {
        if (location.getWorld() == null) return null;
        villager = location.getWorld().spawn(location, Villager.class);
        villager.setCustomName(name);
        villager.setCustomNameVisible(true); // might be true by default but whatever
        villager.setVillagerType(type);
        villager.setProfession(profession);
        villager.setVillagerLevel(5);
        villager.setAI(false);
        villager.setCanPickupItems(false);
        villager.setInvulnerable(true);
        villager.setSilent(true);
        villager.setRecipes(trades);
        return villager;
    }

    /**
     * @return the Villager object that was last spawned in if it exists, null otherwise.
     */
    public Villager getVillager() {
        return villager;
    }

    /**
     * @return the list of trades that the VillagerBuilder contains.
     */
    public ArrayList<MerchantRecipe> getTrades() {
        return trades;
    }

    /**
     * @return the name of the villager.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the villager's specified type.
     */
    public Villager.Type getType() {
        return type;
    }

    /**
     * @return the villager's specified profession.
     */
    public Villager.Profession getProfession() {
        return profession;
    }

}
