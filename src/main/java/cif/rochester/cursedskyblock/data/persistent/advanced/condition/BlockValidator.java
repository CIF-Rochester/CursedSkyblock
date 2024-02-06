package cif.rochester.cursedskyblock.data.persistent.advanced.condition;

import cif.rochester.cursedskyblock.data.persistent.IValidatable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public interface BlockValidator extends IValidatable {
    default boolean validBlock(Block block){
        return validBlock(block.getState());
    }
    boolean validBlock(BlockState block);
}
