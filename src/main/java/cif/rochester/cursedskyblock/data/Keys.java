package cif.rochester.cursedskyblock.data;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.HashMap;
import java.util.Map;

public class Keys {
    private static final Map<Object, NamespacedKey> keys = new HashMap<>();
    public static final NamespacedKey any = of("cursedskyblock:any");
    public static NamespacedKey air = of(Material.AIR);

    public static NamespacedKey of(String key){
        return keys.computeIfAbsent(key,k->NamespacedKey.fromString(key));
    }
    public static NamespacedKey of(Keyed keyed){
        if(keys.containsKey(keyed)){
            return keys.get(keyed);
        }
        else{
            keys.put(keyed,keyed.getKey());
            return keys.get(keyed);
        }
    }

    public static NamespacedKey of(Block block) {
        return of(block.getType());
    }

    public static NamespacedKey of(BlockState block) {
        return of(block.getType());
    }
}
