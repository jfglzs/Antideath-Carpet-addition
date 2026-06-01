package io.github.jfglzs.aca.command;

import carpet.utils.CommandHelper;
import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.EntityPlayerMPFakeAccessor;
import io.github.jfglzs.aca.mixin.carpet.PlayerCommand_Invoker;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.literal;

public class PlayerLockCommand {
    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> argument = literal("player")
                .requires((source) -> CommandHelper.canUseCommand(source, AcaSetting.enablePlayerLockCommand))
                .then(Commands.argument("player", StringArgumentType.word())
                .then(Commands.literal("lock")
                        .executes(PlayerLockCommand::lockPlayer)
                )
                .then(Commands.literal("unlock")
                        .executes(PlayerLockCommand::unlockPlayer)
                ));
        dispatcher.register(argument);
    }

    public static int lockPlayer(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = PlayerCommand_Invoker.getPlayer(ctx);
        if (player instanceof EntityPlayerMPFakeAccessor accessor) {
            if (accessor.aca$getLockState()) {
                Messenger.m(ctx.getSource(), "r player is locked");
                return 1;
            }
            else {
                accessor.aca$setLockState(true);
                Messenger.m(ctx.getSource(), "g player %s is locked".formatted(player.getName().getString()));
            }
        }
        else {
            Messenger.m(ctx.getSource(), "r you cant lock real player");
        }
        return 0;
    }

    public static int unlockPlayer(CommandContext<CommandSourceStack> ctx) {
        ServerPlayer player = PlayerCommand_Invoker.getPlayer(ctx);
        if (player instanceof EntityPlayerMPFakeAccessor accessor) {
            if (!accessor.aca$getLockState()) {
                Messenger.m(ctx.getSource(), "r player is unlocked");
                return 1;
            }
            else {
                Messenger.m(ctx.getSource(), "g player %s is unlocked".formatted(player.getName().getString()));
                accessor.aca$setLockState(false);
            }
        }
        return 0;
    }
}
