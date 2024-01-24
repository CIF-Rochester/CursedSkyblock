package cif.rochester.cursedskyblock.lib.piston;


import cif.rochester.cursedskyblock.lib.WorldPosI;

/**
 * Forces a piston to be sticky. Useful for when server restarts occur.
 */
public class StickyPistonController extends PistonController{
    public StickyPistonController(WorldPosI blockPosition) {
        super(blockPosition);
        isSticky=true;
    }

    @Override
    public boolean isSticky() {
        return true;
    }
}