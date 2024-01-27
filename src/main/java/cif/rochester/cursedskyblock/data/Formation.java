package cif.rochester.cursedskyblock.data;

import cif.rochester.cursedskyblock.CursedSkyblock;
import cif.rochester.cursedskyblock.WeightedList;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import java.util.*;

public class Formation {
    @Save
    public NamespacedKey blockKey = Material.COBBLESTONE.getKey();
    @Save
    public int minY = Integer.MIN_VALUE;
    @Save
    public int maxY = Integer.MAX_VALUE;
    @Save
    @Typing(NamespacedKey.class)
    public Set<NamespacedKey> biomes = new HashSet<>();
    @Save
    public Matcher biomeMatcher = Matcher.ANY;
    @Save
    @Typing(Integer.class)
    public Set<Integer> dimensions = new HashSet<>();
    @Save
    public Matcher dimensionMatcher = Matcher.ANY;
    @Save
    public NamespacedKey shutOffBlock = Keys.of(Material.PACKED_ICE);
    @Save
    public BlockFace shutOffBlockFace = BlockFace.DOWN;
    @Typing(WeightedKey.class)
    public WeightedList<WeightedKey<BlockData>> replacements = new WeightedList<>();


    public Formation(){}
    public Formation(Material replaced){
        blockKey = replaced.getKey();
    }
    public Formation(NamespacedKey replaced){
        blockKey = replaced;
    }
    public Formation setMinY(int y){
        this.minY=y;
        return this;
    }
    public Formation setMaxY(int y){
        this.maxY=y;
        return this;
    }
    public Formation addBiomes(Biome... biomes){
        for (Biome biome : biomes) {
            this.biomes.add(biome.getKey());
        }
        return this;
    }
    public Formation addDimensions(int... ids){
        for (int id : ids) {
            dimensions.add(id);
        }
        return this;
    }
    public Formation setDimensionMatcher(Matcher matcher){
        this.dimensionMatcher=matcher;
        return this;
    }
    public Formation setBiomeMatcher(Matcher matcher){
        this.biomeMatcher=matcher;
        return this;
    }

    public Formation setShutOffBlock(Material shutOff){
        shutOffBlock = Keys.of(shutOff);
        return this;
    }
    public Formation setShutOffBlockFace(BlockFace face){
        shutOffBlockFace=face;
        return this;
    }

    public Formation withReplacements(Object... weightedReplacements){
        int weight = 1;
        NamespacedKey key = null;
        for (int i = 0; i < weightedReplacements.length; i++) {
            if(i%2 == 0){
                key = Converter.namespacedKey(weightedReplacements[i]);
            }
            if(i%2 == 1){
                weight = (int) weightedReplacements[i];
                if(key != null) {
                    replacements.add(new WeightedKey<>(key, weight));
                }
            }
        }
        return this;
    }

    public void set(BlockState b, Random rand){
        BlockData data = null;
        while(data == null && !replacements.isEmpty()) {
            WeightedKey<BlockData> key = replacements.getRandom(rand);
            if(key.cached == null){
                //Save the blockdata so that we don't doom ourselves to server lag.
                try {
                    key.cached = Bukkit.createBlockData(key.toString());
                } catch (Exception e) {
                    CursedSkyblock.instance.getLogger().warning("Removing block: " + key.getKey() + " from formation options due to: " + e.getMessage());
                    replacements.remove(key);
                    e.printStackTrace();
                }
            }
            data = key.cached;
        }
        if(data != null) {
            b.setBlockData(data);
        }
    }

    public boolean matches(Block b){
        return biomeMatches(b) && dimensionMatches(b) && inRange(b) && !shutOff(b);
    }

    public boolean shutOff(Block b) {
        return Keys.of(b.getRelative(shutOffBlockFace).getType()) == shutOffBlock;
    }

    public boolean inRange(Block b) {
        return b.getY() >= minY && b.getY() <= maxY;
    }

    public boolean biomeMatches(Block b) {
        return biomeMatcher.matches(biomes,Keys.of(b.getBiome()));
    }

    public boolean dimensionMatches(Block b) {
        return dimensionMatcher.matches(dimensions,b.getWorld().getEnvironment().getId());

    }

    public boolean matches(BlockState block) {
        return matches(block.getBlock());
    }
}
