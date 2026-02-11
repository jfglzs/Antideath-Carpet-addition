package io.jfglzs.ad_carpet_addition.logger.cpu;

import carpet.utils.Messenger;
import com.sun.management.OperatingSystemMXBean;
import io.jfglzs.ad_carpet_addition.utils.ThreadUtils;
import net.minecraft.text.Text;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;

public class CpuLoad {
    private static final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    private static final SystemInfo info = new SystemInfo();
    private static volatile String perCoreLoad = " ";

    public static Text[] getCpuLoad(String option) {
        double cpuLoad = osBean.getCpuLoad() * 100;
        String color = Messenger.heatmap_color(cpuLoad, 100);
        Text perCore;

        synchronized (ThreadUtils.lock) {perCore = Messenger.c(perCoreLoad);}

        Text fullCore = Messenger.c(
                "g Cpu Load: ",
                String.format("%s %.2f%%", color, cpuLoad)
        );

        switch (option) {
            case "all" -> {return new Text[] {fullCore, perCore};}
            case "percore" -> {return new Text[] {perCore};}
            default -> {return new Text[] {fullCore};}
        }
    }

    private static Text[] getCpuPerCoreLoad() {
        CentralProcessor processor = info.getHardware().getProcessor();

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

            StringBuilder load = new StringBuilder("g ");

            // 格式化输出
            for (int i = 0; i < coreLoads.length; i++) {
                load.append(String.format("C%d: %.0f%% ", i, coreLoads[i] * 100));
            }

            synchronized (ThreadUtils.lock) {
                perCoreLoad = load.toString();
            }
        }
    }

    static {
        ThreadUtils.threadPool.submit(CpuLoad::getCpuPerCoreLoad);
    }
}
