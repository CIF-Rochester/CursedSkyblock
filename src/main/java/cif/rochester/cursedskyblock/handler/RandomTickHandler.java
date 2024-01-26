package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.Util;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

/**
 * Randomly changes cobblestone to mossy cobblestone when exposed to water.
 */
public class RandomTickHandler implements Listener {
    private final BlockFace[] mossFaces = new BlockFace[]{BlockFace.EAST,BlockFace.WEST,BlockFace.SOUTH,BlockFace.NORTH};
    @EventHandler
    public void onRandomTick(ServerTickStartEvent event){
        //Handle random mossy cobblestone generation.
        if(Math.random() < 0.0005) {
            l1: for (World world : Bukkit.getWorlds()) {
                Chunk[] chunks = world.getLoadedChunks();
                if(chunks.length > 0) {
                    int maxChunks = Math.max(1,chunks.length/10);
                    for (int i = 0; i < maxChunks; i++) {
                        Chunk target = chunks[(int) (Math.random() * chunks.length)];
                        Block b = Util.pickRandomSolidBlockExposedToSky(target);
                        if (b.getType() == Material.COBBLESTONE) {
                            for (BlockFace mossFace : mossFaces) {
                                if (b.getRelative(mossFace).getType() == Material.WATER) {
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
    @EventHandler
    public void postGen(ChunkPopulateEvent event){
        Chunk chunk = event.getChunk();
        if(chunk.getWorld().getEnvironment() == World.Environment.NORMAL){
            if(chunk.getX() == 0 && chunk.getZ() == 0){
                for (BlockState tileEntity : chunk.getTileEntities()) {
                    if(tileEntity instanceof Sign sign){
                        sign.setWaxed(true);
                        sign.setGlowingText(true);
                        sign.setColor(DyeColor.LIGHT_BLUE);
                        sign.setLine(0,"Welcome! To get");
                        sign.setLine(1,"started, sneak!");
                        sign.setLine(2, "For more info,");
                        sign.setLine(3,"/cs-wiki");
                        tileEntity.update(true,true);
                    }
                }
            }
        }
    }
}
