package cif.rochester.cursedskyblock.data.persistent;

import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;

import java.util.function.Supplier;

public interface ICached<T>{
    T getCached();
    void setCached(T cached);
    default T getOrCompute(Supplier<T> supplier){
        T t = getCached();
        if(t == null){
            t = supplier.get();
            setCached(t);
        }
        return t;
    }
    default boolean isStable(Supplier<T> supplier){
        return getOrCompute(supplier) != null;
    }

    default BlockData asBlockData(){
        try {
            return Bukkit.createBlockData(toString());
        } catch (Exception e) {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    default boolean cacheBlockData(){
        return isStable(()-> (T) asBlockData());
    }
}
