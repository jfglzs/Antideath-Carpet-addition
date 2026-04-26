package io.github.jfglzs.aca.mixin.rule.anvilNeverDamageByFalling;


import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public abstract class AnvilBlock_Mixin {
    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getLandingState_Inject(BlockState fallingState, CallbackInfoReturnable<BlockState> cir) {
        if (AcaSetting.anvilNeverDamageByFalling) {
            cir.setReturnValue(fallingState);
        }
    }
}
