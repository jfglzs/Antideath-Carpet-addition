package io.jfglzs.ad_carpet_addition.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.jfglzs.ad_carpet_addition.mixin.Invoker.command.TelePortCommand_Invoker;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collections;
import java.util.Objects;

import static io.jfglzs.ad_carpet_addition.AcaSetting.enableSpecTPCommand;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SpecTeleportCommand
{

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        LiteralArgumentBuilder<ServerCommandSource> argument = literal("sp")
                      .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableSpecTPCommand) && Objects.requireNonNull(source.getPlayer()).isSpectator())
                          .then(argument("player", EntityArgumentType.player()).executes((context) -> execute(context.getSource(), EntityArgumentType.getPlayer(context, "player"))));

        dispatcher.register(argument);
    }

    private static int execute(ServerCommandSource source, PlayerEntity playerEntity) throws CommandSyntaxException
    {
        return TelePortCommand_Invoker.executeInvoker(source , Collections.singleton(source.getPlayer()) , playerEntity);
    }

}
