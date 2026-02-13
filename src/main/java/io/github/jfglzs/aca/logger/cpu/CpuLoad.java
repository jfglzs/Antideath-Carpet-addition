package io.github.jfglzs.aca.logger.cpu;

import carpet.utils.Messenger;
import com.sun.management.OperatingSystemMXBean;
import io.github.jfglzs.aca.utils.ThreadUtils;
import net.minecraft.text.Text;
import org.apache.commons.lang3.ArrayUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class CpuLoad {
    private static final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static final SystemInfo info = new SystemInfo();
    private static volatile List<Text> perCoreLoad = new ArrayList<>();

    public static Text[] getCpuLoad(String option) {
        double cpuLoad = osBean.getCpuLoad() * 100;
        String color = Messenger.heatmap_color(cpuLoad, 100);
        Text[] text;

        //从另外一个线程同步数据
        synchronized (ThreadUtils.lock) {
            text = perCoreLoad.toArray(new Text[0]);
        }

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
        CentralProcessor processor = info.getHardware().getProcessor();
        List<Text> load = new ArrayList<>();

        // 第一次获取初始 Ticks
        long[][] prevTicks = processor.getProcessorCpuLoadTicks();

        while (true) {

            // 采样间隔（1秒）
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {
            }

            // 计算当前时刻与上一次 Ticks 之间的负载
            double[] coreLoads = processor.getProcessorCpuLoadBetweenTicks(prevTicks);

            // 更新 prevTicks，为下一次循环做准备
            prevTicks = processor.getProcessorCpuLoadTicks();

            // 格式化输出
            for (int i = 0; i < coreLoads.length; i++) {
                double cl = coreLoads[i] * 100; //单核心负载

                load.add(
                        Messenger.c(
                                "%s C%d: %.0f%%".formatted(
                                        Messenger.heatmap_color(cl,100),
                                        i,
                                        cl
                        ))
                );
            }

            synchronized (ThreadUtils.lock) {
                perCoreLoad = List.copyOf(load);
            }
            load.clear();
        }
    }

    static {
        ThreadUtils.threadPool.submit(CpuLoad::getCpuPerCoreLoad);
    }
}
