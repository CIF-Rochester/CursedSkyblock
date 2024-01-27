package cif.rochester.cursedskyblock.data;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public class Converter {
    public static NamespacedKey namespacedKey(Object object){
        if(object instanceof String){
            return NamespacedKey.fromString((String) object);
        }
        if(object instanceof Keyed){
            return ((Keyed) object).getKey();
        }
        if(object instanceof NamespacedKey){
            return (NamespacedKey) object;
        }
        return null;
    }
}
