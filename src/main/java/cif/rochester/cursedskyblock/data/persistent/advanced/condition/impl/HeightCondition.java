package cif.rochester.cursedskyblock.data.persistent.advanced.condition.impl;

import cif.rochester.cursedskyblock.data.persistent.advanced.condition.types.LocationCondition;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import org.bukkit.Location;

//TODO: Reconsider if x and z validators are worth it or just bloat.
public class HeightCondition implements LocationCondition {
    @Save
    public long maxY=Integer.MAX_VALUE;
    @Save
    public long minY=Integer.MIN_VALUE;

    @Override
    public boolean isValid(Location location) {
        return location.getY() <= maxY && location.getY() >= minY;
    }
}
