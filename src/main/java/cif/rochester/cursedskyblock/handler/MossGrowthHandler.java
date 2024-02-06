package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.config.MechanicsConfig;
import cif.rochester.cursedskyblock.config.MossConfig;
import cif.rochester.cursedskyblock.data.persistent.basic.MossSpread;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Allows obtaining various plants by bone mealing moss in special conditions. Can be used to obtain nether plants, mycelium, sculk blocks, and podzol. Depends on Biome.
 */
public class MossGrowthHandler implements Handler {
    private static final Random rand = new Random();
    @EventHandler
    public void onMossGrowth(PlayerInteractEvent event){
        Block b = event.getClickedBlock();
        if(b != null && b.getType() == Material.MOSS_BLOCK){
            MossSpread option = MossConfig.getMossSpread(b);
            if(option != null){
                ItemStack stack = event.getItem();
                if(stack != null && stack.getType() == Material.BONE_MEAL){
                    stack.setAmount(stack.getAmount()-1);
                    event.getPlayer().getInventory().setItem(event.getHand(),stack);
                    option.spread(b.getLocation(),rand);
                    event.setCancelled(true);
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return MechanicsConfig.instance.enableMossOverrides;
    }
}
