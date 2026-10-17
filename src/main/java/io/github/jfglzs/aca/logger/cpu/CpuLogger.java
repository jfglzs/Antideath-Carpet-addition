package io.github.jfglzs.aca.logger.cpu;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import com.sun.management.OperatingSystemMXBean;
import io.github.jfglzs.aca.event.onLogging;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import io.github.pandalxb.jlibrehardwaremonitor.manager.LibreHardwareManager;
import io.github.pandalxb.jlibrehardwaremonitor.model.Computer;
import io.github.pandalxb.jlibrehardwaremonitor.model.Hardware;
import io.github.pandalxb.jlibrehardwaremonitor.model.Sensor;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.ArrayUtils;
import oshi.hardware.CentralProcessor;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.*;

public class CpuLogger extends AbstractHUDLogger {
    public static final CpuLogger INSTANCE;

    static {
        try {
            INSTANCE = new CpuLogger(Loggers.class.getField("__cpu"), "cpu", "cpu load", new String[]{"percore", "all", "fullcore", "others"}, true);
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
        if (Loggers.__cpu) {
            LoggerRegistry.getLogger(this.NAME).log(CpuLoadCalculator::getCpuLoad);
        }
    }

    static class CpuLoadCalculator {
        private static final OperatingSystemMXBean OS_BEAN = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        private static final Map<String, DoubleArrayList> INFO = new HashMap<>(Map.of("Temperature", new DoubleArrayList(), "Clock", new DoubleArrayList(), "Power", new DoubleArrayList(), "Voltage", new DoubleArrayList()));
        private static volatile List<Component> perCoreLoads = new ArrayList<>();
        private static volatile List<Component> perCoreLoads1 = new ArrayList<>();

        static {
            Thread.startVirtualThread(CpuLoadCalculator::getCpuPerCoreLoad);
        }

        public static Component[] getCpuLoad(String option) {
            double cpuLoad = OS_BEAN.getCpuLoad() * 100;
            String color = Messenger.heatmap_color(cpuLoad, 100);
            Component[] perCoreLoadArray = perCoreLoads.toArray(new Component[0]);

            Component fullCore = Messenger.c("g Cpu Load: ", String.format("%s %.2f%%", color, cpuLoad));

            if (option.equals("all")) {
                return ArrayUtils.add(perCoreLoadArray, fullCore);
            }
            else if (option.equals("percore")) {
                return perCoreLoadArray;
            }
            else if (option.equals("others")) {
                return perCoreLoads1.toArray(new Component[0]);
            }

            return new Component[]{fullCore};
        }

        private static void getCpuPerCoreLoad() {
            CentralProcessor processor = Loggers.SYSTEM_INFO.getHardware().getProcessor();

            // 第一次获取初始 Ticks
            long[][] prevTicks = processor.getProcessorCpuLoadTicks();

            while (true) {

                try {
                    getOtherInfo();
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // 计算当前时刻与上一次 Ticks 之间的负载
                double[] coreLoads = processor.getProcessorCpuLoadBetweenTicks(prevTicks);

                prevTicks = processor.getProcessorCpuLoadTicks();

                ArrayList<Component> list = new ArrayList<>();

                // 格式化输出
                for (int i = 0; i < coreLoads.length; i += 2) {
                    list.add(Messenger.c(coreLoad(i + 1, coreLoads[i]), "g  | ", coreLoad(i + 2, coreLoads[i + 1])));
                }

                perCoreLoads = list;
            }
        }

        private static void getOtherInfo() {
            Computer computer = LibreHardwareManager.createInstance().getComputer();
            computer.getHardware().stream().map(Hardware::getSensors).flatMap(Collection::stream).forEach(CpuLoadCalculator::sense);

            List<Component> components = new ArrayList<>();
            components.add(Messenger.c("g " + computer.getHardware().getFirst().getName()));
            components.add(Messenger.c("g Power: %.0fW".formatted(INFO.get("Power").getFirst())));

            int maxCores = Loggers.SYSTEM_INFO.getHardware().getProcessor().getPhysicalProcessors().size();

            for (int i = 0; i < maxCores; i++) {
                Component component = Messenger.c("g CPU%d: ".formatted(i + 1));

                for (Map.Entry<String, DoubleArrayList> entry : INFO.entrySet()) {
                    String type = entry.getKey();
                    DoubleArrayList values = entry.getValue();

                    if (i >= values.size())
                        continue;

                    double value = values.getDouble(i);

                    switch (type) {
                        case "Temperature" ->
                                component = Messenger.c(component, "%s %.0f°C ".formatted(Messenger.heatmap_color(value, 100f), value));
                        case "Clock" -> component = Messenger.c(component, "g %.0fMHz ".formatted(value));
                        case "Voltage" -> component = Messenger.c(component, "g %.3fV ".formatted(value));
                    }
                }

                components.add(component);
            }

            perCoreLoads1 = components;
            INFO.values().forEach(DoubleArrayList::clear);
        }

        private static void sense(Sensor sensor) {
            String name = sensor.getName();
            double value = sensor.getValue();
            String type = sensor.getSensorType();

            if (type.equals("Temperature") && name.contains("CPU") && ! name.contains("TjMax") && ! name.equals("Package"))
                INFO.get(type).add(value);
            if (type.equals("Clock") && ! name.equals("Bus Speed"))
                INFO.get(type).add(value);
            if (type.equals("Power"))
                INFO.get(type).add(value);
            if (type.equals("Voltage") && ! name.equals("CPU Core"))
                INFO.get(type).add(value);
        }

        private static Component coreLoad(int core, double load) {
            double percent = load * 100;
            String coreInfo = "g C%s: ".formatted(core);
            String coreLoad = "%s %.0f%%".formatted(Messenger.heatmap_color(percent, 100), percent);
            return Messenger.c(coreInfo, coreLoad);
        }
    }
}

