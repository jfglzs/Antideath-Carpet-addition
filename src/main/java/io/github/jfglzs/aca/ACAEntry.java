package io.github.jfglzs.aca;

import carpet.CarpetServer;
import io.github.jfglzs.aca.utils.config.ConfigUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ACAEntry implements ModInitializer {
    public static final String MOD_ID = "antideath-carpet-addition";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info(MOD_ID + " is loading...");
        CarpetServer.manageExtension(ACAServer.INSTANCE);
        if (!ConfigUtils.init()) LOGGER.error("Config Initialize Failed");
        ConfigUtils.loadConfigFile();
    }
}