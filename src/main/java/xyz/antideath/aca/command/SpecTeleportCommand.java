package xyz.antideath.aca.command;

import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import xyz.antideath.aca.mixin.Invoker.command.TelePortCommand_Invoker;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collections;

import static xyz.antideath.aca.AcaSetting.enableSpecTPCommand;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SpecTeleportCommand {

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> argument = literal("sp")
                      .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableSpecTPCommand))
                          .then(argument("player", EntityArgumentType.player()).executes((context) -> execute(context.getSource(), EntityArgumentType.getPlayer(context, "player"))));

        dispatcher.register(argument);
    }

    private static int execute(ServerCommandSource source, PlayerEntity player1) throws CommandSyntaxException {
        PlayerEntity player = source.getPlayer();
        if (player == null) return -1;
        if (player.isSpectator()) {
            return TelePortCommand_Invoker.executeInvoker(source , Collections.singleton(player) , player1);
        } else {
            Messenger.m(player, "r you're not spectator");
            return 0;
        }
    }

}
