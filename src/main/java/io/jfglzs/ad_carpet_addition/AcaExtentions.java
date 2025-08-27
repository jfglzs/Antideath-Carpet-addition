package io.jfglzs.ad_carpet_addition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AcaExtentions implements CarpetExtension,ModInitializer {
    private static SettingsManager antideathSettingManager;
	public static final String MOD_ID = "antideath-carpet-addition";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
//    public static final String mod_version = "1.0";
//    public static final String MOD_NAME = "ACA";
    @Override
	public void onInitialize() {
		LOGGER.info("AntiDeath Carpet Addition is loading...");
            CarpetServer.manageExtension(this);
	}

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(AcaSetting.class);
        CarpetServer.settingsManager.registerRuleObserver(((serverCommandSource, rule, s) -> {
        }));

    }

}