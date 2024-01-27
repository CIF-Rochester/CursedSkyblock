package cif.rochester.cursedskyblock.config;

import cif.rochester.cursedskyblock.data.Matcher;
import cif.rochester.cursedskyblock.data.NamespacedMap;
import cif.rochester.cursedskyblock.data.keys.Keys;
import cif.rochester.cursedskyblock.data.persistent.IValidatable;
import cif.rochester.cursedskyblock.data.persistent.MossSpread;
import cif.rochester.cursedskyblock.lib.ILogging;
import com.vicious.viciouslib.persistence.IPersistent;
import com.vicious.viciouslib.persistence.storage.aunotamations.LoadOnly;
import com.vicious.viciouslib.persistence.storage.aunotamations.PersistentPath;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.*;

@LoadOnly
public class MossConfig implements IPersistent, ILogging {
    public static final MossConfig instance = new MossConfig();
    @PersistentPath
    public String path = "plugins/config/cursedskyblock/mosspreads.txt";
    @Typing(MossSpread.class)
    @Save
    public Set<MossSpread> mossSpreads = new HashSet<>();


    //Key is associated with BIOME
    private final NamespacedMap<List<MossSpread>> mossMap = new NamespacedMap<>();

    public MossConfig(){
        mossSpreads.add(new MossSpread(Material.CRIMSON_NYLIUM)
                .addBiomes(Biome.CRIMSON_FOREST)
                .setBiomeMatcher(Matcher.ONLY)
                .replace(Material.NETHERRACK)
                .withSprouts(Material.CRIMSON_ROOTS, 20, Material.CRIMSON_FUNGUS, 5, Material.WARPED_FUNGUS, 1));
        mossSpreads.add(new MossSpread(Material.WARPED_NYLIUM)
                .addBiomes(Biome.WARPED_FOREST)
                .setBiomeMatcher(Matcher.ONLY)
                .replace(Material.NETHERRACK)
                .withSprouts(Material.WARPED_ROOTS, 20, Material.WARPED_FUNGUS, 5, Material.CRIMSON_FUNGUS, 1));
        mossSpreads.add(new MossSpread(Material.MYCELIUM)
                .addBiomes(Biome.MUSHROOM_FIELDS)
                .setBiomeMatcher(Matcher.ONLY)
                .replace(Material.GRASS_BLOCK,Material.DIRT)
                .withSprouts(Material.BROWN_MUSHROOM,5,Material.RED_MUSHROOM,5));
        mossSpreads.add(new MossSpread(Material.PODZOL)
                .addBiomes(Biome.OLD_GROWTH_SPRUCE_TAIGA,Biome.OLD_GROWTH_PINE_TAIGA)
                .setBiomeMatcher(Matcher.ONLY)
                .replace(Material.GRASS_BLOCK,Material.DIRT)
                .withSprouts(Material.BROWN_MUSHROOM,5,Material.RED_MUSHROOM,5));
        mossSpreads.add(new MossSpread(Material.SCULK)
                .addBiomes(Biome.DEEP_DARK)
                .setBiomeMatcher(Matcher.ONLY)
                .replace(Material.DEEPSLATE,Material.STONE)
                .withSprouts(Material.SCULK_CATALYST,1,Material.SCULK_SHRIEKER,5,Material.SCULK_SENSOR,10,Material.SCULK_VEIN,30));
        load();
        save();
    }

    @Override
    public void load() {
        IPersistent.super.load();
        IValidatable.revalidate(mossSpreads,bad->{
            warn("Removing invalid moss spread instance. Block replacement no longer valid: " + bad.replacement);
        });
        mossMap.clear();
        for (MossSpread mossSpread : mossSpreads) {
            if(mossSpread.biomeMatcher == Matcher.ANY){
                addToBiome(mossSpread,Keys.any);
            }
            else if(mossSpread.biomeMatcher == Matcher.EXCEPT){
                for (Biome value : Biome.values()) {
                    NamespacedKey biome = Keys.of(value);
                    if(!mossSpread.biomes.contains(biome)){
                        addToBiome(mossSpread,biome);
                    }
                }
            }
            else{
                for (NamespacedKey biome : mossSpread.biomes) {
                    addToBiome(mossSpread,biome);
                }
            }
        }
    }

    private void addToBiome(MossSpread spread, NamespacedKey key){
        List<MossSpread> lst = mossMap.computeIfAbsent(key,k->new ArrayList<>());
        lst.add(spread);
        lst.sort(Comparator.comparingInt(m -> m.priority));
    }

    public static MossSpread getMossSpread(Block block){
        List<MossSpread> options = instance.mossMap.get(Keys.of(block.getComputedBiome()));
        if(options == null){
            options = instance.mossMap.get(Keys.any);
        }
        if(options != null){
            for (MossSpread option : options) {
                if(option.matches(block)){
                    return option;
                }
            }
        }
        return null;
    }
}
