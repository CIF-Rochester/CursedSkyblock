package cif.rochester.cursedskyblock.data.persistent.basic;

import cif.rochester.cursedskyblock.Util;
import cif.rochester.cursedskyblock.data.keys.CachedKey;
import cif.rochester.cursedskyblock.data.keys.Keys;
import cif.rochester.cursedskyblock.data.persistent.IValidatable;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

import java.util.Random;

public class MossSpread extends BonemealResult<MossSpread> implements IValidatable {
    @Save
    public CachedKey<BlockData> replacement;
    @Save
    public int priority;

    public MossSpread(){
        prioritizeSurfaceBiome=false;
    }
    public MossSpread(Object replacement){
        this.replacement=Keys.cached(replacement);
        prioritizeSurfaceBiome=false;
    }

    public MossSpread priority(int priority){
        this.priority=priority;
        return this;
    }

    @Override
    public boolean validate() {
        if(!super.validate()){
            return false;
        }
        return replacement.cacheBlockData();
    }

    public void apply(Location location, Random rand){
        location.getBlock().setBlockData(replacement.getCached());
        Util.fertilize(location);
        sprout(location,rand);
    }

    @Override
    protected boolean shouldApply(CachedKey<?> key) {
        return super.shouldApply(key) || replacement.equals(key);
    }

    @Override
    public String toString() {
        return replacement.toString();
    }
}
