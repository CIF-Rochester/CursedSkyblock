package cif.rochester.cursedskyblock.data.persistent.basic;

import cif.rochester.cursedskyblock.data.persistent.advanced.condition.BlockValidator;
import cif.rochester.cursedskyblock.data.persistent.advanced.condition.types.Condition;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;

import java.util.HashSet;
import java.util.Set;

public class RandomTickBlockEvent {
    @Save
    public double time=2000;
    @Save
    public boolean useChance=true;
    @Save
    public double percentChunksToApply;
    @Save
    public BlockSelector blockSelector = BlockSelector.FIRST_SOLID;
    @Save
    @Typing(BlockValidator.class)
    public Set<Condition<?>> conditions = new HashSet<>();
}
