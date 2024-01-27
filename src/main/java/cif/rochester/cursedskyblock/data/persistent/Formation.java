package cif.rochester.cursedskyblock.data.persistent;

import cif.rochester.cursedskyblock.WeightedList;
import cif.rochester.cursedskyblock.data.Converter;
import cif.rochester.cursedskyblock.data.Matcher;
import cif.rochester.cursedskyblock.data.keys.CachedKey;
import cif.rochester.cursedskyblock.data.keys.Keys;
import cif.rochester.cursedskyblock.data.keys.WeightedKey;
import cif.rochester.cursedskyblock.lib.ILogging;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import java.util.Random;

public class Formation extends LocationSpecific<Formation> implements IValidatable, ILogging {
    @Save
    public CachedKey<BlockData> replaced = Keys.cached(Material.COBBLESTONE);
    @Save
    public CachedKey<BlockData> shutOffBlock = Keys.cached(Material.PACKED_ICE);
    @Save
    public BlockFace shutOffBlockFace = BlockFace.DOWN;
    @Save
    @Typing(WeightedKey.class)
    public WeightedList<WeightedKey<BlockData>> replacements = new WeightedList<>();


    public Formation(){}
    public Formation(Object replaced){
        this.replaced = Keys.cached(replaced);
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
        shutOffBlock = Keys.cached(shutOff);
        return this;
    }
    public Formation setShutOffBlockFace(BlockFace face){
        shutOffBlockFace=face;
        return this;
    }

    public Formation withReplacements(Object... weightedReplacements){
        replacements = Converter.weightedKeys(weightedReplacements);
        return this;
    }

    public void set(BlockState b, Random rand){
        BlockData data = replacements.getRandom(rand).getCached();
        if(data != null) {
            b.setBlockData(data);
        }
    }

    public boolean matches(Block b){
        return super.matches(b) && !shutOff(b);
    }

    public boolean shutOff(Block b) {
        return Keys.of(b.getRelative(shutOffBlockFace).getType()) == shutOffBlock.getKey();
    }

    public boolean matches(BlockState block) {
        return matches(block.getBlock());
    }

    @Override
    public boolean validate() {
        for (int i = 0; i < replacements.size(); i++) {
            WeightedKey<BlockData> key = replacements.get(i);
            if(!key.cacheBlockData()){
                error("Replacement Block: " + key + " is now invalid (likely modded or misspelled and no longer exists). It will be removed!");
                replacements.remove(i);
                i--;
            }
        }
        return replaced.cacheBlockData();
    }
}
