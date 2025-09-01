package io.jfglzs.ad_carpet_addition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import io.jfglzs.ad_carpet_addition.command.commandRegistry;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class AcaExtension implements CarpetExtension , ModInitializer {
//    private static SettingsManager antideathSettingManager;
	public static final String MOD_ID = "antideath-carpet-addition";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
	public void onInitialize() {
		LOGGER.info(MOD_ID + " is loading...");
            CarpetServer.manageExtension(this);
	}

    @Override
    public void onGameStarted() {
        commandRegistry.registerCommands();
        CarpetServer.settingsManager.parseSettingsClass(AcaSetting.class);
        CarpetServer.settingsManager.registerRuleObserver(((serverCommandSource, rule, s) -> {
        }));
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return Rule_Translator.getTranslationFromResourcePath(lang);
    }
}