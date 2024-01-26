package cif.rochester.cursedskyblock.generation;

import org.bukkit.Material;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.CaveVinesPlant;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OverworldIslandGenerator {
    private static final Random rand = new Random();
    public static void generate(Vector spawn, ChunkGenerator.ChunkData data){
        int surfaceLevel = 3;
        int surface = spawn.getBlockY()+surfaceLevel;
        Vector center = new Vector(8,surface,8);
        generateTerrain(data,center);
        generateStructures(data,surface);
        glowVines(data,new Vector(8,surface-3,8));
    }

    private static void fillAir(ChunkGenerator.ChunkData data){
        for (int y = data.getMinHeight(); y < data.getMaxHeight(); y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    data.setBlock(x,y,z,Material.AIR);
                }
            }
        }
    }

    private static void generateTerrain(ChunkGenerator.ChunkData data, Vector center){
        rect(data,center,2,4,2,Material.STONE);
        rect(data,center,4,2,2,Material.STONE);
        rect(data,center,3,3,3,Material.STONE);
        rect(data,center,1,1,4,Material.STONE);
    }

    private static void generateStructures(ChunkGenerator.ChunkData data, int surface){
        set(data, new Vector(8-3,surface,8-2),Material.LAVA);
        set(data, new Vector(8-3,surface,8-1),Material.COBBLESTONE);
        set(data, new Vector(8-3,surface-1,8-1),Material.PACKED_ICE);
        set(data, new Vector(8-3,surface,8),Material.WATER);
        Levelled levelled = (Levelled) data.getBlockData(8-3,surface,8);
        levelled.setLevel(2);
        data.setBlock(8-3,surface,8, levelled);
        levelled.setLevel(9);
        data.setBlock(8-3,surface-1,8, levelled);
        set(data, new Vector(8-3,surface,8+1),Material.WATER);
        Material sapling = randomSapling();
        switch (sapling){
            case OAK_SAPLING -> {
                set(data, new Vector(8,surface+1,8-4),Material.OAK_SIGN);
                break;
            }
            case BIRCH_SAPLING -> {
                set(data, new Vector(8,surface+1,8-4),Material.BIRCH_SIGN);
                break;
            }
            case SPRUCE_SAPLING -> {
                set(data, new Vector(8,surface+1,8-4),Material.SPRUCE_SIGN);
                break;
            }
            case JUNGLE_SIGN -> {
                set(data, new Vector(8,surface+1,8-4),Material.JUNGLE_SIGN);
                break;
            }
            case ACACIA_SAPLING -> {
                set(data, new Vector(8,surface+1,8-4),Material.ACACIA_SIGN);
                break;
            }
            case CHERRY_SAPLING -> {
                set(data, new Vector(8,surface+1,8-4),Material.CHERRY_SIGN);
                break;
            }
            default ->{}
        }
        set(data, new Vector(8,surface+1,8),sapling);
        data.setBlock(8,surface,8,Material.DIRT);
        set(data, new Vector(8,surface-3,8), Material.BEDROCK);
    }

    private static void glowVines(ChunkGenerator.ChunkData data, Vector spawn){
        int minimumVines = 5;
        int maximumVines = 15;
        int vines = rand.nextInt(minimumVines,maximumVines+1);
        int rangeStart = 4;
        int rangeEnd = 12;
        int y = spawn.getBlockY()-1;
        int surface = spawn.getBlockY()+3;
        Set<Vector> locations = new HashSet<>();
        l1: while(locations.size() < vines){
            int x = rand.nextInt(rangeStart,rangeEnd+1);
            int z = rand.nextInt(rangeStart,rangeEnd+1);
            Vector ceiling = new Vector(x,y,z);
            while(data.getType(ceiling.getBlockX(),ceiling.getBlockY(), ceiling.getBlockZ()) != Material.STONE){
                ceiling = ceiling.add(new Vector(0,1,0));
                if(ceiling.getBlockY() > surface){
                    continue l1;
                }
            }
            locations.add(ceiling.add(new Vector(0,-1,0)));
        }
        int maxLength = 5;
        for (Vector location : locations) {
            int length = rand.nextInt(1,maxLength+1);
            for (int i = 0; i < length; i++) {
                Vector l = location.clone().add(new Vector(0,-i,0));
                //if(i != length-1) {
                    set(data, l, Material.CAVE_VINES);
                    if (Math.random() > 0.5) {
                        CaveVinesPlant plant = (CaveVinesPlant) data.getBlockData(l.getBlockX(), l.getBlockY(), l.getBlockZ());
                        plant.setBerries(true);
                        data.setBlock(l.getBlockX(), l.getBlockY(), l.getBlockZ(), plant);
                    }
                //}
            }
        }
    }

    private static Material randomSapling() {
        Material[] sapling = new Material[]{Material.OAK_SAPLING,Material.BIRCH_SAPLING,Material.ACACIA_SAPLING,Material.JUNGLE_SAPLING,Material.SPRUCE_SAPLING,Material.CHERRY_SAPLING};
        return sapling[(int) (Math.random()* sapling.length)];
    }

    private static void set(ChunkGenerator.ChunkData data, Vector position, Material material){
        data.setBlock(position.getBlockX(), position.getBlockY(), position.getBlockZ(),material);
    }

    private static void rect(ChunkGenerator.ChunkData data, Vector center, int xL, int zL, int yL, Material material){
        for (int x = -xL; x <= xL; x++) {
            for (int z = -zL; z <= zL; z++) {
                for (int y = 0; y < yL; y++) {
                    set(data,center.clone().add(new Vector(x,-y,z)),material);
                }
            }
        }
    }
}
