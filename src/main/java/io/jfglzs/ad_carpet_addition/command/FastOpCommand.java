package io.jfglzs.ad_carpet_addition.command;

import com.mojang.brigadier.CommandDispatcher;
import io.jfglzs.ad_carpet_addition.mixin.Invoker.command.OpCommand_Invoker;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collections;
import java.util.Objects;

import static io.jfglzs.ad_carpet_addition.AcaSetting.enableFastOpCommand;

public class FastOpCommand
{
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("fast_op")
                    .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableFastOpCommand))
                        .executes(context -> OpCommand_Invoker.invokeOp(context.getSource(), Collections.singleton(Objects.requireNonNull(context.getSource().getPlayer()).getGameProfile()))));
    }
}