package io.github.jfglzs.aca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Messenger;
import io.github.jfglzs.aca.command.CommandRegistry;
import io.github.jfglzs.aca.logger.Loggers;
import io.github.jfglzs.aca.utils.RuleTranslator;
import io.github.jfglzs.aca.utils.ThreadUtils;
import io.github.jfglzs.aca.utils.config.ConfigUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class AcaExtension implements CarpetExtension, ModInitializer {
    public static final String MOD_ID = "antideath-carpet-addition";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info(MOD_ID + " is loading...");
        CarpetServer.manageExtension(this);
        if (!ConfigUtils.init()) LOGGER.error("Config Initialize Failed");
        ConfigUtils.loadConfigFile();
    }

    @Override
    public void onGameStarted() {
        CommandRegistry.registerCommands();
        CarpetServer.settingsManager.parseSettingsClass(AcaSetting.class);
        CarpetServer.settingsManager.registerRuleObserver(((source, rule, s) -> {
            if (rule.name().equals("softAnvil") && AcaSetting.anvilNeverDamageByFalling) {
                Messenger.m(source, "r Rule: ", "g softAnvil", "r is incompatible with", "g anvilNeverDamageByFalling");
                AcaSetting.softAnvil = false;
            } else if (rule.name().equals("anvilNeverDamageByFalling") && AcaSetting.softAnvil) {
                Messenger.m(source, "r Rule: ", "g anvilNeverDamageByFalling", "r is incompatible with", "g softAnvil");
                AcaSetting.anvilNeverDamageByFalling = false;
            }
        }));
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        ConfigUtils.saveConfig();
        ThreadUtils.threadPool.shutdown();
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return RuleTranslator.getTranslationFromResourcePath(lang);
    }

    @Override
    public void registerLoggers() {
        Loggers.registerLogger();
    }
}