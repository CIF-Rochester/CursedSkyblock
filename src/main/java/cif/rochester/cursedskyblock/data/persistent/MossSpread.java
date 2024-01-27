package cif.rochester.cursedskyblock.data.persistent;

import cif.rochester.cursedskyblock.CursedSkyblock;
import cif.rochester.cursedskyblock.Util;
import cif.rochester.cursedskyblock.WeightedList;
import cif.rochester.cursedskyblock.data.Converter;
import cif.rochester.cursedskyblock.data.keys.CachedKey;
import cif.rochester.cursedskyblock.data.keys.Keys;
import cif.rochester.cursedskyblock.data.keys.WeightedKey;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

import java.util.*;

public class MossSpread extends LocationSpecific<MossSpread> implements IValidatable{
    @Save
    @Typing(CachedKey.class)
    public List<CachedKey<BlockData>> blocksToReplace = new ArrayList<>();
    @Save
    public CachedKey<BlockData> replacement;
    @Save
    @Typing(WeightedKey.class)
    public WeightedList<WeightedKey<BlockData>> sproutedPlants = new WeightedList<>();
    @Save
    public int range = 4;
    @Save
    public int depth = 2;
    @Save
    public float chanceToSpread = 0.9F;
    @Save
    public float chanceToSprout = 0.35F;
    @Save
    public int priority;

    public MossSpread(){
        prioritizeSurfaceBiome=false;
    }
    public MossSpread(Object replacement){
        this.replacement=Keys.cached(replacement);
        prioritizeSurfaceBiome=false;
    }

    public MossSpread replace(Object... replacements){
        for (Object replacement : replacements) {
            blocksToReplace.add(Keys.cached(replacement));
        }
        return this;
    }

    public MossSpread withSprouts(Object... weightedSprouts){
        sproutedPlants = Converter.weightedKeys(weightedSprouts);
        return this;
    }

    public MossSpread withRange(int range){
        this.range=range;
        return this;
    }

    public MossSpread withDepth(int depth){
        this.depth=depth;
        return this;
    }

    public MossSpread withSpreadChance(float chance){
        this.chanceToSpread=chance;
        return this;
    }

    public MossSpread withSproutChance(float chance){
        this.chanceToSprout=chance;
        return this;
    }

    public MossSpread priority(int priority){
        this.priority=priority;
        return this;
    }

    @Override
    public boolean validate() {
        //Check that the keys actually exist.
        for (int i = 0; i < sproutedPlants.size(); i++) {
            WeightedKey<BlockData> key = sproutedPlants.get(i);
            if(!key.cacheBlockData()){
                CursedSkyblock.instance.getLogger().warning("Sprouted Block: " + key + " is now invalid (likely modded and no longer exists). It will be removed!");
                sproutedPlants.remove(i);
                i--;
            }
        }
        for (int i = 0; i < blocksToReplace.size(); i++) {
            CachedKey<BlockData> key = blocksToReplace.get(i);
            if(!key.cacheBlockData()){
                CursedSkyblock.instance.getLogger().warning("Replaced Block: " + key + " is now invalid (likely modded and no longer exists). It will be removed!");
                blocksToReplace.remove(i);
                i--;
            }
        }
        return replacement.cacheBlockData();
    }

    private boolean canBeReplaced(Location location){
        CachedKey<?> key = Keys.cached(location.getBlock().getType());
        return blocksToReplace.contains(key) || replacement.equals(key);
    }

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
        if(canBeReplaced(ground)) {
            apply(ground, rand);
        }
        range--;
        spreadRec(location.clone().add(1,0,0),rand,range,visited);
        spreadRec(location.clone().add(-1,0,0),rand,range,visited);
        spreadRec(location.clone().add(0,0,1),rand,range,visited);
        spreadRec(location.clone().add(0,0,-1),rand,range,visited);
    }


    public void apply(Location location, Random rand){
        location.getBlock().setBlockData(replacement.getCached());
        Util.fertilize(location);
        sprout(location,rand);
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

    @Override
    public String toString() {
        return replacement.toString();
    }
}
