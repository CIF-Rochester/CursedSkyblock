package cif.rochester.cursedskyblock.data.persistent.advanced.condition.types;

import cif.rochester.cursedskyblock.data.persistent.IValidatable;

public interface Condition<T> extends IValidatable {
    boolean isValid(T t);
}
