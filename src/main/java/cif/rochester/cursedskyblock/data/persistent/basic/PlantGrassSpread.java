package cif.rochester.cursedskyblock.data.persistent.basic;

import cif.rochester.cursedskyblock.Util;
import org.bukkit.Location;

import java.util.Random;

public class PlantGrassSpread extends BonemealResult<PlantGrassSpread>{
    public PlantGrassSpread(){
        chanceToSprout=0.1F;
    }
    @Override
    public void apply(Location location, Random rand) {
        Util.fertilize(location);
        sprout(location,rand);
    }
}
