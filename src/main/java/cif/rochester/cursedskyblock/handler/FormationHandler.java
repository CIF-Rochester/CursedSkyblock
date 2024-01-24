package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.WeightedList;
import cif.rochester.cursedskyblock.BlockWeights;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.util.*;
import java.util.function.Supplier;

/**
 * Changes cobblestone and basalt generator functionality. Type of block generated depends on Y position and dimension. Emerald ore will generate only in mountain type biomes.
 */
public class FormationHandler implements Listener {
    static Random rand = new Random();
    private static final List<Formation> empty = new ArrayList<>();
    private static final Map<Material, List<Formation>> formations = new EnumMap<>(Material.class);
    public FormationHandler(){
        for (Formation value : Formation.values()) {
            List<Formation> options = formations.computeIfAbsent(value.material,(k)->new ArrayList<>());
            options.add(value);
        }
    }

    @EventHandler
    public void onGenerate(BlockFormEvent event){
        for (Formation formation : formations.getOrDefault(event.getNewState().getType(), empty)) {
            if(formation.shouldApply(event)){
                formation.change(event);
                return;
            }
        }
    }
    private static final Sound sound = Sound.sound().type(org.bukkit.Sound.BLOCK_LAVA_EXTINGUISH.key()).build();

    private enum Formation {
        NONE(Material.AIR){
            @Override
            boolean shouldApply(BlockFormEvent event) {
                return false;
            }
        },
        SURFACE_STONE(Material.STONE, BlockWeights.STONE){
            @Override
            boolean shouldApply(BlockFormEvent event) {
                Location l = event.getBlock().getLocation();
                return  l.getBlockY() > 0;
            }
        },
        DEEP_STONE(Material.STONE,BlockWeights.SLATE){
            @Override
            boolean shouldApply(BlockFormEvent event) {
                Location l = event.getBlock().getLocation();
                return  l.getBlockY() <= 0;
            }
        },
        SURFACE_COBBLE(Material.COBBLESTONE,"SURFACE"){
            @Override
            boolean shouldApply(BlockFormEvent event) {
                Location l = event.getBlock().getLocation();
                if(l.getBlock().getRelative(BlockFace.DOWN).getType() == Material.PACKED_ICE){
                    return false;
                }
                return  l.getBlockY() > 0 && l.getWorld().getEnvironment() == World.Environment.NORMAL;
            }

            @Override
            void change(BlockFormEvent event) {
                if(isExtremeHills(event.getBlock().getBiome())){
                    if(rand.nextDouble(100) < 1){
                        event.setCancelled(true);
                        event.getBlock().getWorld().playSound(sound);
                        event.getBlock().setType(Material.EMERALD_ORE);
                    }
                }
                else {
                    super.change(event);
                }
            }
        },
        DEEP_COBBLE(Material.COBBLESTONE,"DEEP"){
            @Override
            boolean shouldApply(BlockFormEvent event) {
                Location l = event.getBlock().getLocation();
                if(l.getBlock().getRelative(BlockFace.DOWN).getType() == Material.PACKED_ICE){
                    return false;
                }
                return  l.getBlockY() <= 0 && l.getWorld().getEnvironment() == World.Environment.NORMAL;
            }
            @Override
            void change(BlockFormEvent event) {
                if(isExtremeHills(event.getBlock().getBiome())){
                    if(rand.nextDouble(100) < 2){
                        event.setCancelled(true);
                        event.getBlock().getWorld().playSound(sound);
                        event.getBlock().setType(Material.DEEPSLATE_EMERALD_ORE);
                    }
                }
                else {
                    super.change(event);
                }
            }
        },
        NETHER_BASALT(Material.BASALT,"NETHER"){
            @Override
            boolean shouldApply(BlockFormEvent event) {
                return event.getBlock().getWorld().getEnvironment() == World.Environment.NETHER;
            }
        };

        private static WeightedList<BlockWeights> enumMatch(String name){
            WeightedList<BlockWeights> weights = new WeightedList<>();
            for (BlockWeights value : BlockWeights.values()) {
                if(value.name().startsWith(name)){
                    weights.add(value);
                }
            }
            return weights;
        }

        private static boolean isExtremeHills(Biome biome) {
            return biome == Biome.STONY_PEAKS || biome == Biome.JAGGED_PEAKS;
        }

        private final Material material;
        private final WeightedList<BlockWeights> blocks;


        Formation(Material material, BlockWeights... weights){
            this.material = material;
            blocks = new WeightedList<>();
            blocks.addAll(Arrays.asList(weights));
        }
        Formation(Material material, WeightedList<BlockWeights> weights){
            this.material = material;
            this.blocks=weights;
        }
        Formation(Material material, Supplier<WeightedList<BlockWeights>> weights){
            this.material = material;
            this.blocks=weights.get();
        }
        Formation(Material material, String name){
            this.material = material;
            this.blocks=enumMatch(name);
        }

        void change(BlockFormEvent event){
            if(shouldApply(event)) {
                Material m = blocks.getRandom(rand).getMaterial();
                if (event.getBlock().getType() != m) {
                    event.setCancelled(true);
                    event.getBlock().getWorld().playSound(sound);
                    event.getBlock().setType(m,true);
                }
            }
        }

        boolean shouldApply(BlockFormEvent event){
            return true;
        }
    }

}
