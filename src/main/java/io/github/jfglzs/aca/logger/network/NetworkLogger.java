package io.github.jfglzs.aca.logger.network;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import io.github.jfglzs.aca.event.LogEvent;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import oshi.hardware.NetworkIF;

import java.lang.reflect.Field;
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
            INSTANCE = new NetworkLogger(Loggers.class.getField("__network"), "network", " ", null, false);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    protected NetworkLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions) {
        super(acceleratorField, logName, def, options, strictOptions);
        LogEvent.event.register(this::updateHUD);
    }

    public long lastUpdate = 0L;
    public long lastRecv   = 0L;
    public long lastSent   = 0L;

    @Override
    public void updateHUD(MinecraftServer server) {
        if (Loggers.__network) {
            for (NetworkIF nif : Loggers.SYSTEM_INFO.getHardware().getNetworkIFs()) {
                if (this.isPhysicDevice(nif)) {
                    long timeDiff = System.currentTimeMillis() - lastUpdate;
                    if (timeDiff > 0) {
                        nif.updateAttributes();
                        long bytesRecv      = nif.getBytesRecv();
                        long bytesSent      = nif.getBytesSent();
                        double uploadMbps   = ((bytesSent - this.lastSent) * 8.0 / (1024 * 1024)) / (timeDiff / 1000.0);
                        double downloadMbps = ((bytesRecv - this.lastRecv) * 8.0 / (1024 * 1024)) / (timeDiff / 1000.0);
                        Text[] text = {
                                Messenger.c(
                                        String.format(
                                                "g ⬆Upload: %.3f Mbps ⬇Download: %.3f Mbps",
                                                uploadMbps,
                                                downloadMbps
                                        )
                                )
                        };
                        this.lastRecv   = bytesRecv;
                        this.lastSent   = bytesSent;
                        this.lastUpdate = System.currentTimeMillis();

                        LoggerRegistry.getLogger("network").log(() -> text);
                        break;
                    }
                }
            }
        }
    }

    public boolean isPhysicDevice(NetworkIF nif) {
//        for (String s : blackList) {
//            if (nif.getDisplayName().toLowerCase().contains(s)) {
//                return false;
//            }
//        }
        return blackList.stream().noneMatch(nif.getDisplayName().toLowerCase()::contains) && !nif.isKnownVmMacAddr();
    }
}
