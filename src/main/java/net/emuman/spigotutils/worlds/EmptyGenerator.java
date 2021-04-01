package net.emuman.spigotutils.worlds;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * A class that uses an empty world generator, good for multiplayer maps where no terrain should exist.
 */
public class EmptyGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
        return createChunkData(world);
    }

}
