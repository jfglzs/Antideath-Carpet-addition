package io.jfglzs.ad_carpet_addition.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class commandRegistry {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            fastOpCommand.register(dispatcher);
        });
    }
}