package io.github.jfglzs.aca;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import io.github.jfglzs.aca.command.*;
import io.github.jfglzs.aca.logger.Loggers;
import io.github.jfglzs.aca.utils.RuleTranslator;
import io.github.jfglzs.aca.utils.ThreadUtils;
import io.github.jfglzs.aca.utils.config.ConfigUtils;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;

import java.util.Map;

public class ACAServer implements CarpetExtension {
    public static final ACAServer INSTANCE = new ACAServer();

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(AcaSetting.class);
        CarpetServer.settingsManager.registerRuleObserver(((source, rule, s) ->{}));
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
        Loggers.registerLoggers();
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
        SearchEntityCommand.registerCommand(dispatcher);
        PreventCommand.registerCommand(dispatcher);
        PlayerLockCommand.registerCommand(dispatcher);
    }

    @Override
    public String version() {
        return ACAEntry.MOD_ID;
    }
}
