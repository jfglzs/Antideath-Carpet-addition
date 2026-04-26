package io.github.jfglzs.aca.command;

import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.jfglzs.aca.mixin.Invoker.TelePortCommand_Invoker;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;

import static io.github.jfglzs.aca.AcaSetting.enableSpecTPCommand;


public class SpecTeleportCommand {
    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> argument = Commands.literal("sp")
                .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableSpecTPCommand))
                .then(Commands.argument("player", EntityArgument.player()).executes((context) -> execute(context.getSource(), EntityArgument.getPlayer(context, "player"))));

        dispatcher.register(argument);
    }

    private static int execute(CommandSourceStack source, Player player1) {
        Player player = source.getPlayer();
        if (player == null) return -1;
        if (player.isSpectator()) {
            return TelePortCommand_Invoker.executeInvoker(source, Collections.singleton(player), player1);
        } else {
            Messenger.m(player, "r you're not spectator");
            return 0;
        }
    }
}
