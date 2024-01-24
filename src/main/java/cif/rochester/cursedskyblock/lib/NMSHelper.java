package cif.rochester.cursedskyblock.lib;

import com.google.common.collect.Lists;
import com.vicious.viciouslib.util.reflect.deep.DeepReflection;
import com.vicious.viciouslib.util.reflect.deep.FieldSearchContext;
import com.vicious.viciouslib.util.reflect.deep.MethodSearchContext;
import com.vicious.viciouslib.util.reflect.deep.TotalFailureException;
import com.vicious.viciouslib.util.reflect.wrapper.ReflectiveField;
import com.vicious.viciouslib.util.reflect.wrapper.ReflectiveMethod;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;

public class NMSHelper {
    static {
        try {
            Enumeration<URL> jarURL = Thread.currentThread().getContextClassLoader().getResources("net/minecraft");
            DeepReflection.mapClasses(jarURL, Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //NMS
    public static Class<?> World = DeepReflection.get("World","net.minecraft");
    public static Class<?> BlockPosition = DeepReflection.get("BlockPosition","net.minecraft");
    public static Class<?> Block = DeepReflection.get("Block","net.minecraft");
    public static Class<?> BlockPiston = DeepReflection.get("BlockPiston","net.minecraft");
    public static Class<?> IBlockData = DeepReflection.get("IBlockData","net.minecraft");
    public static Class EnumDirection = DeepReflection.get("EnumDirection","net.minecraft");
    //BUKKIT
    public static Class<?> CraftBlock = DeepReflection.get("CraftBlock","org.bukkit");
    //Methods
    public static ReflectiveMethod CraftBlock$getNMS;
    public static ReflectiveMethod CraftBlock$getPosition;
    public static ReflectiveMethod IBlockData$getBlock;
    public static ReflectiveMethod BlockPiston$push;
    public static ReflectiveMethod BlockPiston$checkIfExtend;

    //Fields
    public static ReflectiveField CraftWorld$world = new ReflectiveField("world");
    public static ReflectiveField BlockPiston$isSticky;

    public static void init(ClassLoader classLoader) {
        try {
            MethodSearchContext ctx = new MethodSearchContext().accepts().returns(Block);
            IBlockData$getBlock = DeepReflection.getMethod(IBlockData,ctx);
            ctx = new MethodSearchContext().accepts(World, BlockPosition, EnumDirection, boolean.class).returns(boolean.class);
            BlockPiston$push = DeepReflection.getMethod(BlockPiston,ctx);
            CraftBlock$getNMS = DeepReflection.getMethod(CraftBlock,new MethodSearchContext().name("getNMS"));
            CraftBlock$getPosition = DeepReflection.getMethod(CraftBlock,new MethodSearchContext().name("getPosition"));
            BlockPiston$checkIfExtend = DeepReflection.getMethod(NMSHelper.BlockPiston, new MethodSearchContext().accepts(NMSHelper.World,NMSHelper.BlockPosition,NMSHelper.IBlockData));
            BlockPiston$isSticky = DeepReflection.getField(BlockPiston,new FieldSearchContext().type(boolean.class).withAccess(Lists.newArrayList(Modifier::isFinal, Modifier::isPrivate)));
        } catch (TotalFailureException e) {
            e.printStackTrace();
            //ViciousLibKit.LOGGER.severe(e.getMessage());
            //e.printStackTrace();
        }
    }

    public static void setExtended(Block piston, boolean extended) {
        Object nmsblockdata = CraftBlock$getNMS.invoke(piston);
        Object nmsblock = IBlockData$getBlock.invoke(nmsblockdata);
        Enum<?> direction = Enum.valueOf(EnumDirection,((Directional) piston.getBlockData()).getFacing().name());
        BlockPiston$push.invoke(nmsblock,CraftWorld$world.get(piston.getWorld()),CraftBlock$getPosition.invoke(piston),direction,extended);
    }
    public static void updatePiston(Block pistonBase, org.bukkit.World world) {
        Object nmsblockdata = NMSHelper.CraftBlock$getNMS.invoke(pistonBase);
        Object nmsblock = NMSHelper.IBlockData$getBlock.invoke(nmsblockdata);
        Object nmsworld = NMSHelper.CraftWorld$world.get(world);
        Object nmsblockposition = NMSHelper.CraftBlock$getPosition.invoke(pistonBase);
        BlockPiston$checkIfExtend.invoke(nmsblock,nmsworld, nmsblockposition, nmsblockdata);
    }
    public static void convertSticky(Block pistonBase){
        Object nmsblockdata = NMSHelper.CraftBlock$getNMS.invoke(pistonBase);
        BlockPiston$isSticky.set(nmsblockdata,true);
    }
}