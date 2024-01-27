package cif.rochester.cursedskyblock.data;

import cif.rochester.cursedskyblock.IWeighted;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import org.bukkit.NamespacedKey;

import java.util.Objects;

public class WeightedKey<T> implements IWeighted {
    @Save
    public NamespacedKey key = Keys.air;
    @Save
    public int weight = 1;

    public T cached = null;

    public WeightedKey(){}
    public WeightedKey(NamespacedKey key, int weight){
        this.key = key;
        this.weight = weight;
    }
    @Override
    public int getWeight() {
        return weight;
    }
    public NamespacedKey getKey(){
        return key;
    }


    public static WeightedKey of(NamespacedKey key, int weight){
        return new WeightedKey(key,weight);
    }

    @Override
    public String toString() {
        return key.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightedKey that = (WeightedKey) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
