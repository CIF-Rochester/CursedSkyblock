package cif.rochester.cursedskyblock.config;

import cif.rochester.cursedskyblock.data.persistent.basic.BlastCraftingRecipe;
import cif.rochester.cursedskyblock.data.persistent.basic.MaterialStack;
import com.vicious.viciouslib.persistence.IPersistent;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class BlastCraftingConfig implements IPersistent {
    public static BlastCraftingConfig instance = new BlastCraftingConfig();

    @Save
    @Typing(BlastCraftingRecipe.class)
    public List<BlastCraftingRecipe> recipes = new ArrayList<>();

    public Map<Material, List<BlastCraftingRecipe>> recipeMap = new EnumMap<>(Material.class);

    public BlastCraftingConfig(){
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.ANCIENT_DEBRIS,4));
            r.stacks.add(new MaterialStack(Material.NETHERRACK,16));
            r.output=new MaterialStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.DARK_OAK_LOG,4));
            r.stacks.add(new MaterialStack(Material.COBBLESTONE,16));
            r.output=new MaterialStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.DARK_OAK_PLANKS,4));
            r.stacks.add(new MaterialStack(Material.COBBLESTONE,16));
            r.output=new MaterialStack(Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.JUNGLE_SAPLING,4));
            r.stacks.add(new MaterialStack(Material.COBBLESTONE,16));
            r.output=new MaterialStack(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.DRIED_KELP_BLOCK,4));
            r.stacks.add(new MaterialStack(Material.COBBLESTONE,16));
            r.output=new MaterialStack(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.SAND,4));
            r.stacks.add(new MaterialStack(Material.COBBLESTONE,16));
            r.output=new MaterialStack(Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.DEEPSLATE,4));
            r.stacks.add(new MaterialStack(Material.COBBLESTONE,16));
            r.output=new MaterialStack(Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.SCULK_SHRIEKER,4));
            r.stacks.add(new MaterialStack(Material.COBBLESTONE,16));
            r.output=new MaterialStack(Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.BLACKSTONE,4));
            r.stacks.add(new MaterialStack(Material.NETHERRACK,16));
            r.output=new MaterialStack(Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.BONE,4));
            r.stacks.add(new MaterialStack(Material.NETHERRACK,16));
            r.output=new MaterialStack(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.ENDER_EYE,4));
            r.stacks.add(new MaterialStack(Material.COBBLESTONE,16));
            r.output=new MaterialStack(Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DIAMOND_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.CHORUS_FRUIT,4));
            r.stacks.add(new MaterialStack(Material.COBBLESTONE,16));
            r.output=new MaterialStack(Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,1);
        });
        newRecipe(r->{
            r.stacks.add(new MaterialStack(Material.DRIPSTONE_BLOCK,1));
            r.stacks.add(new MaterialStack(Material.ARROW,1));
            r.output = new MaterialStack(Material.POINTED_DRIPSTONE,1);
        });
        load();
        save();
    }

    private void newRecipe(Consumer<BlastCraftingRecipe> bcr){
        BlastCraftingRecipe recipe = new BlastCraftingRecipe();
        bcr.accept(recipe);
        recipes.add(recipe);
    }

    @Override
    public void load() {
        IPersistent.super.load();
        for (BlastCraftingRecipe recipe : recipes) {
            for (MaterialStack stack : recipe.stacks) {
                List<BlastCraftingRecipe> related = recipeMap.computeIfAbsent(stack.material,k->new ArrayList<>());
                related.add(recipe);
            }
        }
    }

    public static BlastCraftingRecipe getRecipe(Item... entities){
        ItemStack[] stacks = new ItemStack[entities.length];
        for (int i = 0; i < entities.length; i++) {
            stacks[i]=entities[i].getItemStack();
        }
        for (ItemStack stack : stacks) {
            List<BlastCraftingRecipe> lst = instance.recipeMap.get(stack.getType());
            for (BlastCraftingRecipe recipe : lst) {
                if(recipe.matches(stacks)){
                    return recipe;
                }
            }
        }
        return null;
    }
}
