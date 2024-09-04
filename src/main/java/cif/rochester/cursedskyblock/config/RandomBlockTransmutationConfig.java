package cif.rochester.cursedskyblock.config;

import cif.rochester.cursedskyblock.data.persistent.basic.RandomTickBlockEvent;
import com.vicious.viciouslib.persistence.IPersistent;
import com.vicious.viciouslib.persistence.storage.aunotamations.PersistentPath;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;

import java.util.HashSet;
import java.util.Set;

public class RandomBlockTransmutationConfig implements IPersistent {
    public static final RandomBlockTransmutationConfig instance = new RandomBlockTransmutationConfig();
    @PersistentPath
    public String path = "plugins/config/cursedskyblock/randomtickevents.txt";
    @Typing(RandomTickBlockEvent.class)
    @Save
    public Set<RandomTickBlockEvent> tickEvents = new HashSet<>();

    private RandomBlockTransmutationConfig(){
        save();
        load();
    }
}
