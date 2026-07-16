package io.github.jfglzs.aca.mixin.rule.shulkerOptimization;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class Entity_Mixin {
    //~ if < 26.1 'updateFluidInteraction' -> 'updateInWaterStateAndDoFluidPushing' {
    @Inject(
            method = "updateFluidInteraction",
            at = @At("HEAD"),
            cancellable = true
    )
    //~}
    private void updateFluidInteraction(CallbackInfoReturnable<Boolean> cir) {
        if (AcaSetting.shulkerOptimization) {
            Entity self = (Entity) ((Object) this);
            if (self instanceof ShulkerBullet || self instanceof Shulker) {
                cir.setReturnValue(false);
            }
        }
    }

    //~ if > 1.21.1 'checkInsideBlocks' -> 'applyEffectsFromBlocks()V' {
    @Inject(
            method = "applyEffectsFromBlocks()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tickBlockCollision_Inject(CallbackInfo ci) {
        if (AcaSetting.shulkerOptimization && (((Entity) (Object) this)) instanceof Shulker) {
            ci.cancel();
        }
    }
    //~}
}
