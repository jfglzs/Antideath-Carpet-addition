package xyz.antideath.aca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.google.common.util.concurrent.RateLimiter;
import xyz.antideath.aca.command.CommandRegistry;
import xyz.antideath.aca.logger.Loggers;
import xyz.antideath.aca.utils.ConfigUtils;
import xyz.antideath.aca.utils.ThreadUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class AcaExtension implements CarpetExtension , ModInitializer {
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
        CarpetServer.settingsManager.registerRuleObserver(((source, rule, s) -> {}));
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayerEntity player) {
        AcaSetting.sneakCooldownMap.put(player, RateLimiter.create(2));
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayerEntity player) {
        AcaSetting.sneakCooldownMap.remove(player);
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
    public void registerLoggers()
    {
        Loggers.registerLogger();
    }
}