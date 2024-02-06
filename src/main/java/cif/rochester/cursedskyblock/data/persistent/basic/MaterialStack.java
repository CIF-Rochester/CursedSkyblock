package cif.rochester.cursedskyblock.data.persistent.basic;

import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialStack {
    @Save
    public Material material = Material.AIR;
    @Save
    public int size = 0;

    public boolean canDrain(ItemStack stack){
        if(stack.getType() == material){
            return stack.getAmount() >= size;
        }
        return false;
    }

    public ItemStack drain(ItemStack stack){
        stack.subtract(size);
        return stack;
    }

    public ItemStack asBukkitStack() {
        return new ItemStack(material,size);
    }
}
