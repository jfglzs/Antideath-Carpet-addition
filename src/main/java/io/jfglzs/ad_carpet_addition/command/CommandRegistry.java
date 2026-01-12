package io.jfglzs.ad_carpet_addition.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CommandRegistry
{
    public static void registerCommands()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            FastOpCommand.registerCommand(dispatcher);
            SearchEntityCommand.registerCommand(dispatcher);
            SpecTeleportCommand.registerCommand(dispatcher);
            PreventCommand.registerCommand(dispatcher);
            MobRiderCommand.registerCommand(dispatcher);
        }
        );
    }
}