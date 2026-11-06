package io.github.jfglzs.aca.logger.cpu;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import com.sun.management.OperatingSystemMXBean;
import io.github.jfglzs.aca.event.onLogging;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import io.github.jfglzs.aca.utils.ThreadUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.locks.LockSupport;

public class CpuLogger extends AbstractHUDLogger {
    public static final CpuLogger INSTANCE;

    static {
        try {
            INSTANCE = new CpuLogger(Loggers.class.getField("__cpu"), "cpu", "cpu load", new String[]{"percore", "all", "fullcore"}, true);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected CpuLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
        onLogging.event.register(this::updateHUD);
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__cpu)
            LoggerRegistry.getLogger(this.NAME).log(CpuLoadCalculator::getCpuLoad);
    }

    static class CpuLoadCalculator {
        private static final OperatingSystemMXBean OS_BEAN = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        private static final Component CPU_NAME = Messenger.c("g CPU: " + StringUtils.stripEnd(Loggers.SYSTEM_INFO.getHardware().getProcessor().getProcessorIdentifier().getName(), " "));
        private static final String SPLIT = "g  | ";
        private static volatile double[] coreLoads = new double[0];

        static {
            ThreadUtils.THREAD_POOL.submit(CpuLoadCalculator::getCpuPerCoreLoad);
        }

        public static Component[] getCpuLoad(String option) {
            double cpuLoad = OS_BEAN.getCpuLoad() * 100;
            String color = Messenger.heatmap_color(cpuLoad, 100);
            Component fullCoreText = Messenger.c("g Cpu Load: ", String.format("%s %.2f%%", color, cpuLoad));
            List<Component> resultText = new ArrayList<>();
            List<Component> percoreText = new ArrayList<>();

            if (coreLoads.length % 4 == 0) {
                for (int i = 0; i < coreLoads.length; i += 4)
                    percoreText.add(Messenger.c(
                            coreLoad(i + 1, coreLoads[i]),
                            SPLIT,
                            coreLoad(i + 2, coreLoads[i + 1]),
                            SPLIT,
                            coreLoad(i + 3, coreLoads[i + 2]),
                            SPLIT,
                            coreLoad(i + 4, coreLoads[i + 3]))
                                   );
            }
            else {
                for (int i = 0; i < coreLoads.length; i += 2)
                    percoreText.add(Messenger.c(coreLoad(i + 1, coreLoads[i]), SPLIT, coreLoad(i + 2, coreLoads[i + 1])));
            }

            resultText.add(CPU_NAME);

            if (option.equals("all")) {
                resultText.addAll(percoreText);
                resultText.add(fullCoreText);
            }
            else if (option.equals("percore")) {
                resultText.addAll(percoreText);
            }
            else {
                resultText.add(fullCoreText);
            }

            return resultText.toArray(new Component[0]);
        }

        private static void getCpuPerCoreLoad() {
            CentralProcessor processor = Loggers.SYSTEM_INFO.getHardware().getProcessor();

            long[][] prevTicks = processor.getProcessorCpuLoadTicks();

            while (true) {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception ignored) {
                }
                finally {
                    coreLoads = processor.getProcessorCpuLoadBetweenTicks(prevTicks);
                    prevTicks = processor.getProcessorCpuLoadTicks();
                }
            }
        }

        private static Component coreLoad(int core, double load) {
            double percent = load * 100;

            String coreInfo = "g C%s: ".formatted(core >= 10 ? core : "0" + core);
            String coreLoad = "%s %4.0f%%".formatted(Messenger.heatmap_color(percent, 100), percent);

            return Messenger.c(coreInfo, coreLoad);
        }
    }
}

