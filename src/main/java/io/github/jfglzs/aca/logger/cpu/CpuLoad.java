package io.github.jfglzs.aca.logger.cpu;

import carpet.utils.Messenger;
import com.sun.management.OperatingSystemMXBean;
import io.github.jfglzs.aca.logger.Loggers;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CpuLoad {
    private static final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static final List<Text> perCoreLoad = new CopyOnWriteArrayList<>();

    public static Text[] getCpuLoad(String option) {
        double cpuLoad = osBean.getCpuLoad() * 100;
        String color = Messenger.heatmap_color(cpuLoad, 100);
        Text[] text = perCoreLoad.toArray(new Text[0]);

        Text fullCore = Messenger.c(
                "g Cpu Load: ",
                String.format("%s %.2f%%", color, cpuLoad)
        );

        switch (option) {
            case "all" -> {
                return ArrayUtils.add(text, fullCore);
            }
            case "percore" -> {
                return text;
            }
            default -> {
                return new Text[] {fullCore};
            }
        }
    }

    private static void getCpuPerCoreLoad() {
        CentralProcessor processor = Loggers.sysInfo.getHardware().getProcessor();

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

            perCoreLoad.clear();

            // 格式化输出
            for (int i = 0; i < coreLoads.length; i += 2) {
                double cl1 = coreLoads[i] * 100;
                double cl2 = coreLoads[i] * 100;

                String coreInfo1 = "g C%d: ".formatted(i + 1);

                String cpuLoad1 = "%s %.0f%%".formatted(
                        Messenger.heatmap_color(cl1, 100),
                        cl1
                );


                String coreInfo2 = "g C%d: ".formatted(i + 2);

                String cpuLoad2 = "%s %.0f%%".formatted(
                        Messenger.heatmap_color(cl2, 100),
                        cl2
                );

                perCoreLoad.add(Messenger.c(coreInfo1, cpuLoad1, " | ", coreInfo2, cpuLoad2));
            }
        }
    }

    static {
        Thread.startVirtualThread(CpuLoad::getCpuPerCoreLoad);
    }
}
