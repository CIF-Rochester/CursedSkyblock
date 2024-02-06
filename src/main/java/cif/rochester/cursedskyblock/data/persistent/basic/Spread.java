package cif.rochester.cursedskyblock.data.persistent.basic;

import com.vicious.viciouslib.persistence.storage.aunotamations.Save;

@SuppressWarnings("unchecked")
public class Spread<T extends Spread<T>> extends LocationSpecific<T>{
    @Save
    public int range = 4;
    @Save
    public int depth = 2;
    @Save
    public float chanceToSpread = 0.9F;
    @Save
    public float chanceToSprout = 0.35F;

    public T withRange(int range){
        this.range=range;
        return (T) this;
    }

    public T withDepth(int depth){
        this.depth=depth;
        return (T) this;
    }

    public T withSpreadChance(float chance){
        this.chanceToSpread=chance;
        return (T) this;
    }

    public T withSproutChance(float chance){
        this.chanceToSprout=chance;
        return (T)this;
    }
}
