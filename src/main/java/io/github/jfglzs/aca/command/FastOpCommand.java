package io.github.jfglzs.aca.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.jfglzs.aca.mixin.Invoker.OpCommand_Invoker;
import net.minecraft.commands.CommandSourceStack;

import java.util.Collections;
import java.util.Objects;

import static io.github.jfglzs.aca.AcaSetting.enableFastOpCommand;
import static net.minecraft.commands.Commands.literal;


public class FastOpCommand {
    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> argument = literal("fastop")
                .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableFastOpCommand))
                .executes(context -> OpCommand_Invoker.invokeOp(context.getSource(), Collections.singleton(Objects.requireNonNull(context.getSource().getPlayer()).getGameProfile())));
        dispatcher.register(argument);
    }
}