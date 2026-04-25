package io.github.jfglzs.aca.logger.network;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import io.github.jfglzs.aca.event.LogEvent;
import io.github.jfglzs.aca.logger.AbstractHUDLogger;
import io.github.jfglzs.aca.logger.Loggers;
import io.github.jfglzs.aca.utils.DataUtils;
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
            INSTANCE = new NetworkLogger(Loggers.class.getField("__network"), "network", " ", new String[]{"uploadAndDownload","totalUploadAndDownload","all"}, true);
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
                    LoggerRegistry.getLogger("network").log(option -> this.logNetwork(option, nif));
                    return;
                }
            }
        }
    }

    private Text[] logNetwork(String option, NetworkIF nif) {
        long timeDiff = System.currentTimeMillis() - lastUpdate;

        if (timeDiff > 0) {
            nif.updateAttributes();
            long bytesRecv      = nif.getBytesRecv();
            long bytesSent      = nif.getBytesSent();
            double uploadMbps   = ((bytesSent - this.lastSent) * 8.0 / (1024 * 1024)) / (timeDiff / 1000.0);
            double downloadMbps = ((bytesRecv - this.lastRecv) * 8.0 / (1024 * 1024)) / (timeDiff / 1000.0);

            String message = switch (option) {
                case "uploadAndDownload"      -> String.format("g Upload: %.3f Mbps Download: %.3f Mbps", uploadMbps, downloadMbps);
                case "totalUploadAndDownload" -> String.format("g TotalUpload: %s TotalDownload: %s", this.calculate(bytesRecv), this.calculate(bytesSent));
                default                       -> String.format("g TotalUpload: %s TotalDownload: %s \nUpload: %.3f Mbps Download: %.3f Mbps", this.calculate(bytesRecv), this.calculate(bytesSent), uploadMbps, downloadMbps);
            };

            this.lastRecv   = bytesRecv;
            this.lastSent   = bytesSent;
            this.lastUpdate = System.currentTimeMillis();

            return new Text[]{Messenger.c(message)};
        }

        return new Text[0];
    }

    private String calculate(long bytes) {
        if (bytes * 8L / DataUtils.MB > 1000) {
            return String.format("%d GB", bytes * 8 / DataUtils.GB);
        }
        return String.format("%d MB", bytes * 8 / DataUtils.MB);
    }

    public boolean isPhysicDevice(NetworkIF nif) {
        return blackList.stream().noneMatch(nif.getDisplayName().toLowerCase()::contains) && !nif.isKnownVmMacAddr();
    }
}

