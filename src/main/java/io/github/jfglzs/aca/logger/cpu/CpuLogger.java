package io.github.jfglzs.aca.logger.cpu;

import carpet.logging.LoggerRegistry;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;

public class CpuLogger extends AbstractHUDLogger {
    public static final CpuLogger INSTANCE;


    protected CpuLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__cpu) {
            LoggerRegistry.getLogger("cpu").log(CpuLoad::getCpuLoad);
        }
    }

    static {
        try {
            INSTANCE = new CpuLogger(Loggers.class.getField("__cpu"),"CPULogger","cpu load", new String[]{"percore","all","fullcore"},false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

