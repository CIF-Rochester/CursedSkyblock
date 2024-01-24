package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * When an end crystal explodes on a block in the center of 12 reinforced deepslate in the shape of an end portal, it will replace the deep slate with end portal frames.
 */
public class EndCrystalExplosionHandler implements Listener {
    @EventHandler
    public void onExplosion(EntityExplodeEvent event){
        if(event.getEntityType() == EntityType.ENDER_CRYSTAL){
            Location center = event.getLocation();
            center = Util.findGround(center);
            //Too lazy to think about how to write a loop, so I'll just throw if the portal is incomplete.
            try {
                formFrame(center.clone().add(-2, 0, 0), BlockFace.SOUTH);
                formFrame(center.clone().add(-2, 0, 1), BlockFace.SOUTH);
                formFrame(center.clone().add(-2, 0, -1), BlockFace.SOUTH);
                formFrame(center.clone().add(2, 0, 0), BlockFace.NORTH);
                formFrame(center.clone().add(2, 0, 1), BlockFace.NORTH);
                formFrame(center.clone().add(2, 0, -1), BlockFace.NORTH);
                formFrame(center.clone().add(0, 0, -2), BlockFace.EAST);
                formFrame(center.clone().add(-1, 0, -2), BlockFace.EAST);
                formFrame(center.clone().add(1, 0, -2), BlockFace.EAST);
                formFrame(center.clone().add(0, 0, 2), BlockFace.WEST);
                formFrame(center.clone().add(-1, 0, 2), BlockFace.WEST);
                formFrame(center.clone().add(1, 0, 2), BlockFace.WEST);
            } catch (Throwable ignored){}
        }
    }

    private void formFrame(Location location, BlockFace face) {
        Block block = location.getBlock();
        if(block.getType() == Material.END_PORTAL){
            EndPortalFrame data = (EndPortalFrame) block.getBlockData();
            data.setFacing(face);
            block.setBlockData(data);
            return;
        }
        if(block.getType() == Material.REINFORCED_DEEPSLATE){
            block.setType(Material.END_PORTAL_FRAME);
            EndPortalFrame data = (EndPortalFrame) block.getBlockData();
            data.setFacing(face);
            block.setBlockData(data);
            return;
        }
        throw new RuntimeException();
    }
}
