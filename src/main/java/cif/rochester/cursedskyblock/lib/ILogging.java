package cif.rochester.cursedskyblock.lib;

import cif.rochester.cursedskyblock.CursedSkyblock;

public interface ILogging {
    default void warn(String message){
        CursedSkyblock.instance.getSLF4JLogger().warn(message);
    }
    default void warn(String message, Throwable throwable){
        CursedSkyblock.instance.getSLF4JLogger().warn(message,throwable);
    }
    default void error(String message){
        CursedSkyblock.instance.getSLF4JLogger().error(message);
    }
    default void error(String message, Throwable throwable){
        CursedSkyblock.instance.getSLF4JLogger().error(message,throwable);
    }
    default void info(String message){
        CursedSkyblock.instance.getSLF4JLogger().info(message);
    }
    default void info(String message, Throwable throwable){
        CursedSkyblock.instance.getSLF4JLogger().info(message,throwable);
    }
}
