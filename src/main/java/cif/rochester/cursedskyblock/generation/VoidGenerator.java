package cif.rochester.cursedskyblock.generation;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    @Override
    public @NotNull ChunkData createVanillaChunkData(@NotNull World world, int x, int z) {
        return Bukkit.getServer().createChunkData(world);
    }

    @Override
    public @Nullable Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
        return new Location(world,0,85,0);
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

    public void generateBedrock(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull ChunkData chunkData) {
        Vector spawn = new Vector(0,80,0);
        if(worldInfo.getEnvironment() == World.Environment.NORMAL){
            int cx = spawn.getBlockX()/16;
            int cz = spawn.getBlockZ()/16;
            if(cx == x && cz == z){
                OverworldIslandGenerator.generate(spawn, chunkData);
            }
        }
    }
}
