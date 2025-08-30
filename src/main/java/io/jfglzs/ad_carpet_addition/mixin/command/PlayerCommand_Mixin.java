package io.jfglzs.ad_carpet_addition.mixin.command;

import carpet.commands.PlayerCommand;
import carpet.fakes.ServerPlayerInterface;
import carpet.helpers.EntityPlayerActionPack;
import carpet.helpers.EntityPlayerActionPack.ActionType;
import carpet.patches.EntityPlayerMPFake;
import carpet.utils.Messenger;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.jfglzs.ad_carpet_addition.commands.PlayerActionPackHelper;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Consumer;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@Mixin(PlayerCommand.class)
public class PlayerCommand_Mixin {
    @Unique
    private static void manipulate(CommandContext<ServerCommandSource> context, Consumer<EntityPlayerActionPack> action) {
        try {
            String playerName = context.getArgument("player", String.class);
            ServerPlayerEntity player = context.getSource().getServer().getPlayerManager().getPlayer(playerName);
            if (!(player instanceof EntityPlayerMPFake)) {
                Messenger.m(context.getSource(), "r This command only works for fake players");
                return;
            }
            EntityPlayerActionPack ap = ((ServerPlayerInterface)player).getActionPack();
            action.accept(ap);
        } catch (Exception e) {
            Messenger.m(context.getSource(), "r Failed to execute fake player action: " + e.getMessage());
        }
    }


    /**
     * 给 action 命令增加 time 子命令
     */
    @Inject(method = "makeActionCommand", at = @At("RETURN"), cancellable = true, remap = false)
    private static void applyPlayerActionEnhancements(String actionName, ActionType type, CallbackInfoReturnable<LiteralArgumentBuilder<ServerCommandSource>> cir) {
        String timeArg = "worldTime";

        LiteralArgumentBuilder<ServerCommandSource> command = cir.getReturnValue();

        command.then(literal("worldTime")
                .then(argument(timeArg, IntegerArgumentType.integer(0 , 23999))
                        .executes(ctx -> {
                            int WorldTime = IntegerArgumentType.getInteger(ctx, timeArg);
                            manipulate(ctx, ap -> ap.start(type, PlayerActionPackHelper.worldTime(WorldTime)));
                            cir.setReturnValue(command);
                            return 1;
                        })
                )
        );
    }
}
