package io.github.jfglzs.aca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import io.github.jfglzs.aca.command.*;
import io.github.jfglzs.aca.logger.Loggers;
import io.github.jfglzs.aca.utils.EntityUtils;
import io.github.jfglzs.aca.utils.RuleTranslator;
import io.github.jfglzs.aca.utils.ThreadUtils;
import io.github.jfglzs.aca.utils.config.ConfigUtils;
import io.github.jfglzs.aca.utils.wrap.Cache;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;

import java.util.Map;

public class ACAServer implements CarpetExtension {
    public static final ACAServer INSTANCE = new ACAServer();
    public static final MinecraftServer MINECRAFT_SERVER = CarpetServer.minecraft_server;
    public static final EnvType ENV_TYPE = FabricLoader.getInstance().getEnvironmentType();


    @Override
    public void onGameStarted() {
        this.initConfig();
        CarpetServer.settingsManager.parseSettingsClass(AcaSetting.class);
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        ConfigUtils.saveConfig();
        ThreadUtils.THREAD_POOL.shutdownNow();
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return RuleTranslator.getTranslationFromResourcePath(lang);
    }

    @Override
    public void registerLoggers() {
        Loggers.registerLoggers();
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
        PlayerCommand.registerCommand(dispatcher);
    }

    @Override
    public String version() {
        return ACAEntry.MOD_ID;
    }

    public void initConfig() {
        if (!ConfigUtils.init()) {
            ACAEntry.LOGGER.error("cannot create config directory, Use default settings");
        }
        else {
            ConfigUtils.loadConfigFile();
        }
    }
}
