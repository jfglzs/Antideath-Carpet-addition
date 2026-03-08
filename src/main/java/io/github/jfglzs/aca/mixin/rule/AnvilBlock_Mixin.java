package io.github.jfglzs.aca.mixin.rule;


import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlock_Mixin {
    @Inject(
            method = "getLandingState",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getLandingStateInject(BlockState fallingState, CallbackInfoReturnable<BlockState> cir) {
        if (AcaSetting.anvilNeverDamageByFalling) {
            cir.setReturnValue(fallingState);
        }
    }
}
