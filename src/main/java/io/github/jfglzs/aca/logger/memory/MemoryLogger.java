package io.github.jfglzs.aca.logger.memory;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import io.github.jfglzs.aca.event.LogEvent;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import io.github.jfglzs.aca.utils.DataUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;

public class MemoryLogger extends AbstractHUDLogger {
    public static final MemoryLogger INSTANCE;

    static {
        try {
            INSTANCE = new MemoryLogger(Loggers.class.getField("__sysMem"), "sysMemory", " ", new String[]{"Physical","Swap","Both"}, true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected MemoryLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
        LogEvent.event.register(this::updateHUD);

    }

    private long toMiB(long bytes) {
        return bytes / DataUtils.MB;
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__sysMem) {
            LoggerRegistry.getLogger("sysMemory").log(this::logMemory);
        }
    }

    private Component[] logMemory(String option) {
        long free      = toMiB(Loggers.SYSTEM_INFO.getHardware().getMemory().getAvailable());
        long total     = toMiB(Loggers.SYSTEM_INFO.getHardware().getMemory().getTotal());
        long page      = toMiB(Loggers.SYSTEM_INFO.getHardware().getMemory().getVirtualMemory().getSwapUsed());
        long pageTotal = toMiB(Loggers.SYSTEM_INFO.getHardware().getMemory().getVirtualMemory().getSwapTotal());

        String message = switch (option) {
            case "Physical" -> String.format("g Physical: %dM/%dM", total - free, total);
            case "Swap"     -> String.format("g Swap: %dM/%dM", page, pageTotal);
            default         -> String.format("g Physical: %dM/%dM Swap: %dM/%dM", total - free, total, page, pageTotal);
        };
        return new Text[] {Messenger.c(message)};
    }
}
