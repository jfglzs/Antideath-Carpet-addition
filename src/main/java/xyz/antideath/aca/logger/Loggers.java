package xyz.antideath.aca.logger;


import carpet.logging.LoggerRegistry;
import xyz.antideath.aca.logger.cpu.CpuLogger;
import xyz.antideath.aca.logger.memory.MemoryLogger;

public class Loggers {
    public static boolean __cpu = false;
    public static boolean __mem = false;

    public static void registerLogger() {
        LoggerRegistry.registerLogger("cpu", CpuLogger.INSTANCE);
        LoggerRegistry.registerLogger("memAllocate", MemoryLogger.INSTANCE);;
    }
}
