package io.jfglzs.ad_carpet_addition.logger;

import carpet.logging.LoggerRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CpuLogger extends AbstractHUDLogger
{
    public static final CpuLogger INSTANCE;
    static Text cpuInfo = Text.of(getHardware().getProcessor().getProcessorIdentifier().getName());

    static
    {
        try
        {
            INSTANCE = new CpuLogger(Loggers.class.getField("__cpu"),"CPULogger"," ",null,false);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected CpuLogger(Field acceleratorField, String logName, String def, String[] options, boolean strictOptions)
    {
        super(acceleratorField, logName, def, options, strictOptions);
    }

    @Override
    public void updateHUD(MinecraftServer server)
    {
        List<Text> list = new ArrayList<>();
        list.add(cpuInfo);
        LoggerRegistry.getLogger("cpu").log(() -> list.toArray(new Text[0]));
    }

    public static HardwareAbstractionLayer getHardware()
    {
        SystemInfo systemInfo = new SystemInfo();
        return systemInfo.getHardware();
    }
}
