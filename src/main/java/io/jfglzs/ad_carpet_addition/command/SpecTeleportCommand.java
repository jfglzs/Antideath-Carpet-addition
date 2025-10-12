package io.jfglzs.ad_carpet_addition.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.jfglzs.ad_carpet_addition.mixin.Invoker.command.TelePortCommand_Invoker;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collections;
import java.util.Objects;

import static io.jfglzs.ad_carpet_addition.AcaSetting.enableSpecTPCommand;

public class SpecTeleportCommand
{

    public static void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
    {
        dispatcher.register(CommandManager.literal("sp")
                      .requires((source) -> carpet.utils.CommandHelper.canUseCommand(source, enableSpecTPCommand) && Objects.requireNonNull(source.getPlayer()).isSpectator())
                          .then(CommandManager.argument("player", EntityArgumentType.player()).executes((context) -> execute(context.getSource(), EntityArgumentType.getPlayer(context, "player")))));
    }

    private static int execute(ServerCommandSource source, PlayerEntity playerEntity) throws CommandSyntaxException
    {
        return TelePortCommand_Invoker.executeInvoker(source , Collections.singleton(source.getPlayer()) , playerEntity);
    }

}
