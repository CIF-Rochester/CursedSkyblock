package cif.rochester.cursedskyblock.data.persistent.advanced.condition.impl;

import cif.rochester.cursedskyblock.CursedSkyblock;
import cif.rochester.cursedskyblock.data.persistent.advanced.condition.types.Condition;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;

public class ChanceCondition implements Condition<Object> {
    @Save
    public double chance = 1D;

    @Override
    public boolean isValid(Object o) {
        return CursedSkyblock.getRandom().nextDouble(1.0D) < chance;
    }
}
