package cif.rochester.cursedskyblock.data.persistent.basic;

import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Use VLK's advanced recipe handling system.
public class BlastCraftingRecipe {
    @Save
    @Typing(MaterialStack.class)
    public List<MaterialStack> stacks = new ArrayList<>();
    @Save
    public MaterialStack output = new MaterialStack(Material.AIR,0);

    public int calculateNumberOfRecipes(ItemStack... stacks){
        Map<Material, Integer> stackMap = new HashMap<>();
        for (ItemStack stack : stacks) {
            if(stackMap.putIfAbsent(stack.getType(), stack.getAmount()) != null){
                stackMap.replace(stack.getType(),stackMap.get(stack.getType())+stack.getAmount());
            }
        }
        int mult = Integer.MAX_VALUE;
        for (MaterialStack stack : this.stacks) {
            int n = stackMap.getOrDefault(stack.material,0);
            if(n == 0){
                return 0;
            }
            mult = Math.min(n/stack.size,mult);
        }
        return mult;
    }

    @Override
    public String toString() {
        return "Yields: " + output;
    }
}
