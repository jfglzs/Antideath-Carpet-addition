package io.github.jfglzs.aca.command;

import carpet.patches.EntityPlayerMPFake;
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
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

import static net.minecraft.commands.Commands.literal;

public class PlayerCommand {
    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> argument = literal("player")
                .requires((source) -> CommandHelper.canUseCommand(source, AcaSetting.enablePlayerLockCommand))
                .then(Commands.argument("player", StringArgumentType.word())
                .then(Commands.literal("lock").executes(PlayerCommand::lockPlayer))
                .then(Commands.literal("unlock").executes(PlayerCommand::unlockPlayer))
//                .then(Commands.literal("makePrivate")).executes(PlayerCommand::makePrivate)
//                .then(Commands.literal("makePublic")).executes(PlayerCommand::makePublic)
                );
        dispatcher.register(argument);
    }

    public static int lockPlayer(CommandContext<CommandSourceStack> ctx) {
        var player = PlayerCommand_Invoker.getPlayer(ctx);
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
        else if (player instanceof ServerPlayer) {
            Messenger.m(ctx.getSource(), "r you cant lock real player");
        }
        else {
            Messenger.m(ctx.getSource(), "r player doesnt exist");
        }
        return 0;
    }

    public static int unlockPlayer(CommandContext<CommandSourceStack> ctx) {
        var player = PlayerCommand_Invoker.getPlayer(ctx);
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

//    public static int makePrivate(CommandContext<CommandSourceStack> ctx) {
//        var player_ = PlayerCommand_Invoker.getPlayer(ctx);
//        var fakesAndOwners = AcaSetting.config.privateFakesAndOwners;
//        if (player_ instanceof EntityPlayerMPFake fakePlayer) {
//            for (UUID uuid : fakesAndOwners.keySet()) {
//                if (fakesAndOwners.get(uuid) instanceof List<UUID> uuids) {
//                    if (uuids.contains(fakePlayer.getUUID())) {
//                        Messenger.m(ctx.getSource(), "r player %s is already own by %s".formatted(fakePlayer.getName().getString(), ctx.getSource().getServer().getPlayerList().getPlayer(uuid)));
//                        return 1;
//                    }
//                }
//            }
//
//            if (ctx.getSource().getPlayer() instanceof ServerPlayer realPlayer) {
//                var fakePlayerUUID = fakePlayer.getUUID();
//                var realPlayerUUID = realPlayer.getUUID();
//                if (fakesAndOwners.get(fakePlayerUUID) instanceof List<UUID> uuids) {
//                    uuids.add(fakePlayerUUID);
//                }
//                else {
//                    fakesAndOwners.put(realPlayerUUID, new ArrayList<>(Collections.singleton(fakePlayerUUID)));
//                }
//                Messenger.m(realPlayer, "g fake player %s is own by you".formatted(fakePlayer.getName().getString()));
//                return 0;
//            }
//        }
//        return 1;
//    }

//    private static int makePublic(CommandContext<CommandSourceStack> context) {
//        if (context.getSource().getPlayer() instanceof ServerPlayer) {
//
//        }
//        return 0;
//    }
}
