package cif.rochester.cursedskyblock.config;

import cif.rochester.cursedskyblock.data.persistent.basic.BlastCraftingRecipe;
import cif.rochester.cursedskyblock.data.persistent.basic.MaterialStack;
import com.vicious.viciouslib.persistence.IPersistent;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BlastCraftingConfig implements IPersistent {
    public static BlastCraftingConfig instance = new BlastCraftingConfig();

    @Save
    @Typing(BlastCraftingRecipe.class)
    public List<BlastCraftingRecipe> recipes = new ArrayList<>();

    public Map<Material, List<BlastCraftingRecipe>> recipeMap = new EnumMap<>(Material.class);

    public BlastCraftingConfig(){
        load();
        save();
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
