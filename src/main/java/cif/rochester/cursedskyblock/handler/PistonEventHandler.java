package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.Util;
import cif.rochester.cursedskyblock.lib.piston.PistonController;
import cif.rochester.cursedskyblock.lib.piston.PistonWatcher;
import cif.rochester.cursedskyblock.lib.WorldPosI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.type.Piston;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import java.util.HashMap;
import java.util.Map;

public class PistonEventHandler implements Listener {
    private static final Map<WorldPosI, PistonController> controllers = new HashMap<>();
    @EventHandler
    public void onRedstoneChange(BlockPhysicsEvent event){
        Block b = event.getBlock();
        if (b.getType() == Material.PISTON) {
            Piston piston = (Piston) b.getBlockData();
            if (!piston.isExtended()) {
                Block anvil = b.getRelative(piston.getFacing());
                Material mat = anvil.getType();
                if (mat == Material.ANVIL || mat == Material.CHIPPED_ANVIL || mat == Material.DAMAGED_ANVIL) {
                    Block destination = anvil.getRelative(piston.getFacing());
                    if(!isPowered(b)){
                        PistonController controller = get(b.getLocation());
                        controller.allowRetract();
                    }
                    else if(Util.inBounds(destination) && Util.isAir(destination)) {
                        PistonController controller = get(b.getLocation());
                        controller.haltRetract();
                        PistonWatcher.listenRetract(controller.getPosition(),controller);
                        anvil.setType(Material.AIR);
                        destination.setType(Material.ANVIL);
                        controller.attemptPush();
                    }
                }
            }
        }
    }

    private static PistonController get(Location location){
        WorldPosI pos = new WorldPosI(location);
        if(controllers.containsKey(pos)){
            return controllers.get(pos);
        }
        else {
            PistonController controller = new PistonController(pos);
            controllers.put(pos,controller);
            return controller;
        }
    }

    //BlockFace[] tests = new BlockFace[]{BlockFace.NORTH,BlockFace.SOUTH,BlockFace.EAST,BlockFace.WEST};
    private boolean isPowered(Block block){
        Block other = block.getRelative(BlockFace.DOWN);
        if(other.getType() == Material.REDSTONE_LAMP){
            Lightable lightable = (Lightable) other.getBlockData();
            return lightable.isLit();
        }
        return false;
        /*for (BlockFace test : tests) {
            Block other = block.getRelative(test);
            BlockData other = block.getRelative()
            if(other.getBlockData() instanceof )
        }*/
    }
}
