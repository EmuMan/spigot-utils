package net.emuman.spigotutils.npc;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;

/**
 * Allows for the fast creation of villagers with custom attributes and trades.
 */
public class VillagerBuilder extends MerchantMenuBuilder {

    private final ArrayList<MerchantRecipe> trades;
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
        super(name);
        this.trades = new ArrayList<>();
        this.type = type;
        this.profession = profession;
    }

    /**
     * Spawns the villager to the given location, with all previously defined specifications.
     *
     * @param location the location to spawn the villager into.
     * @return         the Villager that was spawned in.
     */
    public Villager spawnVillager(Location location) {
        if (location.getWorld() == null) return null;

        Villager villager = location.getWorld().spawn(location, Villager.class);
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
     * This method does not fit within the implementation of VillagerBuilder, and should therefore not be used.
     *
     * @throws UnsupportedOperationException since there is no implementation.
     */
    @Override
    public Merchant createMerchant() {
        throw new UnsupportedOperationException("This method is not available for this implementation. Use VillagerBuilder#spawnVillager instead.");
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
