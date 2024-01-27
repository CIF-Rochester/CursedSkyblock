package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.ChanceDrop;
import cif.rochester.cursedskyblock.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.Furnace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Function;

/**
 * Anvil Crushing.
 *
 * Used to obtain difficult to get or unobtainable blocks by crushing blocks with an anvil.
 */
public class AnvilCrushingHandler implements Listener {
    private static final Map<Material,Material> leavesToSapling = new EnumMap<>(Material.class);
    static {
        leavesToSapling.put(Material.OAK_LEAVES,Material.OAK_SAPLING);
        leavesToSapling.put(Material.DARK_OAK_LEAVES,Material.DARK_OAK_SAPLING);
        leavesToSapling.put(Material.BIRCH_LEAVES,Material.BIRCH_SAPLING);
        leavesToSapling.put(Material.ACACIA_LEAVES,Material.ACACIA_SAPLING);
        leavesToSapling.put(Material.SPRUCE_LEAVES,Material.SPRUCE_SAPLING);
        leavesToSapling.put(Material.JUNGLE_LEAVES,Material.JUNGLE_SAPLING);
        leavesToSapling.put(Material.MANGROVE_LEAVES,Material.MANGROVE_PROPAGULE);
        leavesToSapling.put(Material.CHERRY_LEAVES,Material.CHERRY_SAPLING);
    }

    private static final Random rand = new Random();
    private static final Map<Material, List<CrushResult>> crushResults = new EnumMap<>(Material.class);
    static{
        //Cobblestone to lava (Rare)
        juiceResult(Material.COBBLESTONE,cauldron->{
            Block furnace = cauldron.getLocation().add(0,-1,0).getBlock();
            if(furnace.getType() == Material.BLAST_FURNACE){
                Furnace f = (Furnace) furnace.getBlockData();
                if(f.getLightEmission() > 0){
                    if(rand.nextDouble(1.0) < 0.01){
                        cauldron.setType(Material.LAVA_CAULDRON);
                    }
                    return true;
                }
            }
            return false;
        });
        //Sand to Cactus and sugar cane
        ChanceDrop.ChanceList<Material> sandDrops = ChanceDrop.ChanceList.of(ChanceDrop.of(Material.CACTUS,0.05),ChanceDrop.of(Material.SUGAR_CANE,0.05));
        juiceResult(Material.SAND,cauldron->{
            World w = cauldron.getWorld();
            Location dropSpot = cauldron.getLocation();
            sandDrops.roll(rand,material->{
                w.dropItem(dropSpot.add(0.5, 0.5, 0.5), new ItemStack(material));
            });
            return true;
        });
        //Dirt to various plants
        ChanceDrop.ChanceList<Material> dirtDrops = ChanceDrop.ChanceList.of(ChanceDrop.of(Material.WHEAT_SEEDS,0.03),ChanceDrop.of(Material.FERN,0.01),ChanceDrop.of(Material.GRASS,0.01),ChanceDrop.of(Material.DEAD_BUSH,0.01),ChanceDrop.of(Material.BEETROOT_SEEDS,0.02),ChanceDrop.of(Material.SWEET_BERRIES,0.01));
        juiceResult(Material.DIRT,cauldron->{
            World w = cauldron.getWorld();
            Location dropSpot = cauldron.getLocation();
            dirtDrops.roll(rand,material->{
                w.dropItem(dropSpot.add(0.5, 0.5, 0.5), new ItemStack(material));
            });
            return true;
        });
        //Mossy cobblestone to moss carpets.
        juiceResult(Material.MOSSY_COBBLESTONE,cauldron->{
            World w = cauldron.getWorld();
            Location dropSpot = cauldron.getLocation();
            if(rand.nextDouble(1) < 0.85D) {
                w.dropItem(dropSpot.add(0.5, 0.5, 0.5), new ItemStack(Material.MOSS_CARPET));
            }
            return true;
        });
        //Leaves to Water (also has a sapling and apple drop chance)
        Function<Block,Boolean> waterCauldron = cauldron->{
            if(rand.nextDouble(1) < 0.2) {
                if (cauldron.getType() == Material.CAULDRON) {
                    cauldron.setType(Material.WATER_CAULDRON);
                    Levelled levelled = (Levelled) cauldron.getBlockData();
                    levelled.setLevel(levelled.getMinimumLevel());
                    cauldron.setBlockData(levelled);
                } else {
                    Levelled levelled = (Levelled) cauldron.getBlockData();
                    levelled.setLevel(Math.min(levelled.getLevel() + 1, levelled.getMaximumLevel()));
                    cauldron.setBlockData(levelled);
                }
            }
            Material leafType = cauldron.getRelative(BlockFace.UP,2).getType();
            if(rand.nextDouble(1.0) < 0.04){
                cauldron.getWorld().dropItem(cauldron.getLocation().add(0.5, 0.5, 0.5), new ItemStack(leavesToSapling.get(leafType)));
            }
            if((leafType == Material.OAK_LEAVES || leafType == Material.DARK_OAK_LEAVES) && rand.nextDouble(1.0) < 0.02){
                cauldron.getWorld().dropItem(cauldron.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.APPLE));
            }
            if((leafType == Material.JUNGLE_LEAVES) && rand.nextDouble(1.0) < 0.02){
                cauldron.getWorld().dropItem(cauldron.getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.COCOA_BEANS));
            }
            return true;
        };
        for (Material value : Material.values()) {
            if(value.name().contains("LEAVES")){
                juiceResult(value,waterCauldron);
            }
        }
        //Snow Block to Powdered Snow
        juiceResult(Material.SNOW_BLOCK,cauldron->{
            if(cauldron.getType() == Material.CAULDRON){
                cauldron.setType(Material.POWDER_SNOW_CAULDRON);
                Levelled levelled = (Levelled) cauldron.getBlockData();
                levelled.setLevel(levelled.getMaximumLevel());
                cauldron.setBlockData(levelled);
            }
            else{
                Levelled levelled = (Levelled) cauldron.getBlockData();
                levelled.setLevel(levelled.getMaximumLevel());
                cauldron.setBlockData(levelled);
            }
            return true;
        });
        //Snow to Powdered Snow
        juiceResult(Material.SNOW,cauldron->{
            if(cauldron.getType() == Material.CAULDRON){
                cauldron.setType(Material.POWDER_SNOW_CAULDRON);
                Levelled levelled = (Levelled) cauldron.getBlockData();
                levelled.setLevel(1);
                cauldron.setBlockData(levelled);
            }
            else{
                Levelled levelled = (Levelled) cauldron.getBlockData();
                levelled.setLevel(Math.min(levelled.getLevel()+1,levelled.getMaximumLevel()));
                cauldron.setBlockData(levelled);
            }
            return true;
        });
        //Magma_Block to lava
        juiceResult(Material.MAGMA_BLOCK,cauldron->{
            cauldron.setType(Material.LAVA_CAULDRON);
            return true;
        });
        //Moss to lush cave blocks
        ChanceDrop.ChanceList<Material> mossDrops = ChanceDrop.ChanceList.of(ChanceDrop.of(Material.GLOW_BERRIES,0.03),ChanceDrop.of(Material.AZALEA,0.03),ChanceDrop.of(Material.FLOWERING_AZALEA,0.03),ChanceDrop.of(Material.SMALL_DRIPLEAF,0.03),ChanceDrop.of(Material.BIG_DRIPLEAF,0.02),ChanceDrop.of(Material.SPORE_BLOSSOM,0.02));
        juiceResult(Material.MOSS_BLOCK,cauldron->{
            World w = cauldron.getWorld();
            Location dropSpot = cauldron.getLocation();
            mossDrops.roll(rand,material->{
                w.dropItem(dropSpot.add(0.5, 0.5, 0.5), new ItemStack(material));
            });
            return true;
        });
        //Stone to Netherrack
        lavaBatheResult(Material.STONE,cauldron->{
            cauldron.setType(Material.CAULDRON);
            World w = cauldron.getWorld();
            Location dropSpot = cauldron.getLocation();
            w.dropItem(dropSpot.add(0.5, 0.5, 0.5), new ItemStack(Material.NETHERRACK));
            return true;
        });
        //Water to Obsidian
        lavaBatheResult(Material.WATER,cauldron->{
            cauldron.setType(Material.CAULDRON);
            World w = cauldron.getWorld();
            Location dropSpot = cauldron.getLocation();
            w.dropItem(dropSpot.add(0.5, 0.5, 0.5), new ItemStack(Material.OBSIDIAN));
            return true;
        });
        //Dripstone Source
        addResult(Material.WATER,Material.STONE,Material.DRIPSTONE_BLOCK);
        //Initial Dirt source
        addResult(Material.SAND,Material.GRAVEL,Material.COARSE_DIRT);
        //Stone to Sand.
        addResult(Material.STONE,Material.COBBLESTONE);
        addResult(Material.COBBLESTONE,Material.GRAVEL);
        addResult(Material.GRAVEL,Material.SAND);
        //Sand to Clay.
        addResult(Material.WATER,Material.SAND,Material.CLAY);
        //Red Sand
        addResult(Material.RED_SANDSTONE,Material.RED_SAND);
        addResult(Material.SANDSTONE,Material.SAND);
        //Source of soul sand and soil.
        addResult(Material.NETHERRACK,Material.SOUL_SOIL);
        addResult(Material.SOUL_SOIL,Material.SOUL_SAND);
        //Deepslate to cobble conversion
        addResult(Material.DEEPSLATE,Material.COBBLED_DEEPSLATE);
        addResult(Material.COBBLED_DEEPSLATE,Material.COBBLESTONE);
        //Alternate route to netherrack
        addResult(Material.BASALT,Material.BLACKSTONE);
        addResult(Material.BLACKSTONE,Material.NETHERRACK);
        //Renewable amethyst
        addResult(Material.AMETHYST_BLOCK,Material.BUDDING_AMETHYST);
        //Glowstone from shroomlights.
        addResult(Material.SHROOMLIGHT,Material.GLOWSTONE);
        //Calcite Source
        addResult(Material.BONE_BLOCK,Material.CALCITE);
        //Wow, you hurt the obsidian. Now it's sad :(
        addResult(Material.OBSIDIAN,Material.CRYING_OBSIDIAN);
        //Sponge
        addResult(Material.SLIME_BLOCK,Material.YELLOW_WOOL,Material.SPONGE);
        //Blue ice obtaining.
        addResult(Material.ICE, Material.ICE,Material.PACKED_ICE);
        addResult(Material.PACKED_ICE,Material.PACKED_ICE,Material.BLUE_ICE);
        //Sus blocks
        addResult(Material.TERRACOTTA,Material.SAND,Material.SUSPICIOUS_SAND);
        addResult(Material.TERRACOTTA,Material.GRAVEL,Material.SUSPICIOUS_GRAVEL);
        //Red Sand
        addResult(Material.TERRACOTTA,Material.RED_SAND);
        //Moss carpet and dirt to grass
        addResult(Material.MOSS_CARPET,Material.DIRT,Material.GRASS_BLOCK);
        //Moss carpets to moss
        varResult(Material.MOSS_BLOCK,Material.MOSS_CARPET,Material.MOSS_CARPET,Material.MOSS_CARPET);
        //Reinforced Deepslate
        varResult(Material.REINFORCED_DEEPSLATE,Material.COAL_BLOCK,Material.COPPER_BLOCK,Material.IRON_BLOCK,Material.GOLD_BLOCK, Material.REDSTONE_BLOCK,Material.LAPIS_BLOCK,Material.DIAMOND_BLOCK,Material.EMERALD_BLOCK,Material.NETHERITE_BLOCK,Material.DEEPSLATE);
        //Bedrock (16x RDS)
        varResult(Material.BEDROCK,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE,Material.REINFORCED_DEEPSLATE);
    }

    public static void juiceResult(Material input, Function<Block,Boolean> result){
        CrushResult r = (l)->{
            if(Util.inBounds(l.clone().add(0,-2,0))){
                if(l.clone().add(0,-1,0).getBlock().getType() == Material.IRON_BARS){
                    Block destination = l.clone().add(0,-2,0).getBlock();
                    if(destination.getType() == Material.CAULDRON || destination.getType() == Material.WATER_CAULDRON){
                        if(result.apply(destination)){
                            l.getBlock().setType(Material.AIR);
                            return true;
                        }
                    }
                }
            }
            return false;
        };
        crushResults.computeIfAbsent(input,k->new ArrayList<>()).add(r);
    }

    public static void lavaBatheResult(Material input, Function<Block,Boolean> result){
        CrushResult r = (l)->{
            if(Util.inBounds(l.clone().add(0,-1,0))){
                Block destination = l.clone().add(0,-1,0).getBlock();
                if(destination.getType() == Material.LAVA_CAULDRON){
                    if(result.apply(destination)){
                        l.getBlock().setType(Material.AIR);
                        return true;
                    }
                }
            }
            return false;
        };
        crushResults.computeIfAbsent(input,k->new ArrayList<>()).add(r);
    }

    public static void varResult(Material result, Material... blocks){
        CrushResult r = (l)->{
            Block other = l.clone().add(0,-1,0).getBlock();
            int i = 1;
            while(Util.inBounds(other)){
                if(blocks[i] != other.getType()){
                    return false;
                }
                i++;
                if(i >= blocks.length){
                    l.getBlock().setType(Material.AIR);
                    while(l.getBlockY() != other.getY()){
                        l = l.clone().add(0,-1,0);
                        l.getBlock().setType(Material.AIR);
                    }
                    other.setType(result);
                    return true;
                }
                other = other.getRelative(BlockFace.DOWN);
            }
            return false;
        };
        crushResults.computeIfAbsent(blocks[0],k->new ArrayList<>()).add(r);
    }

    public static void addResult(Material key, Material result) {
        CrushResult r = (l)->{
            l.getBlock().setType(result);
            return true;
        };
        crushResults.computeIfAbsent(key,k->new ArrayList<>()).add(r);
    }
    public static void addResult(Material key, Material key2, Material result){
        CrushResult r = (l)->{
            Block other = l.clone().add(0,-1,0).getBlock();
            if(Util.inBounds(other)) {
                if (other.getType() == key2) {
                    l.getBlock().setType(Material.AIR);
                    other.setType(result);
                    return true;
                }
            }
            return false;
        };
        crushResults.computeIfAbsent(key,k->new ArrayList<>()).add(r);
    }
    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event){
        if(event.getEntityType() == EntityType.FALLING_BLOCK){
            FallingBlock entity = (FallingBlock) event.getEntity();
            BlockData data = entity.getBlockData();
            Material material = data.getMaterial();
            if(material == Material.ANVIL || material == Material.CHIPPED_ANVIL || material == Material.DAMAGED_ANVIL){
                Location crushed = entity.getLocation().clone();
                if(!event.getBlock().isLiquid()) {
                    crushed = crushed.add(0, -1, 0);
                }
                if (Util.inBounds(crushed)) {
                    Block block = crushed.getBlock();
                    List<CrushResult> possibleResults = crushResults.get(block.getType());
                    if (possibleResults != null) {
                        for (CrushResult crushResult : possibleResults) {
                            if (crushResult.apply(crushed)) {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFallingBlockDeath(ItemSpawnEvent event){
        Item item = event.getEntity();
        ItemStack stack = item.getItemStack();
        Material type = stack.getType();
        if(stack.getAmount() == 1 && type == Material.ANVIL || type == Material.CHIPPED_ANVIL || type == Material.DAMAGED_ANVIL){
            Location crushed = item.getLocation().clone();
            if(Util.inBounds(crushed)) {
                Block block = crushed.getBlock();
                List<CrushResult> possibleResults = crushResults.get(block.getType());
                if(possibleResults != null) {
                    for (CrushResult crushResult : possibleResults) {
                        if (crushResult.apply(crushed)) {
                            crushed.getBlock().setType(type);
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
            if(Util.isAir(crushed.getBlock())) {
                crushed = item.getLocation().add(0, -1, 0).clone();
                if (Util.inBounds(crushed)) {
                    Block block = crushed.getBlock();
                    List<CrushResult> possibleResults = crushResults.get(block.getType());
                    if (possibleResults != null) {
                        for (CrushResult crushResult : possibleResults) {
                            if (crushResult.apply(crushed)) {
                                crushed.getBlock().getRelative(BlockFace.UP).setType(type);
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @FunctionalInterface
    private interface CrushResult {
        boolean apply(Location location);
    }
}
