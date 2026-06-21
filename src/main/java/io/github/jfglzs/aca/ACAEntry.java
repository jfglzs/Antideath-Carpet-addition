package io.github.jfglzs.aca;

import carpet.CarpetServer;
import carpet.api.settings.Rule;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ACAEntry implements ModInitializer {
    public static final String MOD_ID = "antideath-carpet-addition";
    public static final String FANCY_NAME = "Antideath Carpet Addition";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final int ruleAmount;
    public static String MOD_VER;

    @Override
    public void onInitialize() {
        MOD_VER = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();

        LOGGER.info("{} v{} is loading... {} Rules loaded", MOD_ID, MOD_VER, ruleAmount);
        CarpetServer.manageExtension(ACAServer.INSTANCE);
    }

    static {
        ruleAmount = (int) Arrays.stream(AcaSetting.class.getFields()).filter(field -> field.isAnnotationPresent(Rule.class)).count();
    }
}