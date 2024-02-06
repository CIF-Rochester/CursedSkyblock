package cif.rochester.cursedskyblock.data.persistent.advanced.condition;

import cif.rochester.cursedskyblock.data.Matcher;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;

import java.util.HashSet;
import java.util.Set;

public class BiomeValidator implements BlockValidator {
    @Save
    @Typing(NamespacedKey.class)
    public Set<NamespacedKey> biomes = new HashSet<>();
    @Save
    public Matcher matcher = Matcher.ONLY;

    @Override
    public boolean validBlock(BlockState block) {
        return matcher.matches(biomes,block.getBlock().getBiome().getKey());
    }
}
