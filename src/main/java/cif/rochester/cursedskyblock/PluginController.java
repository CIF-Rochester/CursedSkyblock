package cif.rochester.cursedskyblock;

import cif.rochester.cursedskyblock.handler.*;
import com.vicious.viciouslib.persistence.IPersistent;
import com.vicious.viciouslib.util.ClassAnalyzer;
import com.vicious.viciouslib.util.ClassMap;
import com.vicious.viciouslib.util.reflect.ClassManifest;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.List;

public class PluginController {
    private static final ClassMap<Handler> eventHandlers = new ClassMap<>();
    static {
        //Regulated by configs
        eventHandlers.put(FormationHandler.class,new FormationHandler());
        eventHandlers.put(MossGrowthHandler.class,new MossGrowthHandler());
        //These aren't regulated yet.
        Bukkit.getPluginManager().registerEvents(new AnvilCrushingHandler(),CursedSkyblock.instance);
        Bukkit.getPluginManager().registerEvents(new EndCrystalExplosionHandler(),CursedSkyblock.instance);
        Bukkit.getPluginManager().registerEvents(new PistonEventHandler(),CursedSkyblock.instance);
        Bukkit.getPluginManager().registerEvents(new RandomTickHandler(),CursedSkyblock.instance);
        Bukkit.getPluginManager().registerEvents(new GrassBonemealingHandler(),CursedSkyblock.instance);
        Bukkit.getPluginManager().registerEvents(new TwerkSimHandler(),CursedSkyblock.instance);
        Bukkit.getPluginManager().registerEvents(new StoneBonemealingHandler(),CursedSkyblock.instance);
    }
    public static void reload() {
        for (Handler value : eventHandlers.values()) {
            if(value.isEnabled()){
                Bukkit.getPluginManager().registerEvents(value,CursedSkyblock.instance);
            }
            else{
                HandlerList.unregisterAll(value);
            }
        }
    }
}
