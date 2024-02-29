package cif.rochester.cursedskyblock.handler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.util.EnumMap;
import java.util.Map;

public class LightningStrikeHandler implements Listener {
    private static final Map<Material,Material> map = new EnumMap<>(Material.class);
    static {
        map.put(Material.HAY_BLOCK,Material.PUMPKIN);
        map.put(Material.PUMPKIN,Material.MELON);
    }
    @EventHandler
    public void onStrike(LightningStrikeEvent event){
        Location l = event.getLightning().getLocation();
        l = l.add(-2,0,-2);
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                for (int y = 0; y < 2; y++) {
                    Location b = l.clone().add(x,y,z);
                    Material m = map.get(b.getBlock().getType());
                    if(m != null){
                        b.getBlock().setType(m);
                    }
                }
            }
        }
    }
}
