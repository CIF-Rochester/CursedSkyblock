package cif.rochester.cursedskyblock;

import cif.rochester.cursedskyblock.handler.*;
import cif.rochester.cursedskyblock.lib.NMSHelper;
import cif.rochester.cursedskyblock.generation.VoidGenerator;
import com.vicious.cursedskyblock.handler.*;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CursedSkyblock extends JavaPlugin {
    public static Plugin instance;

    @Override
    public void onEnable() {
        //Init NMS for better piston control.
        NMSHelper.init(Thread.currentThread().getContextClassLoader());
        instance=this;
        Bukkit.getPluginManager().registerEvents(new AnvilCrushingHandler(),this);
        Bukkit.getPluginManager().registerEvents(new FormationHandler(),this);
        Bukkit.getPluginManager().registerEvents(new MossGrowthHandler(),this);
        Bukkit.getPluginManager().registerEvents(new EndCrystalExplosionHandler(),this);
        Bukkit.getPluginManager().registerEvents(new PistonEventHandler(),this);
        Bukkit.getPluginManager().registerEvents(new RandomTickHandler(),this);
        Bukkit.getPluginManager().registerEvents(new GrassBonemealingHandler(),this);
    }

    private final VoidGenerator generator = new VoidGenerator();

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        getLogger().info(worldName + " is using cursed skyblock voidgen.");
        return generator;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
