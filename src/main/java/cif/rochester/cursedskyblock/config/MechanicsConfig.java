package cif.rochester.cursedskyblock.config;

import com.vicious.viciouslib.persistence.storage.aunotamations.PersistentPath;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;

public class MechanicsConfig {
    public static final MechanicsConfig instance = new MechanicsConfig();
    @PersistentPath
    public String path = "plugins/config/cursedskyblock/mechanics.txt";

    @Save(description = "Enables cobblestone and basalt generation overrides. Customize in formations.txt")
    public boolean enableFormationOverrides = true;
    @Save(description = "Enables moss bonemealing overrides. Customize in mossspreads.txt")
    public boolean enableMossOverrides = true;
    @Save(description = "Enables growing extra plants when bonemealing grass. Customize in grassmealing.txt")
    public boolean enableGrassmealingBonuses = true;
    @Save(description = "Enables explosion crafting. Customize in blastcrafting.txt")
    public boolean enableBlastCrafting = true;
}
