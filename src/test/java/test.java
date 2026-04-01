import oshi.SystemInfo;
import oshi.hardware.NetworkIF;

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