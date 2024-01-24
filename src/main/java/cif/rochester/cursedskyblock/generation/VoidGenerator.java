package cif.rochester.cursedskyblock.generation;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class VoidGenerator extends ChunkGenerator {
    @Override
    public @NotNull ChunkData createVanillaChunkData(@NotNull World world, int x, int z) {
        return Bukkit.getServer().createChunkData(world);
    }
}
