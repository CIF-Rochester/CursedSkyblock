package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;

import java.util.*;

public class GrassBonemealingHandler implements Listener {
    private static final Random rand = new Random();
    private static final Map<Biome, List<Material>> biomeToSapling = new EnumMap<>(Biome.class);
    static {
        registerConversion(Material.OAK_SAPLING,Biome.FOREST,Biome.WOODED_BADLANDS,Biome.DARK_FOREST,Biome.FLOWER_FOREST,Biome.SWAMP,Biome.SAVANNA,Biome.JUNGLE,Biome.SPARSE_JUNGLE,Biome.BAMBOO_JUNGLE);
        registerConversion(Material.BIRCH_SAPLING,Biome.FOREST,Biome.DARK_FOREST,Biome.FLOWER_FOREST,Biome.BIRCH_FOREST,Biome.OLD_GROWTH_BIRCH_FOREST);
        registerConversion(Material.ACACIA_SAPLING,Biome.SAVANNA,Biome.SAVANNA_PLATEAU,Biome.WINDSWEPT_SAVANNA);
        registerConversion(Material.JUNGLE_SAPLING,Biome.JUNGLE,Biome.BAMBOO_JUNGLE,Biome.SPARSE_JUNGLE);
        registerConversion(Material.BAMBOO_SAPLING,Biome.JUNGLE,Biome.BAMBOO_JUNGLE,Biome.SPARSE_JUNGLE);
        registerConversion(Material.CHERRY_SAPLING,Biome.CHERRY_GROVE);
        registerConversion(Material.MANGROVE_PROPAGULE,Biome.MANGROVE_SWAMP);
        registerConversion(Material.SPRUCE_SAPLING,Biome.TAIGA,Biome.SNOWY_TAIGA,Biome.OLD_GROWTH_SPRUCE_TAIGA,Biome.OLD_GROWTH_PINE_TAIGA);
        registerConversion(Material.DARK_OAK_SAPLING,Biome.DARK_FOREST);
    }

    private static void registerConversion(Material sapling, Biome... biomes){
        for (Biome biome : biomes) {
            List<Material> results = biomeToSapling.computeIfAbsent(biome, k->new ArrayList<>());
            results.add(sapling);
        }
    }
    @EventHandler
    public void onMossGrowth(BlockFertilizeEvent event){
        if(event.getBlock().getType() == Material.GRASS_BLOCK){
            Block above = event.getBlock().getRelative(BlockFace.UP,2);
            if(Util.inBounds(above) && Util.isAir(above)){
                List<Material> options = biomeToSapling.get(above.getBiome());
                if(!options.isEmpty()) {
                    Set<Location> visited = new HashSet<>();
                    spreadRec(above.getLocation(), 3, visited, options);
                }
            }
        }
    }

    private void spreadRec(Location location, int range, Set<Location> visited, List<Material> options){
        if(visited.contains(location)){
            return;
        }
        visited.add(location);
        if(range == 0 || rand.nextDouble(1.0) < 0.1){
            return;
        }
        if(location.getBlock().isSolid() || location.getBlock().isLiquid() || !Util.inBounds(location)){
            return;
        }
        grow(location,options);
        range--;
        spreadRec(location.clone().add(1,0,0),range,visited,options);
        spreadRec(location.clone().add(-1,0,0),range,visited,options);
        spreadRec(location.clone().add(0,0,1),range,visited,options);
        spreadRec(location.clone().add(0,0,-1),range,visited,options);
    }

    private void grow(Location location, List<Material> options) {
        if(rand.nextDouble(1.0) < 0.1) {
            while (!location.getBlock().isSolid() && Util.inBounds(location)) {
                location = location.clone().add(0, -1, 0);
            }
            if (location.getBlock().getType() == Material.GRASS_BLOCK) {
                Block target = location.getBlock().getRelative(BlockFace.UP);
                if (Util.isAir(target) || target.getType() == Material.GRASS) {
                    if(options.size() > 0) {
                        target.setType(options.get(rand.nextInt(options.size())));
                    }
                }
            }
        }
    }
}
