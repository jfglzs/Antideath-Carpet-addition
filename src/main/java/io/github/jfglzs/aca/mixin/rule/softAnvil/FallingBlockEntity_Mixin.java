package io.github.jfglzs.aca.mixin.rule.softAnvil;

import io.github.jfglzs.aca.AcaSetting;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlockEntity.class)
public class FallingBlockEntity_Mixin {
    @Shadow
    private boolean destroyedOnLanding;

    @Inject(
            method = "handleFallDamage",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"),
            cancellable = true
    )
    public void handleFallDamage_Inject(double fallDistance, float damagePerDistance, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (AcaSetting.softAnvil) {
            this.destroyedOnLanding = true;
            cir.setReturnValue(false);
        }
    }
}
