package io.github.jfglzs.aca.logger.physicMem;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import io.github.jfglzs.aca.event.LogEvent;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.lang.reflect.Field;

public class SystemMemoryLogger extends AbstractHUDLogger {
    public static final SystemMemoryLogger INSTANCE;

    static {
        try {
            INSTANCE = new SystemMemoryLogger(Loggers.class.getField("__sysMem"), "sysMemory", " ", null, false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected SystemMemoryLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
        LogEvent.event.register(this::updateHUD);

    }

    private static long toMiB(long bytes) {
        return bytes / 1024L / 1024L;
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__sysMem) {
            long free      = toMiB(Loggers.SYSTEM_INFO.getHardware().getMemory().getAvailable());
            long total     = toMiB(Loggers.SYSTEM_INFO.getHardware().getMemory().getTotal());
            long page      = toMiB(Loggers.SYSTEM_INFO.getHardware().getMemory().getVirtualMemory().getSwapUsed());
            long pageTotal = toMiB(Loggers.SYSTEM_INFO.getHardware().getMemory().getVirtualMemory().getSwapTotal());

            Text[] texts = {
                    Messenger.c(
                            String.format(
                                    "g Physical: %dM/%dM Virtual: %dM/%dM",
                                    total - pageTotal - free,
                                    total,
                                    page,
                                    pageTotal
                            )
                    )
            };
            LoggerRegistry.getLogger("sysMemory").log(() -> texts);
        }
    }
}
