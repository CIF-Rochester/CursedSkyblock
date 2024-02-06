package cif.rochester.cursedskyblock.data.persistent.advanced.condition.impl;

import cif.rochester.cursedskyblock.data.Matcher;
import cif.rochester.cursedskyblock.data.persistent.advanced.condition.types.WorldCondition;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class DimensionCondition implements WorldCondition {
    @Save
    @Typing(Integer.class)
    public Set<Integer> ids = new HashSet<>();
    @Save
    public Matcher matcher = Matcher.ONLY;

    //Todo Find some sort of NMS implementation to make this more accurate with modded.
    @Override
    public boolean isValid(World world) {
        return matcher.matches(ids,world.getEnvironment().getId());
    }
}
