package cif.rochester.cursedskyblock.data.persistent;

import cif.rochester.cursedskyblock.data.Matcher;
import cif.rochester.cursedskyblock.data.keys.Keys;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class LocationSpecific<T extends LocationSpecific<T>> {
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
    public boolean prioritizeSurfaceBiome = true;
    @Save
    @Typing(Integer.class)
    public Set<Integer> dimensions = new HashSet<>();
    @Save
    public Matcher dimensionMatcher = Matcher.ANY;

    public T setMinY(int y){
        this.minY=y;
        return (T) this;
    }
    public T setMaxY(int y){
        this.maxY=y;
        return (T) this;
    }
    public T addBiomes(Biome... biomes){
        for (Biome biome : biomes) {
            this.biomes.add(biome.getKey());
        }
        return (T) this;
    }
    public T addDimensions(int... ids){
        for (int id : ids) {
            dimensions.add(id);
        }
        return (T) this;
    }
    public T setDimensionMatcher(Matcher matcher){
        this.dimensionMatcher=matcher;
        return (T) this;
    }
    public T setBiomeMatcher(Matcher matcher){
        this.biomeMatcher=matcher;
        return (T) this;
    }

    public boolean matches(Block b){
        return biomeMatches(b) && dimensionMatches(b) && inRange(b);
    }

    public boolean inRange(Block b) {
        return b.getY() >= minY && b.getY() <= maxY;
    }

    public boolean biomeMatches(Block b) {
        if(!prioritizeSurfaceBiome) {
            return biomeMatcher.matches(biomes, Keys.of(b.getBiome()));
        }
        else{
            return biomeMatcher.matches(biomes, Keys.of(new Location(b.getWorld(),b.getX(),255,b.getZ()).getBlock().getBiome()));
        }
    }

    public boolean dimensionMatches(Block b) {
        return dimensionMatcher.matches(dimensions,b.getWorld().getEnvironment().getId());

    }
}
