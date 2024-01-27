package cif.rochester.cursedskyblock;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.block.data.type.CaveVinesPlant;

public class Util {
    private static BlockFace[] normals = new BlockFace[]{BlockFace.NORTH,BlockFace.SOUTH,BlockFace.EAST,BlockFace.WEST,BlockFace.UP,BlockFace.DOWN};

    public static Location findGround(Location location){
        while(isAir(location) && inBounds(location)){
            location = location.clone().add(0,-1,0);
        }
        return location;
    }

    public static Location findGround(Location location, int depth) {
        int i = 0;
        while(isAir(location) && inBounds(location) && i < depth){
            location = location.clone().add(0,-1,0);
            i++;
        }
        return location;
    }

    public static boolean inBounds(Location location) {
        return location.getY() >= location.getWorld().getMinHeight() && location.getY() <= location.getWorld().getMaxHeight();
    }

    public static boolean inBounds(Block block) {
        return block.getY() > block.getWorld().getMinHeight() && block.getY() <= block.getWorld().getMaxHeight();
    }

    public static boolean isAir(Location location) {
        return isAir(location.getBlock());
    }

    public static boolean isAir(Block block) {
        return block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR;
    }

    public static int getHeight(World world){
        return world.getMaxHeight()-world.getMinHeight();
    }

    public static Block pickRandomBlock(Chunk target) {
        int x = (int) (16*Math.random());
        int z = (int) (16*Math.random());
        int y = (int) (getHeight(target.getWorld())*Math.random()) + target.getWorld().getMinHeight();
        return target.getBlock(x,y,z);
    }

    public static Block pickRandomSolidBlockExposedToSky(Chunk target) {
        int x = (int) (16*Math.random());
        int z = (int) (16*Math.random());
        World world = target.getWorld();
        Location location = new Location(world,target.getX()*16+x,world.getMaxHeight(),target.getZ()*16+z);
        while(!isSolid(location) && inBounds(location)){
            location = location.clone().add(0,-1,0);
        }
        return location.getBlock();
    }

    public static boolean isSolid(Location location) {
        return location.getBlock().isSolid();
    }

    public static void fertilize(Location location){
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location.getX() + 0.5, location.getY() + 0.5, location.getZ() + 0.5, 5, 0.6, 0.1, 0.6);
    }

    public static void fertilizeCaveVine(Block b){
        CaveVinesPlant plant = (CaveVinesPlant) b.getBlockData();
        plant.setBerries(true);
        b.setBlockData(plant);
        fertilize(b.getLocation());
    }

    public static void setBlockDataAndFaces(Location location,BlockData data, BlockFace... blockFaces) {
        if(data instanceof MultipleFacing){
            for (BlockFace normal : normals) {
                ((MultipleFacing) data).setFace(normal,false);
            }
            for (BlockFace blockFace : blockFaces) {
                ((MultipleFacing) data).setFace(blockFace,true);
            }
        }
        location.getBlock().setBlockData(data);

    }
}
