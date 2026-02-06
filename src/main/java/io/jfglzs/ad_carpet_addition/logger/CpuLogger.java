package io.jfglzs.ad_carpet_addition.logger;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import com.sun.management.OperatingSystemMXBean;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;

public class CpuLogger extends AbstractHUDLogger {
    public static final CpuLogger INSTANCE;
    private static final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    protected CpuLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__cpu) {
            LoggerRegistry.getLogger("cpu").log(CpuLogger::getCpuLoad);
        }
    }

    private static Text[] getCpuLoad() {
        double cpuLoad = osBean.getCpuLoad() * 100;
        String color = Messenger.heatmap_color(cpuLoad, 100);
        return new Text[] {
                Messenger.c(
                        "g Cpu Usage: ",
                        String.format("%s %.2f%%", color, cpuLoad)
                )
        };
    }

    static {
        try {
            INSTANCE = new CpuLogger(Loggers.class.getField("__cpu"),"CPULogger"," ",null,false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}

