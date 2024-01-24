package cif.rochester.cursedskyblock.handler;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import cif.rochester.cursedskyblock.Util;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Randomly changes cobblestone to mossy cobblestone when exposed to water.
 */
public class RandomTickHandler implements Listener {
    private final BlockFace[] mossFaces = new BlockFace[]{BlockFace.EAST,BlockFace.WEST,BlockFace.SOUTH,BlockFace.NORTH};
    @EventHandler
    public void onRandomTick(ServerTickStartEvent event){
        //Handle random mossy cobblestone generation.
        if(Math.random() < 0.00005) {
            l1: for (World world : Bukkit.getWorlds()) {
                Chunk[] chunks = world.getLoadedChunks();
                if(chunks.length > 0) {
                    Chunk target = chunks[(int) (Math.random() * chunks.length)];
                    Block b = Util.pickRandomSolidBlockExposedToSky(target);
                    if(b.getType() == Material.COBBLESTONE){
                        for (BlockFace mossFace : mossFaces) {
                            if(b.getRelative(mossFace).getType() == Material.WATER){
                                b.setType(Material.MOSSY_COBBLESTONE);
                                break l1;
                            }
                        }
                    }
                }
            }
        }
    }

}
