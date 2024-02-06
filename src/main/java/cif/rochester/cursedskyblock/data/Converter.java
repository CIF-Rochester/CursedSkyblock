package cif.rochester.cursedskyblock.data;

import cif.rochester.cursedskyblock.WeightedList;
import cif.rochester.cursedskyblock.data.keys.WeightedKey;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class Converter {
    public static NamespacedKey namespacedKey(Object object){
        if(object instanceof String){
            return NamespacedKey.fromString((String) object);
        }
        if(object instanceof BlockState){
            return namespacedKey(((BlockState) object).getBlock());
        }
        if(object instanceof Block){
            return namespacedKey(((Block) object).getType());
        }
        if(object instanceof Keyed){
            return ((Keyed) object).getKey();
        }
        if(object instanceof NamespacedKey){
            return (NamespacedKey) object;
        }
        throw new RuntimeException("Cannot convert " + object.getClass() + " to a namespaced key!");
    }

    public static <T> WeightedList<WeightedKey<T>> weightedKeys(Object... objects){
        Integer weight = null;
        NamespacedKey key = null;
        WeightedList<WeightedKey<T>> list = new WeightedList<>();
        for (Object object : objects) {
            if (object instanceof Integer) {
                weight = (Integer) object;
            }
            if (isKey(object)) {
                key = namespacedKey(object);
            }
            if (weight != null && key != null) {
                list.add(new WeightedKey<>(key, weight));
                key = null;
                weight = null;
            }
        }
        return list;
    }

    private static boolean isKey(Object object) {
        return object instanceof NamespacedKey || object instanceof Keyed || object instanceof String;
    }

}
