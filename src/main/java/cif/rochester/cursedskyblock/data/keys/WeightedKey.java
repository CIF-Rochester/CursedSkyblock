package cif.rochester.cursedskyblock.data.keys;

import cif.rochester.cursedskyblock.IWeighted;
import cif.rochester.cursedskyblock.data.persistent.ICached;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class WeightedKey<T> implements IWeighted, Keyed, ICached<T> {
    @Save
    public NamespacedKey key = Keys.air;
    @Save
    public int weight = 1;

    private T cached = null;

    @Override
    public T getCached() {
        return cached;
    }

    @Override
    public void setCached(T cached) {
        this.cached=cached;
    }

    public T getOrCompute(Supplier<T> supplier){
        if(cached == null){
            cached=supplier.get();
        }
        return cached;
    }

    public WeightedKey(){}
    public WeightedKey(NamespacedKey key, int weight){
        this.key = key;
        this.weight = weight;
    }
    @Override
    public int getWeight() {
        return weight;
    }

    public @NotNull NamespacedKey getKey(){
        return key;
    }


    public static <T> WeightedKey<T> of(NamespacedKey key, int weight){
        return new WeightedKey<>(key,weight);
    }

    @Override
    public String toString() {
        return key.toString();
    }

    @Override
    public boolean equals(Object o) {
        NamespacedKey key = null;
        if(o instanceof Keyed){
            key = ((Keyed) o).getKey();
        }
        if(o instanceof NamespacedKey){
            key = (NamespacedKey) o;
        }
        return Objects.equals(this.key,key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
