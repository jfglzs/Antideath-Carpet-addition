package io.github.jfglzs.aca.mixin.carpet;

import carpet.commands.PlayerCommand;
import carpet.utils.Messenger;
import com.mojang.brigadier.context.CommandContext;
import io.github.jfglzs.aca.AcaSetting;
import io.github.jfglzs.aca.accessors.EntityPlayerMPFakeAccessor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerCommand.class)
public abstract class PlayerCommand_Mixin {
    @Shadow
    private static ServerPlayer getPlayer(CommandContext<CommandSourceStack> context) {
        return null;
    };

    @Inject(
            method = "kill",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void kill(CommandContext<CommandSourceStack> context, CallbackInfoReturnable<Integer> cir) {
        if (getPlayer(context) instanceof EntityPlayerMPFakeAccessor accessor && !AcaSetting.enablePlayerLockCommand.equals("false")) {
            if (accessor.aca$getLockState()) {
                Messenger.m(context.getSource(), "r you cant kill this player because its locked");
                cir.setReturnValue(1);
            }
        }
    }

    @Inject(
            method = "cantSpawn",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void cantSpawn(CommandContext<CommandSourceStack> context, CallbackInfoReturnable<Integer> cir) {

    }
}
