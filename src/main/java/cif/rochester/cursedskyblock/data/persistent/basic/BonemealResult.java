package cif.rochester.cursedskyblock.data.persistent.basic;

import cif.rochester.cursedskyblock.CursedSkyblock;
import cif.rochester.cursedskyblock.Util;
import cif.rochester.cursedskyblock.WeightedList;
import cif.rochester.cursedskyblock.data.Converter;
import cif.rochester.cursedskyblock.data.keys.CachedKey;
import cif.rochester.cursedskyblock.data.keys.Keys;
import cif.rochester.cursedskyblock.data.keys.WeightedKey;
import cif.rochester.cursedskyblock.data.persistent.IValidatable;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class BonemealResult<T extends BonemealResult<T>> extends Spread<T> implements IValidatable {
    @Save
    @Typing(CachedKey.class)
    public List<CachedKey<BlockData>> surfaceBlocks = new ArrayList<>();
    @Save
    @Typing(WeightedKey.class)
    public WeightedList<WeightedKey<BlockData>> sproutedPlants = new WeightedList<>();

    public T surfaceBlocks(Object... surfaceBlocks){
        for (Object replacement : surfaceBlocks) {
            this.surfaceBlocks.add(Keys.cached(replacement));
        }
        return (T)this;
    }

    public T withSprouts(Object... weightedSprouts){
        sproutedPlants = Converter.weightedKeys(weightedSprouts);
        return (T)this;
    }

    @Override
    public boolean validate() {
        //Check that the keys actually exist.
        for (int i = 0; i < sproutedPlants.size(); i++) {
            WeightedKey<BlockData> key = sproutedPlants.get(i);
            if(!key.cacheBlockData()){
                CursedSkyblock.instance.getLogger().warning("Sprouted Block: " + key + " is now invalid (likely misspelled or modded and no longer exists). It will be removed!");
                sproutedPlants.remove(i);
                i--;
            }
        }
        for (int i = 0; i < surfaceBlocks.size(); i++) {
            CachedKey<BlockData> key = surfaceBlocks.get(i);
            if(!key.cacheBlockData()){
                CursedSkyblock.instance.getLogger().warning("Replaced Block: " + key + " is now invalid (likely misspelled or modded and no longer exists). It will be removed!");
                surfaceBlocks.remove(i);
                i--;
            }
        }
        return !surfaceBlocks.isEmpty();
    }


    public abstract void apply(Location location, Random rand);

    public void spread(Location location, Random rand){
        Location air = location.clone().add(0,1,0);
        if(air.getBlock().isSolid() || air.getBlock().isLiquid() || !Util.inBounds(air)){
            return;
        }
        Set<Location> visited = new HashSet<>();
        apply(location, rand);
        spreadRec(air,rand,range,visited);
    }

    private void spreadRec(Location location, Random rand, int range, Set<Location> visited){
        if(visited.contains(location)){
            return;
        }
        visited.add(location);
        if(range == 0 || rand.nextDouble(1.0) > chanceToSpread){
            return;
        }
        if(location.getBlock().isSolid() || location.getBlock().isLiquid() || !Util.inBounds(location)){
            return;
        }
        Location ground = Util.findGround(location,depth+1);
        if(shouldApply(ground)) {
            apply(ground, rand);
        }
        range--;
        spreadRec(location.clone().add(1,0,0),rand,range,visited);
        spreadRec(location.clone().add(-1,0,0),rand,range,visited);
        spreadRec(location.clone().add(0,0,1),rand,range,visited);
        spreadRec(location.clone().add(0,0,-1),rand,range,visited);
    }

    public void sprout(Location location, Random rand){
        if(rand.nextDouble(1.0) < chanceToSprout) {
            location = location.clone().add(0, 1, 0);
            if (Util.inBounds(location) && !location.getBlock().isSolid() && !location.getBlock().isLiquid()) {
                Util.fertilize(location);
                Util.setBlockDataAndFaces(location,sproutedPlants.getRandom(rand).getCached(), BlockFace.DOWN);
            }
        }
    }

    protected boolean shouldApply(Location location){
        return shouldApply(Keys.cached(location.getBlock().getType()));
    }

    protected boolean shouldApply(CachedKey<?> key){
        return surfaceBlocks.contains(key);
    }
}
