package cif.rochester.cursedskyblock;

import cif.rochester.cursedskyblock.commands.WikiCommand;
import cif.rochester.cursedskyblock.config.FormationsConfig;
import cif.rochester.cursedskyblock.data.Keys;
import cif.rochester.cursedskyblock.generation.VoidGenerator;
import cif.rochester.cursedskyblock.handler.*;
import cif.rochester.cursedskyblock.lib.NMSHelper;
import com.vicious.viciouslib.LoggerWrapper;
import com.vicious.viciouslib.persistence.IPersistent;
import com.vicious.viciouslib.persistence.vson.SerializationHandler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandMap;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class CursedSkyblock extends JavaPlugin {
    public static Plugin instance;
    public static final List<IPersistent> configs = new ArrayList<>();

    @Override
    public void onEnable() {
        //Init NMS for better piston control.
        LoggerWrapper.javaLogger = getLogger();
        SerializationHandler.registerHandler(NamespacedKey.class, Keys::of,k-> "\"" + k.toString() + "\"");
        NMSHelper.init(Thread.currentThread().getContextClassLoader());
        instance=this;
        configs.add(FormationsConfig.instance);


        Bukkit.getPluginManager().registerEvents(new AnvilCrushingHandler(),this);
        Bukkit.getPluginManager().registerEvents(new FormationHandler(),this);
        Bukkit.getPluginManager().registerEvents(new MossGrowthHandler(),this);
        Bukkit.getPluginManager().registerEvents(new EndCrystalExplosionHandler(),this);
        Bukkit.getPluginManager().registerEvents(new PistonEventHandler(),this);
        Bukkit.getPluginManager().registerEvents(new RandomTickHandler(),this);
        Bukkit.getPluginManager().registerEvents(new GrassBonemealingHandler(),this);
        Bukkit.getPluginManager().registerEvents(new TwerkSimHandler(),this);
        Bukkit.getPluginManager().registerEvents(new StoneBonemealingHandler(),this);
        CommandMap map = Bukkit.getCommandMap();
        map.register("cswiki","cursedskyblock",new WikiCommand());
    }

    private final VoidGenerator generator = new VoidGenerator();

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        getLogger().info(worldName + " is using cursed skyblock voidgen.");
        return generator;
    }

    @Override
    public void reloadConfig() {
        for (IPersistent config : configs) {
            config.load();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
