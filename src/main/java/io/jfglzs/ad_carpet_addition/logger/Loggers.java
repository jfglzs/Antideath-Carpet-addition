package io.jfglzs.ad_carpet_addition.logger;


import carpet.logging.LoggerRegistry;

public class Loggers
{
    public static boolean __cpu = false;

    public static void registerLogger()
    {
        LoggerRegistry.registerLogger("cpu",CpuLogger.INSTANCE);
    }
}
