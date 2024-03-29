package cif.rochester.cursedskyblock.lib;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.Objects;

public class WorldPosI {
    private final World world;
    private final int x;
    private final int y;
    private final int z;

    public WorldPosI(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public WorldPosI(Location location){
        this(location.getWorld(), location.getBlockX(), location.blockY(), location.blockZ());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldPosI worldPosI = (WorldPosI) o;
        return x == worldPosI.x && y == worldPosI.y && z == worldPosI.z && Objects.equals(world, worldPosI.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getZ(){
        return z;
    }

    public World getWorld() {
        return world;
    }

    public Block getBlock() {
        return world.getBlockAt(x,y,z);
    }

    public WorldPosI translate(Vector vec) {
        return new WorldPosI(world,x+vec.getBlockX(),y+vec.getBlockY(),z+vec.getBlockZ());
    }

}