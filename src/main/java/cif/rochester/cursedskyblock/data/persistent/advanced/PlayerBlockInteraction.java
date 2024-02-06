package cif.rochester.cursedskyblock.data.persistent.advanced;

import cif.rochester.cursedskyblock.data.Matcher;
import cif.rochester.cursedskyblock.data.persistent.advanced.blockaction.BlockAction;
import cif.rochester.cursedskyblock.data.persistent.advanced.condition.BlockValidator;
import com.vicious.viciouslib.persistence.storage.aunotamations.Save;
import com.vicious.viciouslib.persistence.storage.aunotamations.Typing;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerBlockInteraction {
    @Save
    @Typing(EquipmentSlot.class)
    public Set<EquipmentSlot> acceptableSlots = new HashSet<>();
    @Save
    public Matcher itemMatcher = Matcher.ONLY;
    @Save
    public Set<NamespacedKey> acceptableItems = new HashSet<>();
    @Save
    @Typing(BlockValidator.class)
    public List<BlockValidator> blockValidators = new ArrayList<>();
    @Save
    @Typing(BlockAction.class)
    public List<BlockAction> actions = new ArrayList<>();
}
