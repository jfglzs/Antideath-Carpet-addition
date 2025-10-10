package io.jfglzs.ad_carpet_addition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import io.jfglzs.ad_carpet_addition.command.CommandRegistry;
import io.jfglzs.ad_carpet_addition.logger.Loggers;
import io.jfglzs.ad_carpet_addition.utils.FlipCooldown;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.Map;


public class AcaExtension implements CarpetExtension , ModInitializer
{
	public static final String MOD_ID = "antideath-carpet-addition";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
	public void onInitialize()
    {
		LOGGER.info(MOD_ID + " is loading...");
        CarpetServer.manageExtension(this);
    }

    @Override
    public void onGameStarted()
    {
        FlipCooldown.init();
        CommandRegistry.registerCommands();
        CarpetServer.settingsManager.parseSettingsClass(AcaSetting.class);
        CarpetServer.settingsManager.registerRuleObserver(((serverCommandSource, rule, s) -> {
        }));
    }

    @Override
    public Map<String, String> canHasTranslations(String lang)
    {
        return RuleTranslator.getTranslationFromResourcePath(lang);
    }

    public static void getSensor()
    {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        System.out.println(hardware.getSensors().getCpuVoltage());
    }

    @Override
    public void registerLoggers()
    {
        Loggers.registerLogger();
    }
}