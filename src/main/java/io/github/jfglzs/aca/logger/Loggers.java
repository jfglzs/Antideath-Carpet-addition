package io.github.jfglzs.aca.logger;


import carpet.logging.LoggerRegistry;
import io.github.jfglzs.aca.logger.cpu.CpuLogger;
import io.github.jfglzs.aca.logger.memory.MemoryLogger;
import io.github.jfglzs.aca.logger.network.NetworkLogger;
import io.github.jfglzs.aca.logger.physicMem.SystemMemoryLogger;
import oshi.SystemInfo;

public class Loggers {
    public static final SystemInfo SYSTEM_INFO = new SystemInfo();

    public static boolean __cpu     = false;
    public static boolean __mem     = false;
    public static boolean __sysMem  = false;
    public static boolean __network = false;

    public static void registerLogger() {
        LoggerRegistry.registerLogger("cpu", CpuLogger.INSTANCE);
        LoggerRegistry.registerLogger("memAllocate", MemoryLogger.INSTANCE);
        LoggerRegistry.registerLogger("network", NetworkLogger.INSTANCE);
        LoggerRegistry.registerLogger("sysMemory", SystemMemoryLogger.INSTANCE);
    }
}
