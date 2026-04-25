package io.github.jfglzs.aca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import io.github.jfglzs.aca.command.CommandRegistry;
import io.github.jfglzs.aca.logger.Loggers;
import io.github.jfglzs.aca.utils.RuleTranslator;
import io.github.jfglzs.aca.utils.ThreadUtils;
import io.github.jfglzs.aca.utils.config.ConfigUtils;
import net.minecraft.server.MinecraftServer;

import java.util.Map;

public class ACAServer implements CarpetExtension {
    public static final ACAServer INSTANCE = new ACAServer();

    private ACAServer() {}

    @Override
    public void onGameStarted() {
        CommandRegistry.registerCommands();
        CarpetServer.settingsManager.parseSettingsClass(AcaSetting.class);
        CarpetServer.settingsManager.registerRuleObserver(((source, rule, s) -> {
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

    @Override
    public String version() {
        return ACAEntry.MOD_ID;
    }
}
