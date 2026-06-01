import io.github.jfglzs.aca.logger.Loggers;
import oshi.hardware.HWDiskStore;

import java.util.List;

public class test {
    public static void main(String[] args) {
        List<HWDiskStore> diskStores = Loggers.SYSTEM_INFO.getHardware().getDiskStores();
        diskStores.forEach(diskStore -> {
            System.out.println(diskStore.getModel());
            System.out.println(diskStore.getReadBytes());
            System.out.println(diskStore.getWriteBytes());
            System.out.println(diskStore.getSize());
        });
    }
}