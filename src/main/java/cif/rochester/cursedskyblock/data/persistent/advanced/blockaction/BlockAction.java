package cif.rochester.cursedskyblock.data.persistent.advanced.blockaction;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public interface BlockAction {
    default void apply(Block block){
        apply(block.getState());
    }
    void apply(BlockState state);
}
