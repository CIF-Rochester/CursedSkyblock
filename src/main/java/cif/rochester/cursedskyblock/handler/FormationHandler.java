package cif.rochester.cursedskyblock.handler;

import cif.rochester.cursedskyblock.config.FormationsConfig;
import cif.rochester.cursedskyblock.config.MechanicsConfig;
import cif.rochester.cursedskyblock.data.persistent.basic.Formation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockFormEvent;

import java.util.Random;

/**
 * Changes cobblestone and basalt generator functionality. Type of block generated depends on Y position and dimension. Emerald ore will generate only in mountain type biomes.
 */
public class FormationHandler implements Handler {
    static Random rand = new Random();

    @EventHandler
    public void onGenerate(BlockFormEvent event){
        Formation formation = FormationsConfig.getFormation(event.getNewState());
        if(formation != null){
            formation.set(event.getNewState(),rand);
        }
    }

    @Override
    public boolean isEnabled() {
        return MechanicsConfig.instance.enableFormationOverrides;
    }
}
