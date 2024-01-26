package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.CursedSkyblock;
import cif.rochester.cursedskyblock.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Twerking near saplings can bone meal them.
 */
public class TwerkSimHandler implements Listener {
    private static final Map<Material,TreeType> treeTypes = new EnumMap<>(Material.class);
    static {
        treeTypes.put(Material.OAK_SAPLING,TreeType.TREE);
        treeTypes.put(Material.BIRCH_SAPLING,TreeType.BIRCH);
        treeTypes.put(Material.SPRUCE_SAPLING,TreeType.REDWOOD);
        treeTypes.put(Material.JUNGLE_SAPLING,TreeType.JUNGLE);
        treeTypes.put(Material.ACACIA_SAPLING,TreeType.ACACIA);
        treeTypes.put(Material.CHERRY_SAPLING,TreeType.CHERRY);
    }
    private final Random rand = new Random();
    private final Map<UUID, AtomicLong> playerTwerks = new HashMap<>();
    public TwerkSimHandler(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(CursedSkyblock.instance,()->{
            for (AtomicLong value : playerTwerks.values()) {
                value.getAndDecrement();
            }
        },2400L,2400L);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        if(event.isSneaking()){
            Location l = event.getPlayer().getLocation();
            if(Util.inBounds(l)){
                l = l.add(-1,0,-1);
                for (int x = 0; x < 3; x++) {
                    for (int z = 0; z < 3; z++) {
                        for (int y = 0; y < 1; y++) {
                            Block b = l.clone().add(x, -y, z).getBlock();
                            boolean success = false;
                            if(b.getBlockData() instanceof CaveVinesPlant plant){
                                if (shouldApply(event.getPlayer().getUniqueId())) {
                                    Util.fertilize(b.getLocation());
                                    if (rand.nextDouble(1.0) > 0.5) {
                                        Util.fertilizeCaveVine(b);
                                        success = true;
                                    }
                                }
                            }
                            else {
                                TreeType type = treeTypes.get(b.getType());
                                if (type != null) {
                                    if (shouldApply(event.getPlayer().getUniqueId())) {
                                        Util.fertilize(b.getLocation());
                                        if (rand.nextDouble(1.0) > 0.5) {
                                            b.setType(Material.AIR);
                                            b.getWorld().generateTree(b.getLocation(), type);
                                            success = true;
                                        }
                                    }
                                }
                            }
                            if (success) {
                                getCount(event.getPlayer().getUniqueId()).getAndIncrement();
                            }
                        }
                    }
                }
            }
        }
    }

    private AtomicLong getCount(UUID uuid){
        return playerTwerks.computeIfAbsent(uuid,u->new AtomicLong(0L));
    }

    private boolean shouldApply(UUID uuid){
        return rand.nextLong(Math.max(getCount(uuid).get(),1L)) < 1.0;
    }
}
