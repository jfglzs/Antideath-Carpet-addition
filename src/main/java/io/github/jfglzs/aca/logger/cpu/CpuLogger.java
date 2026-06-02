package io.github.jfglzs.aca.logger.cpu;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import com.sun.management.OperatingSystemMXBean;
import io.github.jfglzs.aca.event.onLogging;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class CpuLogger extends AbstractHUDLogger {
    public static final CpuLogger INSTANCE;

    static {
        try {
            INSTANCE = new CpuLogger(Loggers.class.getField("__cpu"), "cpu", "cpu load", new String[]{"percore", "both", "fullcore"}, true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected CpuLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
        onLogging.event.register(this::updateHUD);
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__cpu) {
            LoggerRegistry.getLogger(this.NAME).log(CpuLoadCalculator::getCpuLoad);
        }
    }

    static class CpuLoadCalculator {
        private static final OperatingSystemMXBean OS_BEAN = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        private static volatile ArrayList<Component> perCoreLoads = new ArrayList<>();

        static {
            Thread.startVirtualThread(CpuLoadCalculator::getCpuPerCoreLoad);
        }

        public static Component[] getCpuLoad(String option) {
            double cpuLoad = OS_BEAN.getCpuLoad() * 100;
            String color = Messenger.heatmap_color(cpuLoad, 100);
            Component[] perCoreLoadArray = perCoreLoads.toArray(new Component[0]);

            Component fullCore = Messenger.c(
                    "g Cpu Load: ",
                    String.format("%s %.2f%%", color, cpuLoad)
            );

            if (option.equals("all")) {
                return ArrayUtils.add(perCoreLoadArray, fullCore);
            } else if (option.equals("percore")) {
                return perCoreLoadArray;
            } else {
                return new Component[]{fullCore};
            }
        }

        private static void getCpuPerCoreLoad() {
            CentralProcessor processor = Loggers.SYSTEM_INFO.getHardware().getProcessor();

            // 第一次获取初始 Ticks
            long[][] prevTicks = processor.getProcessorCpuLoadTicks();

            while (true) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 计算当前时刻与上一次 Ticks 之间的负载
                double[] coreLoads = processor.getProcessorCpuLoadBetweenTicks(prevTicks);

                prevTicks = processor.getProcessorCpuLoadTicks();

                ArrayList<Component> list = new ArrayList<>();

                // 格式化输出
                for (int i = 0; i < coreLoads.length; i += 2) {
                    list.add(Messenger.c(
                            coreLoad(i + 1, coreLoads[i]),
                                   "g  | ",
                                   coreLoad(i + 2, coreLoads[i + 1])
                            )
                    );
                }

                perCoreLoads = list;
            }
        }

        private static Component coreLoad(int core, double load) {
            double percent = load * 100;
            String coreInfo = "g C%s: ".formatted(core);
            String coreLoad = "%s %.0f%%".formatted(
                    Messenger.heatmap_color(percent, 100),
                    percent
            );
            return Messenger.c(coreInfo, coreLoad);
        }
    }
}

