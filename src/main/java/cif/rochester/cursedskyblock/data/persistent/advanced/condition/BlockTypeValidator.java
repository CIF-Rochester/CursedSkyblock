package cif.rochester.cursedskyblock.data.persistent.advanced.condition;

import cif.rochester.cursedskyblock.CursedSkyblock;
import cif.rochester.cursedskyblock.data.keys.CachedKey;
import cif.rochester.cursedskyblock.data.keys.Keys;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockTypeValidator implements BlockValidator {
    @Save
    @Typing(CachedKey.class)
    public Set<CachedKey<BlockData>> targets = new HashSet<>();

    @Override
    public boolean validate() {
        List<CachedKey<BlockData>> bad = new ArrayList<>();
        for (CachedKey<BlockData> target : targets) {
            if(!target.cacheBlockData()){
                CursedSkyblock.instance.getLogger().warning("Block: " + target + " is now invalid (likely misspelled or modded and no longer exists). It will be removed!");
                bad.add(target);
            }
        }
        bad.forEach(targets::remove);
        return !targets.isEmpty();
    }

    @Override
    public boolean validBlock(BlockState block) {
        return targets.contains(Keys.cached(block));
    }
}
