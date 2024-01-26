package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.BlockWeights;
import cif.rochester.cursedskyblock.Util;
import cif.rochester.cursedskyblock.WeightedList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.SculkVein;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Allows obtaining various plants by bone mealing moss in special conditions. Can be used to obtain nether plants, mycelium, sculk blocks, and podzol. Depends on Biome.
 */
public class MossGrowthHandler implements Listener {
    private static final Random rand = new Random();
    private static final Map<Biome,MossOption> options = new HashMap<>();
    static{
        options.put(Biome.WARPED_FOREST,MossOption.WARPED);
        options.put(Biome.CRIMSON_FOREST,MossOption.CRIMSON);
        options.put(Biome.MUSHROOM_FIELDS,MossOption.MYCELIUM);
        options.put(Biome.DEEP_DARK,MossOption.DARK);
        options.put(Biome.OLD_GROWTH_SPRUCE_TAIGA,MossOption.PODZOL);
    }
    @EventHandler
    public void onMossGrowth(PlayerInteractEvent event){
        Block b = event.getClickedBlock();
        if(b != null && b.getType() == Material.MOSS_BLOCK){
            MossOption option = options.get(b.getComputedBiome());
            if(option != null){
                ItemStack stack = event.getItem();
                if(stack != null && stack.getType() == Material.BONE_MEAL){
                    stack.setAmount(stack.getAmount()-1);
                    event.getPlayer().getInventory().setItem(event.getHand(),stack);
                    spread(b.getLocation(),option);
                    event.setCancelled(true);
                }
            }
        }
    }

    private void spread(Location location, MossOption option){
        Location air = location.clone().add(0,1,0);
        if(air.getBlock().isSolid() || air.getBlock().isLiquid() || !Util.inBounds(air)){
           return;
        }
        Set<Location> visited = new HashSet<>();
        option.sprout(location);
        spreadRec(air,option,4,visited);
    }

    private void spreadRec(Location location, MossOption option, int range, Set<Location> visited){
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
        grow(location,option);
        range--;
        spreadRec(location.clone().add(1,0,0),option,range,visited);
        spreadRec(location.clone().add(-1,0,0),option,range,visited);
        spreadRec(location.clone().add(0,0,1),option,range,visited);
        spreadRec(location.clone().add(0,0,-1),option,range,visited);
    }

    private void grow(Location location, MossOption option) {
        while(!location.getBlock().isSolid() && Util.inBounds(location)){
            location = location.clone().add(0,-1,0);
        }
        option.apply(location);
    }

    private enum MossOption{
        CRIMSON(Material.NETHERRACK,Material.CRIMSON_NYLIUM, BlockWeights.of("CRIMSON")),
        WARPED(Material.NETHERRACK,Material.WARPED_NYLIUM,BlockWeights.of("WARPED")),
        MYCELIUM(Material.GRASS_BLOCK,Material.MYCELIUM,BlockWeights.of("MYCELIUM")),
        PODZOL(Material.GRASS_BLOCK,Material.PODZOL,BlockWeights.of("PODZOL")),
        DARK(Material.DEEPSLATE,Material.SCULK,BlockWeights.of("DARK")){
            @Override
            public void apply(Location location) {
                if(location.getBlock().getType() == getReplacement() || location.getBlock().getType() == getReplaced()){
                    location.getBlock().setType(getReplacement());
                    if(rand.nextDouble(1.0) < 0.5){
                        location = location.clone().add(0,1,0);
                        if(Util.inBounds(location) && !location.getBlock().isSolid() && !location.getBlock().isLiquid()){
                            Material type = getOptions().getRandom(rand).getMaterial();
                            location.getBlock().setType(type);
                            if(type == Material.SCULK_VEIN){
                                SculkVein vein = (SculkVein) location.getBlock().getBlockData();
                                vein.setFace(BlockFace.DOWN,true);
                                vein.setFace(BlockFace.UP,false);
                                vein.setFace(BlockFace.WEST,false);
                                vein.setFace(BlockFace.EAST,false);
                                vein.setFace(BlockFace.SOUTH,false);
                                vein.setFace(BlockFace.NORTH,false);
                                location.getBlock().setBlockData(vein);
                            }
                        }
                    }
                }
            }

            @Override
            public void sprout(Location location){
                location.getBlock().setType(getReplacement());
                if(rand.nextDouble(1.0) < 0.5){
                    location = location.clone().add(0,1,0);
                    if(Util.inBounds(location) && !location.getBlock().isSolid() && !location.getBlock().isLiquid()){
                        Material type = getOptions().getRandom(rand).getMaterial();
                        location.getBlock().setType(type);
                        if(type == Material.SCULK_VEIN){
                            SculkVein vein = (SculkVein) location.getBlock().getBlockData();
                            vein.setFace(BlockFace.DOWN,true);
                            vein.setFace(BlockFace.UP,false);
                            vein.setFace(BlockFace.WEST,false);
                            vein.setFace(BlockFace.EAST,false);
                            vein.setFace(BlockFace.SOUTH,false);
                            vein.setFace(BlockFace.NORTH,false);
                            location.getBlock().setBlockData(vein);
                        }
                    }
                }
            }
        };

        MossOption(Material replaced, Material replacement, WeightedList<BlockWeights> plants) {
            this.replaced = replaced;
            this.replacement = replacement;
            this.weights = plants;
        }

        private final Material replaced;

        public Material getReplacement() {
            return replacement;
        }

        private final Material replacement;
        private final WeightedList<BlockWeights> weights;

        public WeightedList<BlockWeights> getOptions() {
            return weights;
        }

        public Material getReplaced() {
            return replaced;
        }

        public void apply(Location location){
            if(location.getBlock().getType() == replaced || location.getBlock().getType() == getReplaced()){
                location.getBlock().setType(replacement);
                Util.fertilize(location);
                if(rand.nextDouble(1.0) < 0.5){
                    location = location.clone().add(0,1,0);
                    if(Util.inBounds(location) && !location.getBlock().isSolid() && !location.getBlock().isLiquid()){
                        location.getBlock().setType(weights.getRandom(rand).getMaterial());
                        Util.fertilize(location);
                    }
                }
            }
        }

        public void sprout(Location location){
            location.getBlock().setType(replacement);
            if(rand.nextDouble(1.0) < 0.5){
                location = location.clone().add(0,1,0);
                if(Util.inBounds(location) && !location.getBlock().isSolid() && !location.getBlock().isLiquid()){
                    location.getBlock().setType(weights.getRandom(rand).getMaterial());
                }
            }
        }
    }
}
