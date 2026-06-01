package io.github.jfglzs.aca.logger;


import carpet.logging.LoggerRegistry;
import io.github.jfglzs.aca.logger.cpu.CpuLogger;
import io.github.jfglzs.aca.logger.disk.DiskLogger;
import io.github.jfglzs.aca.logger.memory.MemoryAllocationRateLogger;
import io.github.jfglzs.aca.logger.memory.MemoryLogger;
import io.github.jfglzs.aca.logger.network.NetworkLogger;
import oshi.SystemInfo;

public class Loggers {
    public static final SystemInfo SYSTEM_INFO = new SystemInfo();

    public static boolean __cpu     = false;
    public static boolean __mem     = false;
    public static boolean __sysMem  = false;
    public static boolean __network = false;
    public static boolean ___disk = false;

    public static void registerLoggers() {
        registerLogger(CpuLogger.INSTANCE);
        registerLogger(MemoryLogger.INSTANCE);
        registerLogger(NetworkLogger.INSTANCE);
        registerLogger(MemoryAllocationRateLogger.INSTANCE);
        registerLogger(DiskLogger.INSTANCE);
    }

    public static void registerLogger(final AbstractHUDLogger LOGGER_INSTANCE) {
        LoggerRegistry.registerLogger(LOGGER_INSTANCE.NAME, LOGGER_INSTANCE);
    }
}
