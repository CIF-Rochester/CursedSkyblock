package cif.rochester.cursedskyblock;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Util {
    public static Location findGround(Location location){
        while(isAir(location) && inBounds(location)){
            location = location.clone().add(0,-1,0);
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

    private static boolean isSolid(Location location) {
        return location.getBlock().isSolid();
    }
}
