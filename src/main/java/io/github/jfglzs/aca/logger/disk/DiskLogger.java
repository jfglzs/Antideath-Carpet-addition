package io.github.jfglzs.aca.logger.disk;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import io.github.jfglzs.aca.ACAEntry;
import io.github.jfglzs.aca.ACAServer;
import io.github.jfglzs.aca.event.onLogging;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import io.github.jfglzs.aca.utils.DataUtils;
import io.github.jfglzs.aca.utils.ThreadUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DiskLogger extends AbstractHUDLogger {
    public static final DiskLogger INSTANCE;
    public static final Map<String, DiskSnapShot> LAST_READ_AND_WRITE;
    public static volatile Component[] cache = new Component[0];

    static {
        try {
            INSTANCE = new DiskLogger(Loggers.class.getField("___disk"), "disk", null, null, true);
            LAST_READ_AND_WRITE = new HashMap<>();
            onLogging.event.register(INSTANCE::updateHUD);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private long lastRequest = 0;
    private CompletableFuture<Component[]> future = null;

    protected DiskLogger(Field acceleratorField, String logName, String def, String[] options, boolean isStrict) {
        super(acceleratorField, logName, def, options, isStrict);
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.___disk) {
            if (this.future == null || future.isDone()) {
                this.future = CompletableFuture.supplyAsync(this::logDisk, ThreadUtils.THREAD_POOL);
                this.future.whenComplete((c, e) -> {
                    if (e == null)
                        cache = c;
                    else
                        ACAEntry.LOGGER.error("err when log disk", e);
                });
            }
            LoggerRegistry.getLogger(this.NAME).log(() -> cache);
        }
    }

    public Component[] logDisk() {
        long current = System.currentTimeMillis();
        long timeDeltaMs = current - this.lastRequest;
        List<Component> disks = new ArrayList<>();
        List<HWDiskStore> diskStores = Loggers.SYSTEM_INFO.getHardware().getDiskStores();

        for (HWDiskStore store : diskStores) {
            disks.add(Messenger.c(String.format("g %s", store.getModel())));
            this.logReadAndWrite(store, disks, timeDeltaMs);
            this.logStorage(store, disks);
        }

        this.lastRequest = current;

        return disks.toArray(new Component[0]);
    }

    private void logReadAndWrite(HWDiskStore store, List<Component> disks, long timeDeltaMs) {
        if (! LAST_READ_AND_WRITE.containsKey(store.getName())) {
            LAST_READ_AND_WRITE.put(store.getName(), new DiskSnapShot(store.getReadBytes(), store.getWriteBytes()));
        }
        else if (timeDeltaMs > 0) {

            long currentRead = store.getReadBytes();
            long currentWrite = store.getWriteBytes();

            var snapShot = LAST_READ_AND_WRITE.get(store.getName());

            long readBytesDelta = currentRead - snapShot.readBytes();
            long writeBytesDelta = currentWrite - snapShot.writeBytes();

            double readSpeedMB = (readBytesDelta / (double) timeDeltaMs) * (1000.0 / 1024.0 / 1024.0);
            double writeSpeedMB = (writeBytesDelta / (double) timeDeltaMs) * (1000.0 / 1024.0 / 1024.0);

            disks.add(Messenger.c(String.format("g R: %.3f MB/s W: %.3f MB/S", readSpeedMB, writeSpeedMB)));
            LAST_READ_AND_WRITE.put(store.getName(), new DiskSnapShot(currentRead, currentWrite));
        }
    }

    private void logStorage(HWDiskStore store, List<Component> disks) {
        for (HWPartition partition : store.getPartitions()) {
            var mountPoint = partition.getMountPoint();
            if (mountPoint == null || mountPoint.isEmpty() || mountPoint.trim().contains("unknown"))
                continue;

            File f = new File(mountPoint);

            long usedSpace = f.getTotalSpace() - f.getFreeSpace();
            long totalSpace = f.getTotalSpace();

            if (mountPoint.equals("/")) {
                disks.add(Messenger.c("g %s %sGB/%sGB".formatted("RootFileSystem", usedSpace / DataUtils.GB, totalSpace / DataUtils.GB)));
            }
            else {
                disks.add(Messenger.c("g %s %sGB/%sGB".formatted(mountPoint, usedSpace / DataUtils.GB, totalSpace / DataUtils.GB)));
            }
        }
    }

    private record DiskSnapShot(long readBytes, long writeBytes) {
    }
}

