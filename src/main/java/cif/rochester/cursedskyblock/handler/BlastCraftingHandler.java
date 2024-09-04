package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.CursedSkyblock;
import cif.rochester.cursedskyblock.config.BlastCraftingConfig;
import cif.rochester.cursedskyblock.config.MechanicsConfig;
import cif.rochester.cursedskyblock.data.persistent.basic.BlastCraftingRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;

public class BlastCraftingHandler implements Handler {
    @EventHandler
    public void onExplosion(ExplosionPrimeEvent event){
        Location l = event.getEntity().getLocation();
        Item[] entities = l.getNearbyEntitiesByType(Item.class,2, 2, 2).toArray(new Item[0]);
        Object[] recipeStack = BlastCraftingConfig.getRecipe(entities);
        BlastCraftingRecipe recipe = (BlastCraftingRecipe) recipeStack[0];
        int n = (int) recipeStack[1];
        if(recipe != null) {
            for (Item item : entities) {
                item.setItemStack(ItemStack.empty());
                item.setHealth(0);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(CursedSkyblock.instance, () -> {
                ItemStack output = recipe.output.asBukkitStack();
                int j = n*output.getAmount();
                while(j > 0){
                    ItemStack drop = output.clone();
                    int added = Math.min(drop.getMaxStackSize(),j);
                    j-=added;
                    drop.setAmount(added);
                    l.getWorld().dropItem(l,drop);
                }
            }, 1);
        }
    }

    @Override
    public boolean isEnabled() {
        return MechanicsConfig.instance.enableBlastCrafting;
    }
}
