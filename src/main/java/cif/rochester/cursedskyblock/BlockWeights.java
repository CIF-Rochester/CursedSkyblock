package cif.rochester.cursedskyblock;

import org.bukkit.Material;

public enum BlockWeights implements IWeighted {
    SURFACE_DIAMOND(1, Material.DIAMOND_ORE),
    SURFACE_LAPIS(2, Material.LAPIS_ORE),
    SURFACE_REDSTONE(3, Material.REDSTONE_ORE),
    SURFACE_GOLD(3, Material.GOLD_ORE),
    SURFACE_IRON(5, Material.IRON_ORE),
    SURFACE_COAL(10, Material.COAL_ORE),
    SURFACE_COPPER(7, Material.COPPER_ORE),
    SURFACE_COBBLESTONE(40, Material.COBBLESTONE),
    SURFACE_GRANITE(20, Material.GRANITE),
    SURFACE_DIORITE(20, Material.DIORITE),
    SURFACE_ANDESITE(20, Material.ANDESITE),
    SURFACE_RAW_COPPER(1,Material.RAW_COPPER_BLOCK),

    DEEP_DIAMOND(3, Material.DEEPSLATE_DIAMOND_ORE),
    DEEP_LAPIS(4, Material.DEEPSLATE_LAPIS_ORE),
    DEEP_REDSTONE(5, Material.REDSTONE_ORE),
    DEEP_GOLD(5, Material.DEEPSLATE_GOLD_ORE),
    DEEP_IRON(8, Material.DEEPSLATE_IRON_ORE),
    DEEP_COAL(6, Material.DEEPSLATE_COAL_ORE),
    DEEP_COPPER(5, Material.DEEPSLATE_COPPER_ORE),
    DEEP_COBBLESTONE(48, Material.COBBLED_DEEPSLATE),
    DEEP_TUFF(22, Material.TUFF),
    DEEP_IRON_RAW(1, Material.RAW_IRON_BLOCK),
    DEEP_AMETHYST(1, Material.AMETHYST_BLOCK),

    NETHER_RACK(20, Material.NETHERRACK),
    NETHER_QUARTS(10, Material.NETHER_QUARTZ_ORE),
    NETHER_DEBRIS(1, Material.ANCIENT_DEBRIS),
    NETHER_GOLD(7, Material.NETHER_GOLD_ORE),
    NETHER_STONE(12, Material.BLACKSTONE),
    NETHER_BASALT(10, Material.BASALT),

    WARPED_ROOTS(20,Material.WARPED_ROOTS),
    WARPED_FUNGUS(5,Material.WARPED_FUNGUS),
    WARPED_FUNGUSC(1,Material.CRIMSON_FUNGUS),

    CRIMSON_ROOTS(20,Material.CRIMSON_ROOTS),
    CRIMSON_FUNGUS(5,Material.CRIMSON_FUNGUS),
    CRIMSON_FUNGUSW(1,Material.WARPED_FUNGUS),

    MYCELIUM_BROWN(5,Material.BROWN_MUSHROOM),
    MYCELIUM_RED(5,Material.RED_MUSHROOM),

    PODZOL_BROWN(5,Material.BROWN_MUSHROOM),
    PODZOL_RED(5,Material.RED_MUSHROOM),

    DARK_SKULK_CATALYST(1,Material.SCULK_CATALYST),
    DARK_SKULK_SHRIEKER(5,Material.SCULK_SHRIEKER),
    DARK_SKULK_SENSOR(10,Material.SCULK_SENSOR),
    DARK_SKULK_VEIN(20,Material.SCULK_VEIN),

    STONE(1,Material.STONE),
    SLATE(1,Material.DEEPSLATE);

    private final int weight;
    private final Material material;

    BlockWeights(int weight, Material material) {
        this.weight = weight;
        this.material = material;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    public Material getMaterial() {
        return material;
    }

    public static WeightedList<BlockWeights> of(String name){
        WeightedList<BlockWeights> weights = new WeightedList<>();
        for (BlockWeights value : BlockWeights.values()) {
            if(value.name().startsWith(name)){
                weights.add(value);
            }
        }
        return weights;
    }
}
