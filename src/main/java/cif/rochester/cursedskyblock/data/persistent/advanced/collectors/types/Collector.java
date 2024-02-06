package cif.rochester.cursedskyblock.data.persistent.advanced.collectors.types;

import java.util.Collection;

public interface Collector<T,S> {
    Collection<T> collect(S source);
}
