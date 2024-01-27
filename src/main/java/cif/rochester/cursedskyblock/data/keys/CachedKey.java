package cif.rochester.cursedskyblock.data.keys;

import cif.rochester.cursedskyblock.data.persistent.ICached;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CachedKey<T> implements Keyed, ICached<T> {
    private final NamespacedKey key;
    private T cached = null;

    public CachedKey(NamespacedKey key){
        this.key=key;
    }

    @Override
    public String toString() {
        return key.toString();
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public T getCached() {
        return cached;
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
        return this.key.equals(key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public void setCached(T cached) {
        this.cached=cached;
    }
}
