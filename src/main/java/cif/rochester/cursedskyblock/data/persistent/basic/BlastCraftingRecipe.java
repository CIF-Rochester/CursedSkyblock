package cif.rochester.cursedskyblock.data.persistent.basic;

import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

//TODO: Use VLK's advanced recipe handling system.
public class BlastCraftingRecipe {
    @Save
    @Typing(MaterialStack.class)
    public List<MaterialStack> stacks = new ArrayList<>();
    @Save
    public MaterialStack output = new MaterialStack();

    public boolean matches(ItemStack... stacks){
        l1: for (MaterialStack materialStack : this.stacks) {
            for (ItemStack stack : stacks) {
                if (materialStack.canDrain(stack)) {
                    continue l1;
                }
            }
            return false;
        }
        return true;
    }

    public boolean matches(Item... entities){
        ItemStack[] stacks = new ItemStack[entities.length];
        for (int i = 0; i < entities.length; i++) {
            stacks[i]=entities[i].getItemStack();
        }
        return matches(stacks);
    }
}
