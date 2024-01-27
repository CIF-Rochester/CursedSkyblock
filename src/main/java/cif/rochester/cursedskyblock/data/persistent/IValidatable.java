package cif.rochester.cursedskyblock.data.persistent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public interface IValidatable {
    default boolean validate(){return true;}

    static <V extends IValidatable> void revalidate(Collection<V> collection){
        List<V> bad = new ArrayList<>();
        for (V v : collection) {
            if(!v.validate()){
                bad.add(v);
            }
        }
        collection.removeAll(bad);
    }

    static <V extends IValidatable> void revalidate(Collection<V> collection, Consumer<V> badConsumer){
        List<V> bad = new ArrayList<>();
        for (V v : collection) {
            if(!v.validate()){
                bad.add(v);
                badConsumer.accept(v);
            }
        }
        collection.removeAll(bad);
    }
}
