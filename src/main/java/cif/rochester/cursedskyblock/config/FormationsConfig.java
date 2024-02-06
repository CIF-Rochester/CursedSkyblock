package cif.rochester.cursedskyblock.config;

import cif.rochester.cursedskyblock.data.Matcher;
import cif.rochester.cursedskyblock.data.NamespacedMap;
import cif.rochester.cursedskyblock.data.keys.Keys;
import cif.rochester.cursedskyblock.data.persistent.basic.Formation;
import cif.rochester.cursedskyblock.data.persistent.IValidatable;
import cif.rochester.cursedskyblock.lib.ILogging;
import com.vicious.viciouslib.persistence.IPersistent;
import com.vicious.viciouslib.persistence.storage.aunotamations.LoadOnly;
import com.vicious.viciouslib.persistence.storage.aunotamations.PersistentPath;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@LoadOnly
public class FormationsConfig implements IPersistent, ILogging {
    public static final FormationsConfig instance = new FormationsConfig();
    @PersistentPath
    public String path = "plugins/config/cursedskyblock/formations.txt";
    @Typing(Formation.class)
    @Save
    public Set<Formation> formations = new HashSet<>();


    //Key is associated with Material
    private final NamespacedMap<List<Formation>> formationMap = new NamespacedMap<>();

    public FormationsConfig(){
        formations.add(new Formation(Material.COBBLESTONE)
                .addBiomes(Biome.STONY_PEAKS,Biome.JAGGED_PEAKS,Biome.FROZEN_PEAKS)
                .setBiomeMatcher(Matcher.ONLY)
                .setMinY(1)
                .withReplacements(Material.EMERALD_ORE,1,Material.DIAMOND_ORE,1,Material.LAPIS_ORE,2,Material.REDSTONE_ORE,3,Material.GOLD_ORE,3,Material.IRON_ORE,5,Material.COAL_ORE,10,Material.COPPER_ORE,7,Material.COBBLESTONE,20,Material.STONE,20,Material.GRANITE,20,Material.DIORITE,20,Material.ANDESITE,20,Material.RAW_COPPER_BLOCK,1)
        );
        formations.add(new Formation(Material.COBBLESTONE)
                .addBiomes(Biome.STONY_PEAKS,Biome.JAGGED_PEAKS,Biome.FROZEN_PEAKS)
                .setBiomeMatcher(Matcher.ONLY)
                .setMaxY(0)
                .withReplacements(Material.DEEPSLATE_EMERALD_ORE,4,Material.DEEPSLATE_DIAMOND_ORE,3,Material.DEEPSLATE_LAPIS_ORE,4,Material.DEEPSLATE_REDSTONE_ORE,5,Material.DEEPSLATE_GOLD_ORE,6,Material.DEEPSLATE_IRON_ORE,8,Material.DEEPSLATE_COAL_ORE,2,Material.DEEPSLATE_COPPER_ORE,4,Material.COBBLED_DEEPSLATE,15,Material.DEEPSLATE,21,Material.TUFF,22,Material.RAW_IRON_BLOCK,2,Material.AMETHYST_BLOCK,1)
        );
        formations.add(new Formation(Material.COBBLESTONE)
                .addBiomes(Biome.STONY_PEAKS,Biome.JAGGED_PEAKS,Biome.FROZEN_PEAKS)
                .setBiomeMatcher(Matcher.EXCEPT)
                .setMinY(1)
                .withReplacements(Material.DIAMOND_ORE,1,Material.LAPIS_ORE,2,Material.REDSTONE_ORE,3,Material.GOLD_ORE,3,Material.IRON_ORE,5,Material.COAL_ORE,10,Material.COPPER_ORE,7,Material.COBBLESTONE,20,Material.STONE,20,Material.GRANITE,20,Material.DIORITE,20,Material.ANDESITE,20,Material.RAW_COPPER_BLOCK,1)
        );
        formations.add(new Formation(Material.COBBLESTONE)
                .addBiomes(Biome.STONY_PEAKS,Biome.JAGGED_PEAKS,Biome.FROZEN_PEAKS)
                .setBiomeMatcher(Matcher.EXCEPT)
                .setMaxY(0)
                .withReplacements(Material.DEEPSLATE_DIAMOND_ORE,3,Material.DEEPSLATE_LAPIS_ORE,4,Material.DEEPSLATE_REDSTONE_ORE,5,Material.DEEPSLATE_GOLD_ORE,6,Material.DEEPSLATE_IRON_ORE,8,Material.DEEPSLATE_COAL_ORE,3,Material.DEEPSLATE_COPPER_ORE,4,Material.COBBLED_DEEPSLATE,15,Material.DEEPSLATE,21,Material.TUFF,22,Material.RAW_IRON_BLOCK,2,Material.AMETHYST_BLOCK,1)
        );
        formations.add(new Formation(Material.BASALT)
                .setDimensionMatcher(Matcher.ONLY)
                .addDimensions(-1)
                .setShutOffBlockFace(BlockFace.EAST)
                .withReplacements(Material.NETHERRACK,15,Material.BASALT,20,Material.BLACKSTONE,10,Material.ANCIENT_DEBRIS,1,Material.NETHER_QUARTZ_ORE,10,Material.NETHER_GOLD_ORE,7));
        load();
        save();
    }

    @Override
    public void load() {
        IPersistent.super.load();
        formationMap.clear();
        IValidatable.revalidate(formations,bad->{
            warn("Removing invalid formation instance. Block to replace no longer valid: " + bad.replaced);
        });
        for (Formation formation : formations) {
            List<Formation> options = formationMap.computeIfAbsent(formation.replaced.getKey(), k->new ArrayList<>());
            options.add(formation);
        }
    }

    public static Formation getFormation(BlockState block){
        List<Formation> options = instance.formationMap.get(Keys.of(block));
        if(options != null){
            for (Formation formation : options) {
                if(formation.matches(block)){
                    return formation;
                }
            }
        }
        return null;
    }
}
