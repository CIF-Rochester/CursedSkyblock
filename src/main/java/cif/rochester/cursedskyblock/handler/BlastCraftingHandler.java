package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.CursedSkyblock;
import cif.rochester.cursedskyblock.config.BlastCraftingConfig;
import cif.rochester.cursedskyblock.config.MechanicsConfig;
import cif.rochester.cursedskyblock.data.persistent.basic.BlastCraftingRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

public class BlastCraftingHandler implements Handler {
    @EventHandler
    public void onExplosion(EntityExplodeEvent event){
        Location l = event.getLocation();
        Item[] items = l.getNearbyEntitiesByType(Item.class, 2, 2, 2).toArray(new Item[0]);
        BlastCraftingRecipe recipe = BlastCraftingConfig.getRecipe(items);
        if(recipe != null) {
            for (Item item : items) {
                item.setItemStack(ItemStack.empty());
                item.setHealth(0);
            }
            Bukkit.getScheduler().scheduleSyncDelayedTask(CursedSkyblock.instance, () -> {
                l.getWorld().dropItem(l, recipe.output.asBukkitStack());
            }, 1);
        }
    }

    @Override
    public boolean isEnabled() {
        return MechanicsConfig.instance.enableBlastCrafting;
    }
}
