package xyz.antideath.aca.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import xyz.antideath.aca.mixin.Invoker.command.OpCommand_Invoker;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collections;
import java.util.Objects;

import static xyz.antideath.aca.AcaSetting.enableFastOpCommand;
import static net.minecraft.server.command.CommandManager.literal;

public class FastOpCommand {
    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> argument = literal("fastop")
                    .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableFastOpCommand))
                        .executes(context -> OpCommand_Invoker.invokeOp(context.getSource(), Collections.singleton(Objects.requireNonNull(context.getSource().getPlayer()).getGameProfile())));
        dispatcher.register(argument);
    }
}