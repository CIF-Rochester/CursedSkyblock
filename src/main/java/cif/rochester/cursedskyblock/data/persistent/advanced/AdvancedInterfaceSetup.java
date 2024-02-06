package cif.rochester.cursedskyblock.data.persistent.advanced;

import cif.rochester.cursedskyblock.data.persistent.advanced.condition.impl.BlockTypeCondition;
import cif.rochester.cursedskyblock.data.persistent.advanced.condition.impl.DimensionCondition;
import cif.rochester.cursedskyblock.data.persistent.advanced.condition.impl.HeightCondition;
import com.vicious.viciouslib.persistence.KeyToClass;

public class AdvancedInterfaceSetup {
    public static void init(){
        KeyToClass.register(DimensionCondition.class,"cs.condition.dimension");
        KeyToClass.register(HeightCondition.class,"cs.condition.height");
        KeyToClass.register(BlockTypeCondition.class,"cs.condition.blocktype");
        KeyToClass.register(BlockTypeCondition.class,"cs.condition.chance");
    }
}
