package io.jfglzs.ad_carpet_addition.logger;


import carpet.logging.LoggerRegistry;
import io.jfglzs.ad_carpet_addition.logger.cpu.CpuLogger;
import io.jfglzs.ad_carpet_addition.logger.memory.MemoryLogger;

public class Loggers {
    public static boolean __cpu = false;
    public static boolean __mem = false;

    public static void registerLogger() {
        LoggerRegistry.registerLogger("cpu", CpuLogger.INSTANCE);
        LoggerRegistry.registerLogger("memAllocate", MemoryLogger.INSTANCE);;
    }
}
