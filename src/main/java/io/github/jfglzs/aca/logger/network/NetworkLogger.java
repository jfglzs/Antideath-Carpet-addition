package io.github.jfglzs.aca.logger.network;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import io.github.jfglzs.aca.event.LoggerUpdateEvent;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import oshi.hardware.NetworkIF;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class NetworkLogger extends AbstractHUDLogger {
    public static final NetworkLogger INSTANCE;
    public static Set<String> blackList = Set.of(
            "virtual",
            "vpn",
            "tun",
            "tunnel",
            "radmin"
    );

    static {
        try {
            INSTANCE = new NetworkLogger(Loggers.class.getField("__network"), "MemoryLogger", " ", null, false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public long lastUpdate = 0L;
    public long lastRecv = 0L;
    public long lastSent = 0L;

    protected NetworkLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
        LoggerUpdateEvent.event.register(this::updateHUD);
    }

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__network) {
            List<Text> list = new ArrayList<>();
            for (NetworkIF nif : Loggers.sysInfo.getHardware().getNetworkIFs()) {
                if (this.isPhysicDevice(nif)) {
                    long timediff = System.currentTimeMillis() - lastUpdate;
                    if (timediff > 0) {
                        nif.updateAttributes();
                        long bytesRecv = nif.getBytesRecv();
                        long bytesSent = nif.getBytesSent();
//                      System.out.println("recv " + bytesRecv + " bytes");
//                      System.out.println("sent " + bytesSent + " bytes");
//                      System.out.println("millis " + millis + " ms");
//                      System.out.println("millis " + lastUpdate + " ms");
                        double uploadMbps = ((bytesSent - this.lastSent) * 8.0 / (1024 * 1024)) / (timediff / 1000.0);
                        double downloadMbps = ((bytesRecv - this.lastRecv) * 8.0 / (1024 * 1024)) / (timediff / 1000.0);
                        list.add(
                                Messenger.c(
                                        String.format(
                                                "g ⬆Upload: %.3f Mbps ⬇Download: %.3f Mbps",
                                                uploadMbps,
                                                downloadMbps
                                        )
                                )
                        );
                        this.lastRecv = bytesRecv;
                        this.lastSent = bytesSent;
                        this.lastUpdate = System.currentTimeMillis();
                        break;
                    }
                }
            }
            LoggerRegistry.getLogger("network").log(() -> list.toArray(new Text[0]));
        }
    }

    public boolean isPhysicDevice(NetworkIF nif) {
        for (String s : blackList) {
            if (nif.getDisplayName().toLowerCase().contains(s)) {
                return false;
            }
        }
        return !nif.isKnownVmMacAddr();
    }
}
