package io.jfglzs.ad_carpet_addition.logger.memory;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import io.jfglzs.ad_carpet_addition.logger.AbstractHUDLogger;
import io.jfglzs.ad_carpet_addition.logger.Loggers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MemoryLogger extends AbstractHUDLogger {
    public static final MemoryLogger INSTANCE;
    private static final AllocationRateCalculator allocationRateCalculator;

    protected MemoryLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__mem) {
            List<Text> list = new ArrayList<>();
            long free = Runtime.getRuntime().freeMemory();
            long total = Runtime.getRuntime().totalMemory();

            list.add(
                    Messenger.c(
                            String.format(
                                    Locale.ROOT,
                                    "g Allocation rate: %03dMB/s",
                                    toMiB(allocationRateCalculator.get(total - free))
                            )
                    )
            );
            LoggerRegistry.getLogger("memAllocate").log(() -> list.toArray(new Text[0]));
        }
    }

    private static long toMiB(long bytes)
    {
        return bytes / 1024L / 1024L;
    }

    static {
        try {
            INSTANCE = new MemoryLogger(Loggers.class.getField("__mem"),"MemoryLogger"," ",null,false);
            allocationRateCalculator = new AllocationRateCalculator();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static class AllocationRateCalculator {

        private static final List<GarbageCollectorMXBean> GARBAGE_COLLECTORS = ManagementFactory.getGarbageCollectorMXBeans();
        private long lastCalculated = 0L;
        private long allocatedBytes = -1L;
        private long collectionCount = -1L;
        private long allocationRate = 0L;

        long get(long allocatedBytes) {
            long l = System.currentTimeMillis();
            if (l - this.lastCalculated < 500L) {
                return this.allocationRate;
            } else {
                long m = getCollectionCount();
                if (this.lastCalculated != 0L && m == this.collectionCount) {
                    double d = (double) TimeUnit.SECONDS.toMillis(1L) / (double)(l - this.lastCalculated);
                    long n = allocatedBytes - this.allocatedBytes;
                    this.allocationRate = Math.round((double)n * d);
                }

                this.lastCalculated = l;
                this.allocatedBytes = allocatedBytes;
                this.collectionCount = m;
                return this.allocationRate;
            }
        }

        private static long getCollectionCount() {
            long l = 0L;

            for(GarbageCollectorMXBean garbageCollectorMXBean : GARBAGE_COLLECTORS) {
                l += garbageCollectorMXBean.getCollectionCount();
            }

            return l;
        }
    }
}
