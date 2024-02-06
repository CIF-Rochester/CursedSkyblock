package cif.rochester.cursedskyblock.config;

import cif.rochester.cursedskyblock.data.Matcher;
import cif.rochester.cursedskyblock.data.NamespacedMap;
import cif.rochester.cursedskyblock.data.keys.Keys;
import cif.rochester.cursedskyblock.data.persistent.IValidatable;
import cif.rochester.cursedskyblock.data.persistent.basic.PlantGrassSpread;
import cif.rochester.cursedskyblock.lib.ILogging;
import com.vicious.viciouslib.persistence.IPersistent;
import com.vicious.viciouslib.persistence.storage.aunotamations.PersistentPath;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class GrassmealingConfig implements IPersistent, ILogging {
    public static final GrassmealingConfig instance = new GrassmealingConfig();
    @PersistentPath
    public String path = "plugins/config/cursedskyblock/grassmealing.txt";
    @Typing(PlantGrassSpread.class)
    @Save
    public Set<PlantGrassSpread> grassSpreads = new HashSet<>();

    //Key is associated with BIOME
    private final NamespacedMap<PlantGrassSpread> grassMap = new NamespacedMap<>();
    public GrassmealingConfig(){
        //Jungles
        grassSpreads.add(new PlantGrassSpread()
                .addBiomes(Biome.JUNGLE,Biome.BAMBOO_JUNGLE,Biome.SPARSE_JUNGLE)
                .setBiomeMatcher(Matcher.ONLY)
                .surfaceBlocks(Material.GRASS_BLOCK)
                .withSprouts(Material.JUNGLE_SAPLING,80,Material.BAMBOO_SAPLING, 50, Material.OAK_SAPLING, 30));
        //Cherry
        grassSpreads.add(new PlantGrassSpread()
                .addBiomes(Biome.CHERRY_GROVE)
                .setBiomeMatcher(Matcher.ONLY)
                .surfaceBlocks(Material.GRASS_BLOCK)
                .withSprouts(Material.CHERRY_SAPLING,1));
        //Generic biomes that spawn mostly oak
        grassSpreads.add(new PlantGrassSpread()
                .addBiomes(Biome.SWAMP,Biome.PLAINS,Biome.GROVE,Biome.RIVER,Biome.WOODED_BADLANDS,Biome.MEADOW,Biome.SNOWY_PLAINS,Biome.SNOWY_BEACH,Biome.BEACH)
                .withSproutChance(0.05F)
                .setBiomeMatcher(Matcher.ONLY)
                .surfaceBlocks(Material.GRASS_BLOCK)
                .withSprouts(Material.OAK_SAPLING,1));
        //Birch forests
        grassSpreads.add(new PlantGrassSpread()
                .addBiomes(Biome.BIRCH_FOREST,Biome.OLD_GROWTH_BIRCH_FOREST)
                .setBiomeMatcher(Matcher.ONLY)
                .surfaceBlocks(Material.GRASS_BLOCK)
                .withSprouts(Material.BIRCH_SAPLING,1));
        //Savannas
        grassSpreads.add(new PlantGrassSpread()
                .addBiomes(Biome.SAVANNA,Biome.SAVANNA_PLATEAU,Biome.WINDSWEPT_SAVANNA)
                .setBiomeMatcher(Matcher.ONLY)
                .surfaceBlocks(Material.GRASS_BLOCK)
                .withSprouts(Material.ACACIA_SAPLING,80, Material.OAK_SAPLING, 20));
        //Taigas
        grassSpreads.add(new PlantGrassSpread()
                .addBiomes(Biome.TAIGA,Biome.OLD_GROWTH_PINE_TAIGA,Biome.OLD_GROWTH_SPRUCE_TAIGA,Biome.SNOWY_TAIGA)
                .setBiomeMatcher(Matcher.ONLY)
                .surfaceBlocks(Material.GRASS_BLOCK)
                .withSprouts(Material.SPRUCE_SAPLING,1));
        //Dark Forest
        grassSpreads.add(new PlantGrassSpread()
                .addBiomes(Biome.DARK_FOREST)
                .setBiomeMatcher(Matcher.ONLY)
                .surfaceBlocks(Material.GRASS_BLOCK)
                .withSprouts(Material.DARK_OAK_SAPLING,1));
        //Birch and Oak Forests
        grassSpreads.add(new PlantGrassSpread()
                .addBiomes(Biome.FOREST,Biome.FLOWER_FOREST,Biome.WINDSWEPT_FOREST)
                .setBiomeMatcher(Matcher.ONLY)
                .surfaceBlocks(Material.GRASS_BLOCK)
                .withSprouts(Material.OAK_SAPLING,50,Material.BIRCH_SAPLING));
        //Mangroves
        //Dark Forest
        grassSpreads.add(new PlantGrassSpread()
                .addBiomes(Biome.MANGROVE_SWAMP)
                .setBiomeMatcher(Matcher.ONLY)
                .surfaceBlocks(Material.GRASS_BLOCK)
                .withSprouts(Material.MANGROVE_PROPAGULE,1));
        load();
        save();
    }

    @Override
    public void load() {
        IPersistent.super.load();
        IValidatable.revalidate(grassSpreads, bad->{
            warn("Removing invalid grass spread instance. Has no valid surface blocks.");
        });
        grassMap.clear();
        for (PlantGrassSpread spread : grassSpreads) {
            if(spread.biomeMatcher == Matcher.ANY){
                addToBiome(spread, Keys.any);
            }
            else if(spread.biomeMatcher == Matcher.EXCEPT){
                for (Biome value : Biome.values()) {
                    NamespacedKey biome = Keys.of(value);
                    if(!spread.biomes.contains(biome)){
                        addToBiome(spread,biome);
                    }
                }
            }
            else{
                for (NamespacedKey biome : spread.biomes) {
                    addToBiome(spread,biome);
                }
            }
        }
    }

    private void addToBiome(PlantGrassSpread spread, NamespacedKey key) {
        grassMap.put(key, spread);
    }
    public static PlantGrassSpread getSpread(Block block){
        PlantGrassSpread option = instance.grassMap.get(Keys.of(block.getComputedBiome()));
        if(option == null){
            option = instance.grassMap.get(Keys.any);
        }
        return option;
    }
}
