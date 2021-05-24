package net.emuman.spigotutils;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomLoot {

    /**
     * Helper data class to store information about a weighted item entry.
     */
    private static class Entry {
        public final ItemStack itemStack;
        public final double weight;
        public final int lowerBound;
        public final int upperBound;

        public Entry(ItemStack itemStack, double weight, int lowerBound, int upperBound) {
            this.itemStack = itemStack;
            this.weight = weight;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }
    }

    private final List<Entry> entries;
    private final Random random;

    /**
     * Creates a new RandomLoot instance.
     */
    public RandomLoot() {
        entries = new ArrayList<>();
        random = new Random();
    }

    /**
     * Adds a new weighted item entry to the random loot pool.
     *
     * @param itemStack  the item that will be selected.
     * @param weight     the probability weight of the item (does not need to be normalized).
     * @param lowerBound the lower bound of the item count.
     * @param upperBound the upper bound of the item count.
     */
    public void addEntry(ItemStack itemStack, double weight, int lowerBound, int upperBound) {
        entries.add(new Entry(itemStack, weight, lowerBound, upperBound));
    }

    /**
     * Gets the next item from the random pool.
     *
     * @return the randomly selected ItemStack, with a random amount applied.
     */
    public ItemStack getNext() {
        List<Pair<Entry, Double>> pairs = entries.stream().map(e -> new Pair<>(e, e.weight)).collect(Collectors.toList());
        Entry selected = new EnumeratedDistribution<>(pairs).sample();
        ItemStack itemStack = selected.itemStack.clone();
        // + 1 to make the upper bound inclusive
        itemStack.setAmount(random.nextInt(selected.upperBound - selected.lowerBound + 1) + selected.lowerBound);
        return itemStack;
    }

}
