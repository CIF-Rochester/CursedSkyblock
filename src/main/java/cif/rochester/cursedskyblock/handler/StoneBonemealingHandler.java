package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * A fast way of getting mossy cobblestone.
 */
public class StoneBonemealingHandler implements Listener {
    private final BlockFace[] mossFaces = new BlockFace[]{BlockFace.EAST,BlockFace.WEST,BlockFace.SOUTH,BlockFace.NORTH};

    @EventHandler
    public void onUse(PlayerInteractEvent event){
        if(event.getHand() == EquipmentSlot.HAND){
            ItemStack stack = event.getItem();
            if(stack != null && stack.getType() == Material.BONE_MEAL){
                Block b = event.getClickedBlock();
                if(b != null && b.getType() == Material.COBBLESTONE){
                    if(b.getType() == Material.COBBLESTONE){
                        for (BlockFace mossFace : mossFaces) {
                            if(b.getRelative(mossFace).getType() == Material.WATER){
                                b.setType(Material.MOSSY_COBBLESTONE);
                                stack.setAmount(stack.getAmount()-1);
                                event.getPlayer().getInventory().setItem(event.getHand(),stack);
                                Util.fertilize(b.getLocation());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
