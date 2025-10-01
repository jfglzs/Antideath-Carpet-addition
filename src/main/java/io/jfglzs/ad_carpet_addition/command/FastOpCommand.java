package io.jfglzs.ad_carpet_addition.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collections;

import static io.jfglzs.ad_carpet_addition.AcaSetting.enableFastOpCommand;
import static io.jfglzs.ad_carpet_addition.mixin.Invoker.command.OpCommandInvoker.invokeOp;

public class FastOpCommand
{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("fast_op")
                .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableFastOpCommand))
                .executes(context -> invokeOp(context.getSource(), Collections.singleton(context.getSource().getPlayer().getGameProfile()))));
    }
}