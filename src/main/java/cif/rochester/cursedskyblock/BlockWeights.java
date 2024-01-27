package cif.rochester.cursedskyblock;

import org.bukkit.Material;

public enum BlockWeights implements IWeighted {
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
