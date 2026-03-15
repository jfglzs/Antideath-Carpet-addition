import carpet.utils.Messenger;
import io.github.jfglzs.aca.logger.Loggers;
import oshi.SystemInfo;
import oshi.hardware.NetworkIF;

import static io.github.jfglzs.aca.logger.network.NetworkLogger.blackList;

public class test {
    public static void main(String[] args) {
        SystemInfo info = new SystemInfo();
        for (NetworkIF nif : info.getHardware().getNetworkIFs()) {

                if (!nif.isKnownVmMacAddr() && nif.isConnectorPresent()) {
                    System.out.println(nif.getDisplayName());
                }


        }
    }
}